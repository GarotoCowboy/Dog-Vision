package br.com.dogvision.user.dto.mapper;

import br.com.dogvision.user.dto.response.VeterinarianResponse;
import br.com.dogvision.user.model.Veterinarian;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface VeterinarianMapper {

    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "userId", source = "employee.user.userId")
    @Mapping(target = "registration", source = "employee.user.registration")

    @Mapping(target = "email", source = "employee.email")
    @Mapping(target = "name", source = "employee.name")
    @Mapping(target = "cpf", source = "employee.cpf")
    @Mapping(target = "phone", source = "employee.phone")
    @Mapping(target = "type", source = "employee.type")

    @Mapping(target = "crmv", source = "crmv")
    @Mapping(target = "areaOfExpertise", source = "areaOfExpertise")
    VeterinarianResponse toResponse(Veterinarian vet);
}
