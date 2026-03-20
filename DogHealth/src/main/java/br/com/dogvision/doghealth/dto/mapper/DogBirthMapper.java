package br.com.dogvision.doghealth.dto.mapper;

import br.com.dogvision.doghealth.dto.create.CreateDogBirthRequest;
import br.com.dogvision.doghealth.dto.response.DogBirthResponse;
import br.com.dogvision.doghealth.dto.update.UpdateDogBirthRequest;
import br.com.dogvision.doghealth.model.DogBirth;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface DogBirthMapper {

    DogBirth toEntity(CreateDogBirthRequest dto);

    DogBirthResponse toResponse(DogBirth dogBirth);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UpdateDogBirthRequest dto, @MappingTarget DogBirth dogBirth);

}
