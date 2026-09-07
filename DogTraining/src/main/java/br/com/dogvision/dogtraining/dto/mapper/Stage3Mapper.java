package br.com.dogvision.dogtraining.dto.mapper;

import br.com.dogvision.dogtraining.dto.create.CreateStage3Request;
import br.com.dogvision.dogtraining.dto.response.Stage3Response;
import br.com.dogvision.dogtraining.dto.update.UpdateStage3Request;
import br.com.dogvision.dogtraining.model.Stage3;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface Stage3Mapper {

    Stage3 toEntity(CreateStage3Request dto);

    Stage3Response toResponse(Stage3 stage3);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UpdateStage3Request dto, @MappingTarget Stage3 stage3);
}