package br.com.dogvision.user.dto.mapper;

import br.com.dogvision.user.dto.create.CreateEmployeeRequest;
import br.com.dogvision.user.dto.response.EmployeeResponse;
import br.com.dogvision.user.dto.update.UpdateEmployeeRequest;
import br.com.dogvision.user.model.Employee;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(source = "id", target = "employeeId")
    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "user.registration", target = "registration")
    EmployeeResponse toResponse(Employee entity);

    Employee toEntity(CreateEmployeeRequest dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UpdateEmployeeRequest dto, @MappingTarget Employee entity);
}
