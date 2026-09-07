package br.com.dogvision.dogtraining.controller;

import br.com.dogvision.dogtraining.dto.create.CreateTrainingRequest;
import br.com.dogvision.dogtraining.dto.response.TrainingResponse;
import br.com.dogvision.dogtraining.dto.update.UpdateTrainingRequest;
import br.com.dogvision.dogtraining.infra.security.TokenService;
import br.com.dogvision.dogtraining.service.TrainingService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dogtraining/training")
@AllArgsConstructor
public class TrainingController {

    public final TokenService tokenService;
    public final TrainingService service;

    @PostMapping
    @Transactional
    public ResponseEntity<TrainingResponse> save(@RequestBody CreateTrainingRequest dto) {
        TrainingResponse response = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingResponse> get(@PathVariable UUID id) {
        TrainingResponse response = service.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dog/{dogId}")
    public ResponseEntity<List<TrainingResponse>> listByDogId(@PathVariable UUID dogId) {
        List<TrainingResponse> responses = service.findAllByDogId(dogId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/trainer/{trainerId}")
    public ResponseEntity<List<TrainingResponse>> listByTrainerId(@PathVariable UUID trainerId) {
        List<TrainingResponse> responses = service.findAllByTrainerId(trainerId);
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/update/{id}")
    @Transactional
    public ResponseEntity<TrainingResponse> update(@PathVariable UUID id,
                                                   @RequestBody UpdateTrainingRequest dto) {
        TrainingResponse response = service.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}