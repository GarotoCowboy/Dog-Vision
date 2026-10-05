package br.com.dogvision.dogtraining.dto.mapper;

import br.com.dogvision.dogtraining.dto.create.CreateStage4Request;
import br.com.dogvision.dogtraining.dto.response.Stage4Response;
import br.com.dogvision.dogtraining.dto.update.UpdateStage4Request;
import br.com.dogvision.dogtraining.model.Stage4;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface Stage4Mapper {

    Stage4 toEntity(CreateStage4Request dto);

    Stage4Response toResponse(Stage4 stage4);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UpdateStage4Request dto, @MappingTarget Stage4 stage4);
}