package br.com.dogvision.dogfeeding.service;

import br.com.dogvision.dogfeeding.dto.create.CreateFeedingRequest;
import br.com.dogvision.dogfeeding.dto.response.ConsumptionReportResponse;
import br.com.dogvision.dogfeeding.dto.response.FeedingResponse;
import br.com.dogvision.dogfeeding.dto.update.UpdateFeedingRequest;
import br.com.dogvision.dogfeeding.model.MealType;
import br.com.dogvision.dogfeeding.model.RationType;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface FeedingService {

    List<FeedingResponse> findAll();

    FeedingResponse findById(UUID id);

    List<FeedingResponse> findByDogId(UUID dogId);

    List<FeedingResponse> search(UUID dogId, LocalDate startDate, LocalDate endDate, MealType mealType, RationType rationType);

    List<ConsumptionReportResponse> consumptionReport(UUID dogId, LocalDate startDate, LocalDate endDate, RationType rationType);

    FeedingResponse save(CreateFeedingRequest dto, UUID loggedUserId);

    FeedingResponse update(UUID id, UpdateFeedingRequest dto, UUID loggedUserId);

    void delete(UUID id, UUID loggedUserId);
}
