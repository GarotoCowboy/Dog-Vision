package br.com.dogvision.dogfeeding.repository;

import br.com.dogvision.dogfeeding.model.Ration;
import br.com.dogvision.dogfeeding.model.RationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RationRepository extends JpaRepository<Ration, UUID> {

    List<Ration> findAllByRationType(RationType rationType);
}
