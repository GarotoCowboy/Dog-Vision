package br.com.dogvision.user.dto.mapper;

import br.com.dogvision.user.dto.response.CollaboratorResponse;
import br.com.dogvision.user.model.Collaborator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CollaboratorMapper {

    @Mapping(target = "collaboratorId", source = "id")
    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "registration", source = "user.registration")
    @Mapping(
            target = "email",
            expression = "java(collaborator instanceof br.com.dogvision.user.model.Employee employee ? employee.getEmail() : null)"
    )
    @Mapping(
            target = "phone",
            expression = "java(collaborator instanceof br.com.dogvision.user.model.Employee employee ? employee.getPhone() : null)"
    )
    @Mapping(
            target = "type",
            expression = "java(collaborator instanceof br.com.dogvision.user.model.Employee employee ? employee.getType() : null)"
    )
    @Mapping(target = "shift", source = "shift")
    CollaboratorResponse toResponse(Collaborator collaborator);
}
