package br.com.dogvision.doghealth.service.imp;

import br.com.dogvision.doghealth.dto.create.CreateConsultationRequest;
import br.com.dogvision.doghealth.dto.mapper.ConsultationMapper;
import br.com.dogvision.doghealth.dto.response.ConsultationResponse;
import br.com.dogvision.doghealth.dto.update.UpdateConsultationRequest;
import br.com.dogvision.doghealth.infra.exception.ResourceNotFoundException;
import br.com.dogvision.doghealth.model.Consultation;
import br.com.dogvision.doghealth.repository.ConsultationRepository;
import br.com.dogvision.doghealth.service.ConsultationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ConsultationServiceImp implements ConsultationService {

    private final ConsultationRepository repository;
    private final ConsultationMapper mapper;

    @Override
    public ConsultationResponse getById(UUID id) {
        Consultation c = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Consultation", id));

        return mapper.toResponse(c);
    }

    @Override
    public List<ConsultationResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ConsultationResponse save(CreateConsultationRequest dto, UUID veterinarianId) {
        Consultation consultation = mapper.toEntity(dto);
        consultation.setVeterinarianId(veterinarianId);

        Consultation savedConsultation = repository.save(consultation);
        return mapper.toResponse(savedConsultation);
    }

    @Override
    public ConsultationResponse update(UUID id, UpdateConsultationRequest dto) {
        Consultation consultation = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("consultation", id));

        mapper.updateFromDto(dto, consultation);

        repository.save(consultation);
        return mapper.toResponse(consultation);
    }

    @Override
    public void delete(UUID id) {
        Consultation consultation = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consulta não encontrada"));

        repository.delete(consultation);
    }
}
