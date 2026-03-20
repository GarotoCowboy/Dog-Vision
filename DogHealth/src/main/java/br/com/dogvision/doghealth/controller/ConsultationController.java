package br.com.dogvision.doghealth.controller;

import br.com.dogvision.doghealth.dto.create.CreateConsultationRequest;
import br.com.dogvision.doghealth.dto.response.ConsultationResponse;
import br.com.dogvision.doghealth.dto.update.UpdateConsultationRequest;
import br.com.dogvision.doghealth.infra.security.TokenService;
import br.com.dogvision.doghealth.model.Consultation;
import br.com.dogvision.doghealth.service.ConsultationService;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/doghealth/consultation")
@AllArgsConstructor
public class ConsultationController {

    public final TokenService tokenService;
    public final ConsultationService service;

    @PostMapping
    @Transactional
    public ResponseEntity<ConsultationResponse> save(@RequestBody CreateConsultationRequest dto,
                                             @RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ","");
        UUID veterinarianId = UUID.fromString(tokenService.getIdFromToken(token));
        ConsultationResponse response = service.save(dto, veterinarianId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultationResponse> get(@PathVariable UUID id){
    ConsultationResponse response = service.getById(id);
    return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ConsultationResponse>> list(){
        List<ConsultationResponse> responses = service.getAll();
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/update/{id}")
    @Transactional
    public ResponseEntity<ConsultationResponse> update(@PathVariable UUID id,
                                                       @RequestBody UpdateConsultationRequest dto){
        ConsultationResponse consultationResponse = service.update(id,dto);
        return ResponseEntity.ok(consultationResponse);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}
