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



    @Mapping(target = "employeeId", source = "id")
    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "registration", source = "user.registration")

    CoordinatorResponse toResponse(Coordinator coordinator);


    @Mapping(target = "user.registration", source = "registration")
    @Mapping(target = "user.passwordHash", source = "password")
    @Mapping(target = "type", constant = "COORDINATOR")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Coordinator toEntity(CreateCoordinatorRequest request);
}
