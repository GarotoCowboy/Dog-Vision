package br.com.dogvision.dogtraining.controller;

import br.com.dogvision.dogtraining.dto.create.CreateStage3Request;
import br.com.dogvision.dogtraining.dto.response.Stage3Response;
import br.com.dogvision.dogtraining.dto.update.UpdateStage3Request;
import br.com.dogvision.dogtraining.model.enums.TraningStatus;
import br.com.dogvision.dogtraining.service.Stage3Service;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dogtraining/stage3")
@AllArgsConstructor
public class Stage3Controller {

    public final Stage3Service service;

    @PostMapping
    @Transactional
    public ResponseEntity<Stage3Response> save(@RequestBody CreateStage3Request dto) {
        Stage3Response response = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stage3Response> get(@PathVariable UUID id) {
        Stage3Response response = service.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/training/{trainingId}")
    public ResponseEntity<List<Stage3Response>> listByTrainingId(@PathVariable UUID trainingId) {
        List<Stage3Response> responses = service.findAllByTrainingId(trainingId);
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/update/{id}")
    @Transactional
    public ResponseEntity<Stage3Response> update(@PathVariable UUID id,
                                                 @RequestBody UpdateStage3Request dto) {
        Stage3Response response = service.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/finalize/{id}")
    @Transactional
    public ResponseEntity<Stage3Response> finalizeStage(@PathVariable UUID id,
                                                        @RequestParam TraningStatus status) {
        Stage3Response response = service.finalizeStage(id, status);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}