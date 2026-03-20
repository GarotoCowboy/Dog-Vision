package br.com.dogvision.doghealth.repository;

import br.com.dogvision.doghealth.model.DogBirth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface DogBirthRepository extends JpaRepository<DogBirth, UUID> {
}
