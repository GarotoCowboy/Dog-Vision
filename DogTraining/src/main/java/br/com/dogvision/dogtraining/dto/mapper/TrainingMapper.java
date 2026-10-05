package br.com.dogvision.dogtraining.dto.mapper;

import br.com.dogvision.dogtraining.dto.create.CreateTrainingRequest;
import br.com.dogvision.dogtraining.dto.response.TrainingResponse;
import br.com.dogvision.dogtraining.dto.update.UpdateTrainingRequest;
import br.com.dogvision.dogtraining.model.Training;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TrainingMapper {

    Training toEntity(CreateTrainingRequest dto);

    TrainingResponse toResponse(Training training);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UpdateTrainingRequest dto, @MappingTarget Training training);
}