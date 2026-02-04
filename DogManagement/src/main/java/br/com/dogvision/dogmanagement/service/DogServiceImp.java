package br.com.dogvision.dogmanagement.service;

import br.com.dogvision.dogmanagement.dto.DogResponse;
import br.com.dogvision.dogmanagement.dto.UpdateDogRequest;
import br.com.dogvision.dogmanagement.model.Dog;
import br.com.dogvision.dogmanagement.repository.DogRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DogServiceImp implements DogService{


    private final DogRepository dogRepository;


    @Override
    public Optional<DogResponse> getById(UUID id) {

        return dogRepository.findById(id).map(dog -> new DogResponse(dog));
    }

    @Override
    public List<DogResponse> getAll() {
        return dogRepository.findAll().stream().map(dog -> new DogResponse(dog)).toList();
    }

    @Override
    public Dog save(Dog dog) {
       // dog.setCreatedAt(Timestamp.from(Instant.now()));
        return dogRepository.save(dog);
    }

    @Override
    @Transactional
    public Dog update(UUID id, UpdateDogRequest updateDogRequest) {
        Dog existingDog = dogRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Dog cannot be found with ID: "+id));

    updateDogRequest.updateEntity(existingDog);

    return dogRepository.save(existingDog);
    }

    @Override
    public void delete(UUID id) {

        if(!dogRepository.existsById(id)){
            throw new RuntimeException("dog not exists");
        }
        dogRepository.deleteById(id);
    }
}
