package br.com.dogvision.user.dto.mapper;

import br.com.dogvision.user.dto.create.CreateCoordinatorRequest;
import br.com.dogvision.user.dto.response.CoordinatorResponse;
import br.com.dogvision.user.model.Coordinator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = EmployeeMapper.class)
public interface CoordinatorMapper {

//    @Mapping(target = "employeeId", source = "employee.id")
//    @Mapping(target = "userId", source = "employee.user.userId")
//    @Mapping(target = "registration", source = "employee.user.registration")
//
//    @Mapping(target = "email", source = "employee.email")
//    @Mapping(target = "name", source = "employee.name")
//    @Mapping(target = "cpf", source = "employee.cpf")
//    @Mapping(target = "phone", source = "employee.phone")
//    @Mapping(target = "type", source = "employee.type")
//
//    @Mapping(target = "codAdmin", source = "codAdmin")
//    CoordinatorResponse toResponse(Coordinator coordinator);

    @Mapping(target = "employeeId", source = "id")
    @Mapping(target = "userId", source = "user.userId") // Mapeando da classe User
    @Mapping(target = "registration", source = "user.registration")
        // Name, Email, CPF, Phone, Type e CodAdmin mapeiam automático (nomes iguais)
    CoordinatorResponse toResponse(Coordinator coordinator);

    // 2. REQUEST (DTO) -> ENTIDADE
    @Mapping(target = "user.registration", source = "registration")
    @Mapping(target = "user.passwordHash", source = "password") // Mapeia p/ passwordHash
    @Mapping(target = "type", constant = "COORDINATOR") // Garante o tipo correto
    @Mapping(target = "id", ignore = true) // ID é gerado pelo banco
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Coordinator toEntity(CreateCoordinatorRequest request);
}
