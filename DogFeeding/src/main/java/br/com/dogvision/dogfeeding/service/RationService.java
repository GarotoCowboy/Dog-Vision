package br.com.dogvision.dogfeeding.service;

import br.com.dogvision.dogfeeding.dto.create.CreateRationRequest;
import br.com.dogvision.dogfeeding.dto.response.RationAlertResponse;
import br.com.dogvision.dogfeeding.dto.response.RationConsumptionEstimateResponse;
import br.com.dogvision.dogfeeding.dto.response.RationResponse;
import br.com.dogvision.dogfeeding.dto.update.DecreaseRationStockRequest;
import br.com.dogvision.dogfeeding.dto.update.IncreaseRationStockRequest;
import br.com.dogvision.dogfeeding.dto.update.UpdateRationRequest;
import br.com.dogvision.dogfeeding.model.RationStockStatus;
import br.com.dogvision.dogfeeding.model.RationType;

import java.util.List;
import java.util.UUID;

public interface RationService {

    List<RationResponse> findAll();

    RationResponse findById(UUID id);

    List<RationResponse> search(RationType rationType, RationStockStatus stockStatus);

    List<RationAlertResponse> alerts();

    RationConsumptionEstimateResponse getEstimate(UUID id);

    List<RationConsumptionEstimateResponse> getAllEstimates();

    RationResponse save(CreateRationRequest dto, UUID loggedUserId);

    RationResponse update(UUID id, UpdateRationRequest dto, UUID loggedUserId);

    RationResponse increaseRation(UUID id, IncreaseRationStockRequest dto, UUID loggedUserId);

    RationResponse decreaseRation(UUID id, DecreaseRationStockRequest dto, UUID loggedUserId);

    void delete(UUID id, UUID loggedUserId);
}
