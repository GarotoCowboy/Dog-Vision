package br.com.dogvision.doghealth.service.imp;

import br.com.dogvision.doghealth.dto.create.CreateConsultationRequest;
import br.com.dogvision.doghealth.dto.mapper.ConsultationMapper;
import br.com.dogvision.doghealth.dto.response.ConsultationResponse;
import br.com.dogvision.doghealth.dto.update.UpdateConsultationRequest;
import br.com.dogvision.doghealth.infra.exception.ConsultationNotFoundException;
import br.com.dogvision.doghealth.infra.exception.ResourceNotFoundException;
import br.com.dogvision.doghealth.model.Consultation;
import br.com.dogvision.doghealth.repository.ConsultationRepository;
import br.com.dogvision.doghealth.service.ConsultationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ConsultationServiceImp implements ConsultationService {

    private final ConsultationRepository repository;
    private final ConsultationMapper mapper;

    @Override
    public ConsultationResponse getById(UUID id) {
        Consultation c = repository.findById(id).orElseThrow(() -> new ConsultationNotFoundException(id));

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
    public List<ConsultationResponse> getByMonth(UUID id, int month, int year) {

        LocalDateTime startsAt = LocalDateTime.of(year,month,1,0,0,0);
        LocalDateTime endsAt = YearMonth.of(year,month)
                .atEndOfMonth()
                .atTime(23,59,59);

        return repository.findAllByDogIdAndCreatedAtBetween(id,startsAt,endsAt)
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
                .orElseThrow(() -> new ConsultationNotFoundException(id));

        mapper.updateFromDto(dto, consultation);

        repository.save(consultation);
        return mapper.toResponse(consultation);
    }

    @Override
    public void delete(UUID id) {
        Consultation consultation = repository.findById(id)
                .orElseThrow(() -> new ConsultationNotFoundException(id));

        repository.delete(consultation);
    }
}
