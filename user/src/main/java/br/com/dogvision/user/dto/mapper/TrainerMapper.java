package br.com.dogvision.user.dto.mapper;

import br.com.dogvision.user.dto.create.CreateTrainerRequest;
import br.com.dogvision.user.dto.response.TrainerResponse;
import br.com.dogvision.user.model.Trainer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface TrainerMapper {

  @Mapping(target = "employeeId", source = "id")
  @Mapping(target = "userId", source = "user.userId")
  @Mapping(target = "registration", source = "user.registration")
  TrainerResponse toResponse(Trainer trainer);

  @Mapping(target = "user.registration", source = "registration")
  @Mapping(target = "user.passwordHash", source = "password")
  @Mapping(target = "type", constant = "TRAINER")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Trainer toEntity(CreateTrainerRequest request);
}
