package br.com.dogvision.dogfeeding.service;

import br.com.dogvision.dogfeeding.dto.create.CreateFeedingPlanRequest;
import br.com.dogvision.dogfeeding.dto.response.FeedingPlanResponse;
import br.com.dogvision.dogfeeding.dto.update.UpdateFeedingPlanRequest;

import java.util.List;
import java.util.UUID;

public interface FeedingPlanService {

    List<FeedingPlanResponse> findAll();

    FeedingPlanResponse findById(UUID id);

    List<FeedingPlanResponse> findByDogId(UUID dogId);

    FeedingPlanResponse save(CreateFeedingPlanRequest dto, UUID loggedUserId);

    FeedingPlanResponse update(UUID id, UpdateFeedingPlanRequest dto, UUID loggedUserId);

    void delete(UUID id, UUID loggedUserId);
}
