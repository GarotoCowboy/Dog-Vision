package br.com.dogvision.dogfeeding.service.imp;

import br.com.dogvision.dogfeeding.dto.create.CreateRationRequest;
import br.com.dogvision.dogfeeding.dto.mapper.RationMapper;
import br.com.dogvision.dogfeeding.dto.response.DogRationConsumptionResponse;
import br.com.dogvision.dogfeeding.dto.response.RationAlertResponse;
import br.com.dogvision.dogfeeding.dto.response.RationConsumptionEstimateResponse;
import br.com.dogvision.dogfeeding.dto.response.RationResponse;
import br.com.dogvision.dogfeeding.dto.update.DecreaseRationStockRequest;
import br.com.dogvision.dogfeeding.dto.update.IncreaseRationStockRequest;
import br.com.dogvision.dogfeeding.dto.update.UpdateRationRequest;
import br.com.dogvision.dogfeeding.infra.exception.InvalidRationStateException;
import br.com.dogvision.dogfeeding.infra.exception.RationInUseException;
import br.com.dogvision.dogfeeding.infra.exception.RationNotFoundException;
import br.com.dogvision.dogfeeding.infra.rabbit.ration.RationEventPublisher;
import br.com.dogvision.dogfeeding.model.FeedingPlan;
import br.com.dogvision.dogfeeding.model.Ration;
import br.com.dogvision.dogfeeding.model.RationStockStatus;
import br.com.dogvision.dogfeeding.model.RationType;
import br.com.dogvision.dogfeeding.repository.FeedingItemRepository;
import br.com.dogvision.dogfeeding.repository.FeedingPlanRepository;
import br.com.dogvision.dogfeeding.repository.RationRepository;
import br.com.dogvision.dogfeeding.service.RationService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RationServiceImp implements RationService {

    private final RationRepository repository;
    private final FeedingItemRepository feedingItemRepository;
    private final FeedingPlanRepository feedingPlanRepository;
    private final RationMapper mapper;
    private final RabbitTemplate rabbitTemplate;
    private final RationEventPublisher rationEventPublisher;

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
    public RationConsumptionEstimateResponse getEstimate(UUID id) {
        Ration ration = findEntity(id);
        return toEstimateResponse(ration, LocalDate.now());
    }

    @Override
    public List<RationConsumptionEstimateResponse> getAllEstimates() {
        LocalDate today = LocalDate.now();
        return repository.findAll().stream()
                .sorted(Comparator.comparing(Ration::getName, String.CASE_INSENSITIVE_ORDER))
                .map(ration -> toEstimateResponse(ration, today))
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

        Ration saved = repository.save((ration));

        rationEventPublisher.publishQuantityUpdated(saved);

        return toResponse(saved);
    }

    @Transactional
    @Override
    public RationResponse increaseRation(UUID id, IncreaseRationStockRequest dto, UUID loggedUserId) {
        Ration ration = findEntity(id);
        if (dto.bagCount() == null || dto.bagCount() <= 0) {
            throw new InvalidRationStateException("Bag count must be positive");
        }
        if (dto.weightPerBagKg() == null || dto.weightPerBagKg() <= 0) {
            throw new InvalidRationStateException("Weight per bag must be positive");
        }

        double totalAdded = dto.totalAddedWeightKg();
        ration.setCurrentRationQuantity(round2(ration.getCurrentRationQuantity() + totalAdded));
        validateRationState(ration);

        Ration saved = repository.save(ration);
        rationEventPublisher.publishQuantityUpdated(saved);

        return toResponse(saved);
    }

    @Transactional
    @Override
    public RationResponse decreaseRation(UUID id, DecreaseRationStockRequest dto, UUID loggedUserId) {
        Ration ration = findEntity(id);
        if (dto.quantityKg() == null || dto.quantityKg() <= 0) {
            throw new InvalidRationStateException("Quantity to decrease must be positive");
        }

        double newQuantity = round2(Math.max(0.0, ration.getCurrentRationQuantity() - dto.quantityKg()));
        ration.setCurrentRationQuantity(newQuantity);
        validateRationState(ration);

        Ration saved = repository.save(ration);
        rationEventPublisher.publishQuantityUpdated(saved);

        return toResponse(saved);
    }

    @Transactional
    @Override
    public RationResponse applyDailyConsumption(UUID id, UUID loggedUserId) {
        Ration ration = findEntity(id);
        LocalDate today = LocalDate.now();
        if (ration.getLastDailyDeductionDate() != null && ration.getLastDailyDeductionDate().isEqual(today)) {
            throw new InvalidRationStateException("A baixa diária para a ração " + ration.getName() + " já foi realizada hoje");
        }

        RationConsumptionEstimateResponse estimate = getEstimate(id);
        if (estimate.totalDailyConsumptionKg() != null && estimate.totalDailyConsumptionKg() > 0) {
            double newQuantity = round2(Math.max(0.0, ration.getCurrentRationQuantity() - estimate.totalDailyConsumptionKg()));
            ration.setCurrentRationQuantity(newQuantity);
            ration.setLastDailyDeductionDate(today);
            validateRationState(ration);

            Ration saved = repository.save(ration);
            rationEventPublisher.publishQuantityUpdated(saved);
            return toResponse(saved);
        }
        return toResponse(ration);
    }

    @Transactional
    @Override
    public List<RationResponse> applyDailyConsumptionAll(UUID loggedUserId) {
        LocalDate today = LocalDate.now();
        List<Ration> all = repository.findAll();
        List<RationResponse> updated = new ArrayList<>();
        for (Ration ration : all) {
            if (ration.getLastDailyDeductionDate() != null && ration.getLastDailyDeductionDate().isEqual(today)) {
                continue;
            }
            RationConsumptionEstimateResponse est = getEstimate(ration.getId());
            if (est.totalDailyConsumptionKg() != null && est.totalDailyConsumptionKg() > 0) {
                double newQuantity = round2(Math.max(0.0, ration.getCurrentRationQuantity() - est.totalDailyConsumptionKg()));
                ration.setCurrentRationQuantity(newQuantity);
                ration.setLastDailyDeductionDate(today);
                validateRationState(ration);

                Ration saved = repository.save(ration);
                rationEventPublisher.publishQuantityUpdated(saved);
                updated.add(toResponse(saved));
            }
        }
        return updated;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void scheduledDailyConsumption() {
        LocalDate today = LocalDate.now();
        List<Ration> all = repository.findAll();
        for (Ration r : all) {
            if (r.getLastDailyDeductionDate() != null && r.getLastDailyDeductionDate().isEqual(today)) {
                continue;
            }
            List<FeedingPlan> activePlans = feedingPlanRepository.findActivePlansByRationId(r.getId(), today);
            double dailyTotal = activePlans.stream().mapToDouble(FeedingPlan::getDailyQuantityKg).sum();
            if (dailyTotal > 0) {
                double newQuantity = round2(Math.max(0.0, r.getCurrentRationQuantity() - dailyTotal));
                r.setCurrentRationQuantity(newQuantity);
                r.setLastDailyDeductionDate(today);
                Ration saved = repository.save(r);
                rationEventPublisher.publishQuantityUpdated(saved);
            }
        }
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
        if (ration.getCurrentRationQuantity() < 0) {
            throw new InvalidRationStateException("Current ration quantity cannot be negative");
        }
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private RationResponse toResponse(Ration ration) {
        LocalDate today = LocalDate.now();
        return new RationResponse(
                ration.getId(),
                ration.getName(),
                ration.getRationType(),
                round2(ration.getCurrentRationQuantity()),
                ration.getRegistrationDate(),
                ration.getStockStatus(today),
                ration.getLastDailyDeductionDate()
        );
    }

    private RationConsumptionEstimateResponse toEstimateResponse(Ration ration, LocalDate referenceDate) {
        List<FeedingPlan> activePlans = feedingPlanRepository.findActivePlansByRationId(ration.getId(), referenceDate);

        List<DogRationConsumptionResponse> dogConsumptions = activePlans.stream()
                .map(plan -> new DogRationConsumptionResponse(
                        plan.getDogId(),
                        plan.getId(),
                        plan.getName(),
                        round2(plan.getDailyQuantityKg())
                ))
                .toList();

        double totalDailyConsumption = activePlans.stream()
                .mapToDouble(FeedingPlan::getDailyQuantityKg)
                .sum();

        Double estimatedDaysRemaining = null;
        LocalDate estimatedDepletionDate = null;

        if (ration.getCurrentRationQuantity() <= 0) {
            estimatedDaysRemaining = 0.0;
            estimatedDepletionDate = referenceDate;
        } else if (totalDailyConsumption > 0) {
            estimatedDaysRemaining = round2(ration.getCurrentRationQuantity() / totalDailyConsumption);
            long wholeDays = (long) Math.floor(estimatedDaysRemaining);
            estimatedDepletionDate = referenceDate.plusDays(wholeDays);
        }

        boolean dailyDeductionAppliedToday = ration.getLastDailyDeductionDate() != null
                && ration.getLastDailyDeductionDate().isEqual(referenceDate);

        return new RationConsumptionEstimateResponse(
                ration.getId(),
                ration.getName(),
                ration.getRationType(),
                round2(ration.getCurrentRationQuantity()),
                round2(totalDailyConsumption),
                estimatedDaysRemaining,
                estimatedDepletionDate,
                ration.getStockStatus(referenceDate),
                dogConsumptions,
                ration.getLastDailyDeductionDate(),
                dailyDeductionAppliedToday
        );
    }
}
