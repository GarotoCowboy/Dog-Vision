package br.com.dogvision.dogmanagement.repository;

import br.com.dogvision.dogmanagement.model.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DogRepository extends JpaRepository<Dog,UUID> {

}
