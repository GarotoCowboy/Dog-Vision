package br.com.dogvision.dogmanagement.dto.mapper;

import br.com.dogvision.dogmanagement.dto.CreateDogRequest;
import br.com.dogvision.dogmanagement.dto.DogResponse;
import br.com.dogvision.dogmanagement.dto.UpdateDogRequest;
import br.com.dogvision.dogmanagement.model.Dog;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DogMapper {

    DogResponse toResponse(Dog entity);

    Dog toEntity(CreateDogRequest dto);

    void updateFromDto(UpdateDogRequest dto, @MappingTarget Dog entity);
}
