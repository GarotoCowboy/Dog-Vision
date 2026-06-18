package br.com.dogvision.dogfeeding.service.imp;

import br.com.dogvision.dogfeeding.dto.create.CreateRationRequest;
import br.com.dogvision.dogfeeding.dto.mapper.RationMapper;
import br.com.dogvision.dogfeeding.dto.response.RationAlertResponse;
import br.com.dogvision.dogfeeding.dto.response.RationResponse;
import br.com.dogvision.dogfeeding.dto.update.UpdateRationRequest;
import br.com.dogvision.dogfeeding.infra.exception.InvalidRationStateException;
import br.com.dogvision.dogfeeding.infra.exception.RationInUseException;
import br.com.dogvision.dogfeeding.infra.exception.RationNotFoundException;
import br.com.dogvision.dogfeeding.model.Ration;
import br.com.dogvision.dogfeeding.model.RationStockStatus;
import br.com.dogvision.dogfeeding.model.RationType;
import br.com.dogvision.dogfeeding.repository.FeedingItemRepository;
import br.com.dogvision.dogfeeding.repository.RationRepository;
import br.com.dogvision.dogfeeding.service.RationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RationServiceImp implements RationService {

    private final RationRepository repository;
    private final FeedingItemRepository feedingItemRepository;
    private final RationMapper mapper;

    @Override
    public List<RationResponse> findAll() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(Ration::getName, String.CASE_INSENSITIVE_ORDER))
                .map(this::toResponse)
                .toList();
    }

    @Override
    public RationResponse findById(UUID id) {
        return toResponse(findEntity(id));
    }

    @Override
    public List<RationResponse> search(RationType rationType, RationStockStatus stockStatus) {
        LocalDate today = LocalDate.now();
        return repository.findAll().stream()
                .filter(ration -> rationType == null || ration.getRationType() == rationType)
                .filter(ration -> stockStatus == null || ration.getStockStatus(today) == stockStatus)
                .sorted(Comparator.comparing(Ration::getName, String.CASE_INSENSITIVE_ORDER))
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<RationAlertResponse> alerts() {
        LocalDate today = LocalDate.now();
        return repository.findAll().stream()
                .filter(ration -> ration.getStockStatus(today) != RationStockStatus.HEALTHY)
                .sorted(Comparator.comparing(Ration::getName, String.CASE_INSENSITIVE_ORDER))
                .map(ration -> new RationAlertResponse(
                        ration.getId(),
                        ration.getName(),
                        ration.getStockStatus(today)
                ))
                .toList();
    }

    @Override
    public RationResponse save(CreateRationRequest dto, UUID loggedUserId) {
        Ration ration = mapper.toEntity(dto);
        validateRationState(ration);
        return toResponse(repository.save(ration));
    }

    @Override
    public RationResponse update(UUID id, UpdateRationRequest dto, UUID loggedUserId) {
        Ration ration = findEntity(id);
        mapper.updateFromDto(dto, ration);
        validateRationState(ration);
        return toResponse(repository.save(ration));
    }

    @Override
    public void delete(UUID id, UUID loggedUserId) {
        Ration ration = findEntity(id);
        if (feedingItemRepository.existsByRationId(id)) {
            throw new RationInUseException(id);
        }
        repository.delete(ration);
    }

    private Ration findEntity(UUID id) {
        return repository.findById(id).orElseThrow(() -> new RationNotFoundException(id));
    }

    private void validateRationState(Ration ration) {
        if (ration.getCurrentRationQuantity() > ration.getTotalRationQuantity()) {
            throw new InvalidRationStateException("Current ration quantity cannot be greater than total ration quantity");
        }
    }

    private RationResponse toResponse(Ration ration) {
        LocalDate today = LocalDate.now();
        return new RationResponse(
                ration.getId(),
                ration.getName(),
                ration.getRationType(),
                ration.getTotalRationQuantity(),
                ration.getCurrentRationQuantity(),
                ration.getRegistrationDate(),
                ration.getStockStatus(today)
        );
    }
}
