package br.com.dogvision.doghealth.dto.mapper;

import br.com.dogvision.doghealth.dto.create.CreateConsultationRequest;
import br.com.dogvision.doghealth.dto.response.ConsultationResponse;
import br.com.dogvision.doghealth.dto.update.UpdateConsultationRequest;
import br.com.dogvision.doghealth.model.Consultation;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ConsultationMapper {

    Consultation toEntity(CreateConsultationRequest dto);

    ConsultationResponse toResponse(Consultation consultation);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UpdateConsultationRequest dto, @MappingTarget Consultation consultation);
}
