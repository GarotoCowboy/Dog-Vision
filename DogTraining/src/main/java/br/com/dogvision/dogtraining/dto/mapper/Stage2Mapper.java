package br.com.dogvision.dogtraining.dto.mapper;

import br.com.dogvision.dogtraining.dto.create.CreateStage2Request;
import br.com.dogvision.dogtraining.dto.response.Stage2Response;
import br.com.dogvision.dogtraining.dto.update.UpdateStage2Request;
import br.com.dogvision.dogtraining.model.Stage2;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface Stage2Mapper {

    Stage2 toEntity(CreateStage2Request dto);

    Stage2Response toResponse(Stage2 stage2);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UpdateStage2Request dto, @MappingTarget Stage2 stage2);
}