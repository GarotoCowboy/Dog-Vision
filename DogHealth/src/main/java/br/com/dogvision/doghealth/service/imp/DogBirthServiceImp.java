package br.com.dogvision.doghealth.service.imp;

import br.com.dogvision.doghealth.dto.create.CreateDogBirthRequest;
import br.com.dogvision.doghealth.dto.mapper.DogBirthMapper;
import br.com.dogvision.doghealth.dto.response.DogBirthResponse;
import br.com.dogvision.doghealth.dto.update.UpdateDogBirthRequest;
import br.com.dogvision.doghealth.infra.exception.BirthNotFoundException;
import br.com.dogvision.doghealth.infra.exception.ResourceNotFoundException;
import br.com.dogvision.doghealth.model.DogBirth;
import br.com.dogvision.doghealth.repository.DogBirthRepository;
import br.com.dogvision.doghealth.service.DogsBirthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DogBirthServiceImp implements DogsBirthService {

    private final DogBirthRepository repository;
    private final DogBirthMapper mapper;



    @Override
    public DogBirthResponse getById(UUID id) {
        DogBirth d = repository.findById(id).orElseThrow(() -> new BirthNotFoundException(id));

        return mapper.toResponse(d);
    }

    @Override
    public List<DogBirthResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public DogBirthResponse save(CreateDogBirthRequest dto, UUID veterinarianId) {
        DogBirth dogBirth = mapper.toEntity(dto);
        dogBirth.setVeterinarianId(veterinarianId);

        DogBirth savedDogBirth = repository.save(dogBirth);
        return mapper.toResponse(savedDogBirth);
    }

    @Override
    public DogBirthResponse update(UUID id, UpdateDogBirthRequest dto) {
        DogBirth dogBirth = repository.findById(id)
                .orElseThrow(() -> new BirthNotFoundException(id));

        mapper.updateFromDto(dto, dogBirth);

        repository.save(dogBirth);
        return mapper.toResponse(dogBirth);
    }

    @Override
    public void delete(UUID id) {
        DogBirth dogBirth = repository.findById(id)
                .orElseThrow(() -> new BirthNotFoundException(id));

        repository.delete(dogBirth);
    }
}
