package br.com.dogvision.doghealth.repository;

import br.com.dogvision.doghealth.model.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, UUID> {

}
