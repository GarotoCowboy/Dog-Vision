package br.com.dogvision.dogmanagement.service.imp;

import br.com.dogvision.dogmanagement.dto.CreateDogRequest;
import br.com.dogvision.dogmanagement.dto.DogResponse;
import br.com.dogvision.dogmanagement.dto.UpdateDogRequest;
import br.com.dogvision.dogmanagement.dto.mapper.DogMapper;
import br.com.dogvision.dogmanagement.infra.exceptions.BusinessException;
import br.com.dogvision.dogmanagement.infra.exceptions.DogNotFoundException;
import br.com.dogvision.dogmanagement.model.Dog;
import br.com.dogvision.dogmanagement.model.enums.DogStatus;
import br.com.dogvision.dogmanagement.repository.DogRepository;
import br.com.dogvision.dogmanagement.service.DogService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        if (dto.status() == DogStatus.DOADO || dto.status() == DogStatus.FALECIDO) {
            if (Boolean.TRUE.equals(dto.onKennel())) {
                throw new BusinessException("Cães com status DOADO ou FALECIDO não podem estar no canil.", HttpStatus.BAD_REQUEST);
            }
        }

        Dog dog = mapper.toEntity(dto);

        if (dog.getStatus() == DogStatus.DOADO || dog.getStatus() == DogStatus.FALECIDO) {
            dog.setOnKennel(false);
        } else if (dog.getOnKennel() == null) {
            dog.setOnKennel(true);
        }

        Dog savedDog = dogRepository.save(dog);
        return mapper.toResponse(savedDog);
    }

    @Override
    @Transactional
    public DogResponse update(UUID id, UpdateDogRequest updateDogRequest) {
        Dog dog = findById(id);

        DogStatus targetStatus = updateDogRequest.status() != null ? updateDogRequest.status() : dog.getStatus();

        if (targetStatus == DogStatus.DOADO || targetStatus == DogStatus.FALECIDO) {
            if (Boolean.TRUE.equals(updateDogRequest.onKennel())) {
                throw new BusinessException("Cães com status DOADO ou FALECIDO não podem estar no canil.", HttpStatus.BAD_REQUEST);
            }
        }

        mapper.updateFromDto(updateDogRequest, dog);

        if (dog.getStatus() == DogStatus.DOADO || dog.getStatus() == DogStatus.FALECIDO) {
            dog.setOnKennel(false);
        }

        dogRepository.save(dog);
        return mapper.toResponse(dog);
    }

    @Override
    public void delete(UUID id) {

       Dog dog = dogRepository.findById(id)
               .orElseThrow(() -> new DogNotFoundException(id));

        dogRepository.delete(dog);
    }


    private Dog findById(UUID id){
        return dogRepository.findById(id).orElseThrow(() -> new DogNotFoundException(id));
    }
}
