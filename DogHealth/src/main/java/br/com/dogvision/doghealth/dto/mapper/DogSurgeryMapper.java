package br.com.dogvision.doghealth.dto.mapper;

import br.com.dogvision.doghealth.dto.create.CreateDogSurgeryRequest;
import br.com.dogvision.doghealth.dto.response.DogSurgeryResponse;
import br.com.dogvision.doghealth.dto.update.UpdateDogSurgeryRequest;
import br.com.dogvision.doghealth.model.DogSurgery;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface DogSurgeryMapper {
    DogSurgery toEntity(CreateDogSurgeryRequest dto);

    DogSurgeryResponse toResponse(DogSurgery surgery);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UpdateDogSurgeryRequest dto, @MappingTarget DogSurgery surgery);
}
