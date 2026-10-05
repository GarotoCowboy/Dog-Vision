package br.com.dogvision.dogtraining.dto.mapper;

import br.com.dogvision.dogtraining.dto.create.CreateStage1Request;
import br.com.dogvision.dogtraining.dto.response.Stage1Response;
import br.com.dogvision.dogtraining.dto.update.UpdateStage1Request;
import br.com.dogvision.dogtraining.model.Stage1;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface Stage1Mapper {

    Stage1 toEntity(CreateStage1Request dto);

    Stage1Response toResponse(Stage1 stage1);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UpdateStage1Request dto, @MappingTarget Stage1 stage1);
}