package br.com.dogvision.dogfeeding.repository;

import br.com.dogvision.dogfeeding.model.FeedingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FeedingPlanRepository extends JpaRepository<FeedingPlan, UUID> {

    List<FeedingPlan> findAllByDogId(UUID dogId);
}
