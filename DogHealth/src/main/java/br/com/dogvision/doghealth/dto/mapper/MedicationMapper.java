package br.com.dogvision.doghealth.dto.mapper;

import br.com.dogvision.doghealth.dto.create.CreateMedicationRequest;
import br.com.dogvision.doghealth.dto.response.MedicationResponse;
import br.com.dogvision.doghealth.model.Medication;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicationMapper {

    Medication toEntity(CreateMedicationRequest dto);

    MedicationResponse toResponse(Medication medication);
}

