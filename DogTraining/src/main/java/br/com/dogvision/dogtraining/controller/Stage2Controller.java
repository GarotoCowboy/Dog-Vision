package br.com.dogvision.dogtraining.controller;

import br.com.dogvision.dogtraining.dto.create.CreateStage2Request;
import br.com.dogvision.dogtraining.dto.response.Stage2Response;
import br.com.dogvision.dogtraining.dto.update.UpdateStage2Request;
import br.com.dogvision.dogtraining.model.enums.TraningStatus;
import br.com.dogvision.dogtraining.service.Stage2Service;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dogtraining/stage2")
@AllArgsConstructor
public class Stage2Controller {

    public final Stage2Service service;

    @PostMapping
    @Transactional
    public ResponseEntity<Stage2Response> save(@RequestBody CreateStage2Request dto) {
        Stage2Response response = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stage2Response> get(@PathVariable UUID id) {
        Stage2Response response = service.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/training/{trainingId}")
    public ResponseEntity<List<Stage2Response>> listByTrainingId(@PathVariable UUID trainingId) {
        List<Stage2Response> responses = service.findAllByTrainingId(trainingId);
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/update/{id}")
    @Transactional
    public ResponseEntity<Stage2Response> update(@PathVariable UUID id,
                                                 @RequestBody UpdateStage2Request dto) {
        Stage2Response response = service.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/finalize/{id}")
    @Transactional
    public ResponseEntity<Stage2Response> finalizeStage(@PathVariable UUID id,
                                                        @RequestParam TraningStatus status) {
        Stage2Response response = service.finalizeStage(id, status);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}