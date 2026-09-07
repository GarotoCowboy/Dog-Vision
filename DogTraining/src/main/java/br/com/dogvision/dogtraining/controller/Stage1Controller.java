package br.com.dogvision.dogtraining.controller;

import br.com.dogvision.dogtraining.dto.create.CreateStage1Request;
import br.com.dogvision.dogtraining.dto.response.Stage1Response;
import br.com.dogvision.dogtraining.dto.update.UpdateStage1Request;
import br.com.dogvision.dogtraining.model.enums.TraningStatus;
import br.com.dogvision.dogtraining.service.Stage1Service;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dogtraining/stage1")
@AllArgsConstructor
public class Stage1Controller {

    public final Stage1Service service;

    @PostMapping
    @Transactional
    public ResponseEntity<Stage1Response> save(@RequestBody CreateStage1Request dto) {
        Stage1Response response = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stage1Response> get(@PathVariable UUID id) {
        Stage1Response response = service.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/training/{trainingId}")
    public ResponseEntity<List<Stage1Response>> listByTrainingId(@PathVariable UUID trainingId) {
        List<Stage1Response> responses = service.findAllByTrainingId(trainingId);
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/update/{id}")
    @Transactional
    public ResponseEntity<Stage1Response> update(@PathVariable UUID id,
                                                 @RequestBody UpdateStage1Request dto) {
        Stage1Response response = service.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/finalize/{id}")
    @Transactional
    public ResponseEntity<Stage1Response> finalizeStage(@PathVariable UUID id,
                                                        @RequestParam TraningStatus status) {
        Stage1Response response = service.finalizeStage(id, status);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}