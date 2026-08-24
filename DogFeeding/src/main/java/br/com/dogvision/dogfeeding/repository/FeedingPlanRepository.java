package br.com.dogvision.dogfeeding.repository;

import br.com.dogvision.dogfeeding.model.FeedingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface FeedingPlanRepository extends JpaRepository<FeedingPlan, UUID> {

    List<FeedingPlan> findAllByDogId(UUID dogId);

    @Query("""
            select fp from FeedingPlan fp
            where fp.rationId = :rationId
              and fp.active = true
              and fp.startDate <= :referenceDate
              and (fp.endDate is null or fp.endDate >= :referenceDate)
            """)
    List<FeedingPlan> findActivePlansByRationId(@Param("rationId") UUID rationId,
                                                @Param("referenceDate") LocalDate referenceDate);

    @Query("""
            select fp from FeedingPlan fp
            where fp.active = true
              and fp.startDate <= :referenceDate
              and (fp.endDate is null or fp.endDate >= :referenceDate)
            """)
    List<FeedingPlan> findAllActivePlans(@Param("referenceDate") LocalDate referenceDate);
}
