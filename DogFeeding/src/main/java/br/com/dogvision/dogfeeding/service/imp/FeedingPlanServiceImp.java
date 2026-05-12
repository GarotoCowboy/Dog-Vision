package br.com.dogvision.dogfeeding.service.imp;

import br.com.dogvision.dogfeeding.dto.create.CreateFeedingPlanRequest;
import br.com.dogvision.dogfeeding.dto.response.FeedingPlanResponse;
import br.com.dogvision.dogfeeding.dto.update.UpdateFeedingPlanRequest;
import br.com.dogvision.dogfeeding.infra.exception.FeedingPlanNotFoundException;
import br.com.dogvision.dogfeeding.infra.exception.InvalidRationStateException;
import br.com.dogvision.dogfeeding.model.FeedingPlan;
import br.com.dogvision.dogfeeding.model.Ration;
import br.com.dogvision.dogfeeding.repository.FeedingPlanRepository;
import br.com.dogvision.dogfeeding.repository.RationRepository;
import br.com.dogvision.dogfeeding.service.FeedingPlanService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FeedingPlanServiceImp implements FeedingPlanService {

    private final FeedingPlanRepository repository;
    private final RationRepository rationRepository;

    @Override
    public List<FeedingPlanResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public FeedingPlanResponse findById(UUID id) {
        return toResponse(findEntity(id));
    }

    @Override
    public List<FeedingPlanResponse> findByDogId(UUID dogId) {
        return repository.findAllByDogId(dogId).stream().map(this::toResponse).toList();
    }

    @Override
    public FeedingPlanResponse save(CreateFeedingPlanRequest dto, UUID loggedUserId) {
        validatePlanDates(dto.startDate(), dto.endDate());
        Ration ration = findRation(dto.rationId());
        FeedingPlan entity = new FeedingPlan();
        entity.setDogId(dto.dogId());
        entity.setRationId(ration.getId());
        entity.setName(dto.name());
        entity.setGoal(dto.goal());
        entity.setDailyQuantity(dto.dailyQuantity());
        entity.setUnit(dto.unit());
        entity.setMealTypes(dto.mealTypes());
        entity.setNotes(dto.notes());
        entity.setStartDate(dto.startDate());
        entity.setEndDate(dto.endDate());
        entity.setActive(true);
        return toResponse(repository.save(entity));
    }

    @Override
    public FeedingPlanResponse update(UUID id, UpdateFeedingPlanRequest dto, UUID loggedUserId) {
        FeedingPlan entity = findEntity(id);
        if (dto.rationId() != null) {
            entity.setRationId(findRation(dto.rationId()).getId());
        }
        if (dto.name() != null) {
            entity.setName(dto.name());
        }
        if (dto.goal() != null) {
            entity.setGoal(dto.goal());
        }
        if (dto.dailyQuantity() != null) {
            entity.setDailyQuantity(dto.dailyQuantity());
        }
        if (dto.unit() != null) {
            entity.setUnit(dto.unit());
        }
        if (dto.mealTypes() != null) {
            entity.setMealTypes(dto.mealTypes());
        }
        if (dto.notes() != null) {
            entity.setNotes(dto.notes());
        }
        if (dto.startDate() != null) {
            entity.setStartDate(dto.startDate());
        }
        if (dto.endDate() != null) {
            entity.setEndDate(dto.endDate());
        }
        if (dto.active() != null) {
            entity.setActive(dto.active());
        }
        validatePlanDates(entity.getStartDate(), entity.getEndDate());
        return toResponse(repository.save(entity));
    }

    @Override
    public void delete(UUID id, UUID loggedUserId) {
        repository.delete(findEntity(id));
    }

    private FeedingPlan findEntity(UUID id) {
        return repository.findById(id).orElseThrow(() -> new FeedingPlanNotFoundException(id));
    }

    private Ration findRation(UUID rationId) {
        return rationRepository.findById(rationId)
                .orElseThrow(() -> new InvalidRationStateException("Ration " + rationId + " is not registered"));
    }

    private void validatePlanDates(java.time.LocalDate startDate, java.time.LocalDate endDate) {
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new InvalidRationStateException("Plan end date cannot be before start date");
        }
    }

    private FeedingPlanResponse toResponse(FeedingPlan entity) {
        Ration ration = findRation(entity.getRationId());
        return new FeedingPlanResponse(
                entity.getId(),
                entity.getDogId(),
                ration.getId(),
                ration.getName(),
                ration.getRationType(),
                entity.getName(),
                entity.getGoal(),
                entity.getDailyQuantity(),
                entity.getUnit(),
                entity.getMealTypes(),
                entity.getNotes(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
