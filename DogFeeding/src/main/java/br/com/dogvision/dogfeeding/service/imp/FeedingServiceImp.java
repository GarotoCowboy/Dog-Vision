package br.com.dogvision.dogfeeding.service.imp;

import br.com.dogvision.dogfeeding.dto.create.CreateFeedingItemRequest;
import br.com.dogvision.dogfeeding.dto.create.CreateFeedingRequest;
import br.com.dogvision.dogfeeding.dto.mapper.FeedingMapper;
import br.com.dogvision.dogfeeding.dto.response.ConsumptionReportResponse;
import br.com.dogvision.dogfeeding.dto.response.FeedingItemResponse;
import br.com.dogvision.dogfeeding.dto.response.FeedingResponse;
import br.com.dogvision.dogfeeding.dto.update.UpdateFeedingItemRequest;
import br.com.dogvision.dogfeeding.dto.update.UpdateFeedingRequest;
import br.com.dogvision.dogfeeding.infra.exception.FeedingNotFoundException;
import br.com.dogvision.dogfeeding.infra.exception.InsufficientRationStockException;
import br.com.dogvision.dogfeeding.infra.exception.InvalidRationStateException;
import br.com.dogvision.dogfeeding.infra.exception.RationNotFoundException;
import br.com.dogvision.dogfeeding.model.Feeding;
import br.com.dogvision.dogfeeding.model.FeedingItem;
import br.com.dogvision.dogfeeding.model.MealType;
import br.com.dogvision.dogfeeding.model.Ration;
import br.com.dogvision.dogfeeding.model.RationType;
import br.com.dogvision.dogfeeding.repository.FeedingItemRepository;
import br.com.dogvision.dogfeeding.repository.FeedingRepository;
import br.com.dogvision.dogfeeding.repository.RationRepository;
import br.com.dogvision.dogfeeding.service.FeedingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FeedingServiceImp implements FeedingService {

    private final FeedingRepository repository;
    private final FeedingItemRepository feedingItemRepository;
    private final RationRepository rationRepository;
    private final FeedingMapper mapper;

    @Override
    public List<FeedingResponse> findAll() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(Feeding::getDate).thenComparing(Feeding::getFeedingTime).reversed())
                .map(this::toResponse)
                .toList();
    }

    @Override
    public FeedingResponse findById(UUID id) {
        return toResponse(findEntity(id));
    }

    @Override
    public List<FeedingResponse> findByDogId(UUID dogId) {
        return repository.findAllByDogId(dogId).stream()
                .sorted(Comparator.comparing(Feeding::getDate).thenComparing(Feeding::getFeedingTime).reversed())
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<FeedingResponse> search(UUID dogId, LocalDate startDate, LocalDate endDate, MealType mealType, RationType rationType) {
        validateDateRange(startDate, endDate);

        return repository.findAll().stream()
                .filter(feeding -> dogId == null || feeding.getDogId().equals(dogId))
                .filter(feeding -> startDate == null || !feeding.getDate().isBefore(startDate))
                .filter(feeding -> endDate == null || !feeding.getDate().isAfter(endDate))
                .filter(feeding -> mealType == null || feeding.getMealType() == mealType)
                .filter(feeding -> rationType == null || feeding.getItems().stream().anyMatch(item -> item.getRation().getRationType() == rationType))
                .sorted(Comparator.comparing(Feeding::getDate).thenComparing(Feeding::getFeedingTime).reversed())
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<ConsumptionReportResponse> consumptionReport(UUID dogId, LocalDate startDate, LocalDate endDate, RationType rationType) {
        validateDateRange(startDate, endDate);
        List<FeedingItem> items = feedingItemRepository.findForConsumptionReport(dogId, rationType, startDate, endDate);

        Map<String, Double> totals = new LinkedHashMap<>();
        Map<String, FeedingItem> itemReference = new LinkedHashMap<>();

        for (FeedingItem item : items) {
            String key = item.getFeeding().getDogId() + "|" + item.getRation().getId();
            totals.merge(key, item.getQuantityUsed(), Double::sum);
            itemReference.putIfAbsent(key, item);
        }

        return totals.entrySet().stream()
                .map(entry -> {
                    FeedingItem item = itemReference.get(entry.getKey());
                    return new ConsumptionReportResponse(
                            item.getFeeding().getDogId(),
                            item.getRation().getId(),
                            item.getRation().getName(),
                            entry.getValue(),
                            item.getUnit(),
                            startDate,
                            endDate
                    );
                })
                .toList();
    }

    @Override
    public FeedingResponse save(CreateFeedingRequest dto, UUID loggedUserId) {
        Feeding feeding = mapper.toEntity(dto);
        List<FeedingItem> items = buildItemsForCreate(feeding, dto.items(), dto.date());
        feeding.setItems(items);
        feeding.setQuantity(totalQuantity(items));
        Feeding savedFeeding = repository.save(feeding);
        return toResponse(savedFeeding);
    }

    @Override
    public FeedingResponse update(UUID id, UpdateFeedingRequest dto, UUID loggedUserId) {
        Feeding feeding = findEntity(id);
        restoreStock(feeding.getItems());
        mapper.updateFromDto(dto, feeding);

        LocalDate feedingDate = feeding.getDate();
        List<FeedingItem> items = dto.items() != null
                ? buildItemsForUpdate(feeding, dto.items(), feedingDate)
                : rebuildExistingItems(feeding, feedingDate);

        feeding.getItems().clear();
        feeding.getItems().addAll(items);
        feeding.setQuantity(totalQuantity(items));

        Feeding updatedFeeding = repository.save(feeding);
        return toResponse(updatedFeeding);
    }

    @Override
    public void delete(UUID id, UUID loggedUserId) {
        Feeding feeding = findEntity(id);
        restoreStock(feeding.getItems());
        repository.delete(feeding);
    }

    private Feeding findEntity(UUID id) {
        return repository.findById(id).orElseThrow(() -> new FeedingNotFoundException(id));
    }

    private FeedingResponse toResponse(Feeding feeding) {
        List<FeedingItemResponse> itemResponses = feeding.getItems().stream()
                .map(item -> new FeedingItemResponse(
                        item.getRation().getId(),
                        item.getRation().getName(),
                        item.getRation().getRationType().name(),
                        item.getQuantityUsed(),
                        item.getUnit()
                ))
                .toList();

        return new FeedingResponse(
                feeding.getId(),
                feeding.getDogId(),
                feeding.getDate(),
                feeding.getFeedingTime(),
                feeding.getMealType(),
                itemResponses,
                feeding.getQuantity(),
                feeding.getNotes(),
                feeding.getDogResponse(),
                feeding.getCreatedAt(),
                feeding.getUpdatedAt()
        );
    }

    private List<FeedingItem> buildItemsForCreate(Feeding feeding, List<CreateFeedingItemRequest> requests, LocalDate feedingDate) {
        return requests.stream()
                .map(request -> createItem(feeding, request.rationId(), request.quantityUsed(), request.unit(), feedingDate))
                .toList();
    }

    private List<FeedingItem> buildItemsForUpdate(Feeding feeding, List<UpdateFeedingItemRequest> requests, LocalDate feedingDate) {
        return requests.stream()
                .map(request -> createItem(feeding, request.rationId(), request.quantityUsed(), request.unit(), feedingDate))
                .toList();
    }

    private List<FeedingItem> rebuildExistingItems(Feeding feeding, LocalDate feedingDate) {
        return feeding.getItems().stream()
                .map(item -> createItem(feeding, item.getRation().getId(), item.getQuantityUsed(), item.getUnit(), feedingDate))
                .toList();
    }

    private FeedingItem createItem(Feeding feeding, UUID rationId, double quantityUsed, br.com.dogvision.dogfeeding.model.MeasurementUnit unit, LocalDate feedingDate) {
        Ration ration = rationRepository.findById(rationId).orElseThrow(() -> new RationNotFoundException(rationId));
        validateRationAvailability(ration, quantityUsed, feedingDate);
        ration.setCurrentRationQuantity(ration.getCurrentRationQuantity() - quantityUsed);

        FeedingItem item = new FeedingItem();
        item.setFeeding(feeding);
        item.setRation(ration);
        item.setQuantityUsed(quantityUsed);
        item.setUnit(unit);
        return item;
    }

    private void validateRationAvailability(Ration ration, double quantityUsed, LocalDate feedingDate) {
        if (ration.getCurrentRationQuantity() < quantityUsed) {
            throw new InsufficientRationStockException(ration.getId(), ration.getCurrentRationQuantity(), quantityUsed);
        }
    }

    private void restoreStock(List<FeedingItem> items) {
        for (FeedingItem item : items) {
            Ration ration = item.getRation();
            ration.setCurrentRationQuantity(ration.getCurrentRationQuantity() + item.getQuantityUsed());
            if (ration.getCurrentRationQuantity() > ration.getTotalRationQuantity()) {
                throw new InvalidRationStateException("Restored stock would exceed total quantity for ration " + ration.getId());
            }
        }
    }

    private double totalQuantity(List<FeedingItem> items) {
        return items.stream().mapToDouble(FeedingItem::getQuantityUsed).sum();
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new InvalidRationStateException("End date cannot be before start date");
        }
    }
}
