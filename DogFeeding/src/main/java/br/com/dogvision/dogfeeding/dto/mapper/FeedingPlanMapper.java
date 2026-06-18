package br.com.dogvision.dogfeeding.dto.mapper;

import br.com.dogvision.dogfeeding.dto.create.CreateFeedingPlanRequest;
import br.com.dogvision.dogfeeding.dto.update.UpdateFeedingPlanRequest;
import br.com.dogvision.dogfeeding.model.FeedingPlan;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface FeedingPlanMapper {

    FeedingPlan toEntity(CreateFeedingPlanRequest dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UpdateFeedingPlanRequest dto, @MappingTarget FeedingPlan entity);
}
