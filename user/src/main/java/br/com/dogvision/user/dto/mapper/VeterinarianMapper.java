package br.com.dogvision.user.dto.mapper;

import br.com.dogvision.user.dto.create.CreateVeterinarianRequest;
import br.com.dogvision.user.dto.response.VeterinarianResponse;
import br.com.dogvision.user.model.Veterinarian;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface VeterinarianMapper {

    @Mapping(target = "employeeId", source = "id")
    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "registration", source = "user.registration")
    VeterinarianResponse toResponse(Veterinarian vet);

    @Mapping(target = "user.registration", source = "registration")
    @Mapping(target = "user.passwordHash", source = "password")
    @Mapping(target = "type", constant = "VETERINARIAN")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Veterinarian toEntity(CreateVeterinarianRequest request);
}
