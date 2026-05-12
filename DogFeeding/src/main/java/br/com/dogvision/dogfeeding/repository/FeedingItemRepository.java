package br.com.dogvision.dogfeeding.repository;

import br.com.dogvision.dogfeeding.model.FeedingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface FeedingItemRepository extends JpaRepository<FeedingItem, UUID> {

    boolean existsByRationId(UUID rationId);

    @Query("""
            select fi from FeedingItem fi
            join fetch fi.ration r
            join fi.feeding f
            where (:dogId is null or f.dogId = :dogId)
              and (:rationType is null or r.rationType = :rationType)
              and (:startDate is null or f.date >= :startDate)
              and (:endDate is null or f.date <= :endDate)
            """)
    List<FeedingItem> findForConsumptionReport(UUID dogId,
                                               br.com.dogvision.dogfeeding.model.RationType rationType,
                                               LocalDate startDate,
                                               LocalDate endDate);
}
