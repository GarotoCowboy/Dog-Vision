package br.com.dogvision.user.dto.mapper;

import br.com.dogvision.user.dto.create.CreateUserRequest;
import br.com.dogvision.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {


    User toEntity(CreateUserRequest dto);

}
