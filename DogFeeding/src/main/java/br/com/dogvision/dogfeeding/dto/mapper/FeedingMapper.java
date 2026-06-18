package br.com.dogvision.dogfeeding.dto.mapper;

import br.com.dogvision.dogfeeding.dto.create.CreateFeedingRequest;
import br.com.dogvision.dogfeeding.dto.response.FeedingResponse;
import br.com.dogvision.dogfeeding.dto.update.UpdateFeedingRequest;
import br.com.dogvision.dogfeeding.model.Feeding;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface FeedingMapper {

    @Mapping(target = "items", ignore = true)
    @Mapping(target = "quantity", ignore = true)
    Feeding toEntity(CreateFeedingRequest dto);

    @Mapping(target = "totalQuantity", source = "quantity")
    FeedingResponse toResponse(Feeding feeding);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "quantity", ignore = true)
    void updateFromDto(UpdateFeedingRequest dto, @MappingTarget Feeding feeding);
}
