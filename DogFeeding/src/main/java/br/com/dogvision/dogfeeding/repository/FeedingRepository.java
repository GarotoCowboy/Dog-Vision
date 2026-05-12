package br.com.dogvision.dogfeeding.repository;

import br.com.dogvision.dogfeeding.model.Feeding;
import br.com.dogvision.dogfeeding.model.MealType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FeedingRepository extends JpaRepository<Feeding, UUID> {

    @Override
    @EntityGraph(attributePaths = {"items", "items.ration"})
    List<Feeding> findAll();

    @Override
    @EntityGraph(attributePaths = {"items", "items.ration"})
    Optional<Feeding> findById(UUID id);

    @EntityGraph(attributePaths = {"items", "items.ration"})
    List<Feeding> findAllByDogId(UUID dogId);

    @EntityGraph(attributePaths = {"items", "items.ration"})
    List<Feeding> findAllByDogIdAndDateBetween(UUID dogId, LocalDate startDate, LocalDate endDate);

    @EntityGraph(attributePaths = {"items", "items.ration"})
    List<Feeding> findAllByDateBetween(LocalDate startDate, LocalDate endDate);

    @EntityGraph(attributePaths = {"items", "items.ration"})
    List<Feeding> findAllByMealType(MealType mealType);
}
