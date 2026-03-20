package br.com.dogvision.doghealth.dto.mapper;

import br.com.dogvision.doghealth.dto.create.CreateConsultationRequest;
import br.com.dogvision.doghealth.dto.create.CreateDogWeightRequest;
import br.com.dogvision.doghealth.dto.response.ConsultationResponse;
import br.com.dogvision.doghealth.dto.response.DogWeightResponse;
import br.com.dogvision.doghealth.dto.update.UpdateConsultationRequest;
import br.com.dogvision.doghealth.dto.update.UpdateDogWeightRequest;
import br.com.dogvision.doghealth.model.Consultation;
import br.com.dogvision.doghealth.model.DogWeight;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface DogWeightMapper {

    DogWeight toEntity(CreateDogWeightRequest dto);

    DogWeightResponse toResponse(DogWeight dogWeight);

    void updateFromDto(UpdateDogWeightRequest dto, @MappingTarget DogWeight dogWeight);

}
