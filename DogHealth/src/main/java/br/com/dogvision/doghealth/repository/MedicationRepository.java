package br.com.dogvision.doghealth.repository;

import br.com.dogvision.doghealth.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, UUID> {
    List<Medication> findAllByDogsNameIgnoreCase(String dogsName);
}

