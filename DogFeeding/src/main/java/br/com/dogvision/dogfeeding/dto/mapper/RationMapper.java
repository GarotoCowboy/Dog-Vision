package br.com.dogvision.dogfeeding.dto.mapper;

import br.com.dogvision.dogfeeding.dto.create.CreateRationRequest;
import br.com.dogvision.dogfeeding.dto.update.UpdateRationRequest;
import br.com.dogvision.dogfeeding.model.Ration;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface RationMapper {

    Ration toEntity(CreateRationRequest dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UpdateRationRequest dto, @MappingTarget Ration ration);
}
