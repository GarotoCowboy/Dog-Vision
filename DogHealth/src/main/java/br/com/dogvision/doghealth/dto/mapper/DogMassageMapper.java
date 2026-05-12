package br.com.dogvision.doghealth.dto.mapper;

import br.com.dogvision.doghealth.dto.create.CreateDogBirthRequest;
import br.com.dogvision.doghealth.dto.create.CreateDogMassageRequest;
import br.com.dogvision.doghealth.dto.response.DogBirthResponse;
import br.com.dogvision.doghealth.dto.response.DogMassageResponse;
import br.com.dogvision.doghealth.dto.update.UpdateDogBirthRequest;
import br.com.dogvision.doghealth.dto.update.UpdateDogMassageRequest;
import br.com.dogvision.doghealth.model.DogBirth;
import br.com.dogvision.doghealth.model.DogMassage;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface DogMassageMapper {

    DogMassage toEntity(CreateDogMassageRequest dto);

    DogMassageResponse toResponse(DogMassage dogMassage);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UpdateDogMassageRequest dto, @MappingTarget DogMassage dogMassage);
}
