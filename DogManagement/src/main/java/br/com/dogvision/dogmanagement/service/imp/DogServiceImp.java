package br.com.dogvision.dogmanagement.service.imp;

import br.com.dogvision.dogmanagement.dto.CreateDogRequest;
import br.com.dogvision.dogmanagement.dto.DogResponse;
import br.com.dogvision.dogmanagement.dto.UpdateDogRequest;
import br.com.dogvision.dogmanagement.dto.mapper.DogMapper;
import br.com.dogvision.dogmanagement.infra.exceptions.DogNotFoundException;
import br.com.dogvision.dogmanagement.model.Dog;
import br.com.dogvision.dogmanagement.repository.DogRepository;
import br.com.dogvision.dogmanagement.service.DogService;
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
public class DogServiceImp implements DogService {


    private final DogRepository dogRepository;
    private final DogMapper mapper;


    @Override
    public DogResponse getById(UUID id) {
        Dog dog = dogRepository.findById(id).orElseThrow(() -> new DogNotFoundException(id));

        return mapper.toResponse(dog);
    }

    @Override
    public List<DogResponse> getAll() {

        return dogRepository.findAll()
                .stream()
                .map(mapper::toResponse).toList();
    }

    @Override
    public DogResponse save(CreateDogRequest dto) {

        Dog dog = mapper.toEntity(dto);

        Dog savedDog =dogRepository.save(dog);

        return mapper.toResponse(savedDog);
    }

    @Override
    @Transactional
    public DogResponse update(UUID id, UpdateDogRequest updateDogRequest) {

        Dog dog = findById(id);


    mapper.updateFromDto(updateDogRequest,dog);
    dogRepository.save(dog);

    return mapper.toResponse(dog);
    }

    @Override
    public void delete(UUID id) {

       Dog dog = findById(id);

        dogRepository.delete(dog);
    }


    private Dog findById(UUID id){
        return dogRepository.findById(id).orElseThrow(() -> new DogNotFoundException(id));
    }
}
