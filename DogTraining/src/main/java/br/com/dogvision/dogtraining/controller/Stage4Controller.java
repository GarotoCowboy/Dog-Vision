package br.com.dogvision.dogtraining.controller;

import br.com.dogvision.dogtraining.dto.create.CreateStage4Request;
import br.com.dogvision.dogtraining.dto.response.Stage4Response;
import br.com.dogvision.dogtraining.dto.update.UpdateStage4Request;
import br.com.dogvision.dogtraining.model.enums.TraningStatus;
import br.com.dogvision.dogtraining.service.Stage4Service;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dogtraining/stage4")
@AllArgsConstructor
public class Stage4Controller {

    public final Stage4Service service;

    @PostMapping
    @Transactional
    public ResponseEntity<Stage4Response> save(@RequestBody CreateStage4Request dto) {
        Stage4Response response = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stage4Response> get(@PathVariable UUID id) {
        Stage4Response response = service.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/training/{trainingId}")
    public ResponseEntity<List<Stage4Response>> listByTrainingId(@PathVariable UUID trainingId) {
        List<Stage4Response> responses = service.findAllByTrainingId(trainingId);
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/update/{id}")
    @Transactional
    public ResponseEntity<Stage4Response> update(@PathVariable UUID id,
                                                 @RequestBody UpdateStage4Request dto) {
        Stage4Response response = service.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/finalize/{id}")
    @Transactional
    public ResponseEntity<Stage4Response> finalizeStage(@PathVariable UUID id,
                                                        @RequestParam TraningStatus status) {
        Stage4Response response = service.finalizeStage(id, status);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}