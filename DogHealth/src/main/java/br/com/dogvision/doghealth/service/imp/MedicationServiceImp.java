package br.com.dogvision.doghealth.service.imp;

import br.com.dogvision.doghealth.dto.create.CreateMedicationRequest;
import br.com.dogvision.doghealth.dto.mapper.MedicationMapper;
import br.com.dogvision.doghealth.dto.response.MedicationResponse;
import br.com.dogvision.doghealth.infra.exception.MedicationNotFoundException;
import br.com.dogvision.doghealth.model.Medication;
import br.com.dogvision.doghealth.repository.MedicationRepository;
import br.com.dogvision.doghealth.service.MedicationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MedicationServiceImp implements MedicationService {

    private final MedicationRepository repository;
    private final MedicationMapper mapper;

    @Override
    public MedicationResponse save(CreateMedicationRequest dto) {
        Medication medication = mapper.toEntity(dto);
        Medication savedMedication = repository.save(medication);
        return mapper.toResponse(savedMedication);
    }

    @Override
    public List<MedicationResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public MedicationResponse getById(UUID id) {
        Medication medication = repository.findById(id)
                .orElseThrow(() -> new MedicationNotFoundException(id));
        return mapper.toResponse(medication);
    }

    @Override
    public void delete(UUID id) {
        Medication medication = repository.findById(id)
                .orElseThrow(() -> new MedicationNotFoundException(id));
        repository.delete(medication);
    }
}

