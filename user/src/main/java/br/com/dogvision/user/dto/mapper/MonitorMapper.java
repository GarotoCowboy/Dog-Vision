package br.com.dogvision.user.dto.mapper;

import br.com.dogvision.user.dto.response.MonitorResponse;
import br.com.dogvision.user.model.Monitor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MonitorMapper {

    @Mapping(target = "employeeId", source = "id")
    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "registration", source = "user.registration")
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "cpf", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "type", ignore = true)

    @Mapping(target = "shift", source = "shift")
    MonitorResponse toResponse(Monitor monitor);
}
