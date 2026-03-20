package br.com.dogvision.user.dto.mapper;

import br.com.dogvision.user.dto.response.CoordinatorResponse;
import br.com.dogvision.user.model.Coordinator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CoordinatorMapper {

    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "userId", source = "employee.user.userId")
    @Mapping(target = "registration", source = "employee.user.registration")

    @Mapping(target = "email", source = "employee.email")
    @Mapping(target = "name", source = "employee.name")
    @Mapping(target = "cpf", source = "employee.cpf")
    @Mapping(target = "phone", source = "employee.phone")
    @Mapping(target = "type", source = "employee.type")

    @Mapping(target = "codAdmin", source = "codAdmin")
    CoordinatorResponse toResponse(Coordinator coordinator);
}
