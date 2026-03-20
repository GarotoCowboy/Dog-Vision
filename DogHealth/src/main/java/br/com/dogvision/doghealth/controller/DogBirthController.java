package br.com.dogvision.doghealth.controller;

import br.com.dogvision.doghealth.dto.create.CreateConsultationRequest;
import br.com.dogvision.doghealth.dto.create.CreateDogBirthRequest;
import br.com.dogvision.doghealth.dto.response.ConsultationResponse;
import br.com.dogvision.doghealth.dto.response.DogBirthResponse;
import br.com.dogvision.doghealth.dto.update.UpdateConsultationRequest;
import br.com.dogvision.doghealth.dto.update.UpdateDogBirthRequest;
import br.com.dogvision.doghealth.infra.security.TokenService;
import br.com.dogvision.doghealth.service.ConsultationService;
import br.com.dogvision.doghealth.service.DogsBirthService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/doghealth/birth")
@AllArgsConstructor
public class DogBirthController {

    public final TokenService tokenService;
    public final DogsBirthService service;

    @PostMapping
    @Transactional
    public ResponseEntity<DogBirthResponse> save(@RequestBody CreateDogBirthRequest dto,
                                                 @RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ","");
        UUID veterinarianId = UUID.fromString(tokenService.getIdFromToken(token));
        DogBirthResponse response = service.save(dto, veterinarianId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DogBirthResponse> get(@PathVariable UUID id){
        DogBirthResponse response = service.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<DogBirthResponse>> list(){
        List<DogBirthResponse> responses = service.getAll();
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/update/{id}")
    @Transactional
    public ResponseEntity<DogBirthResponse> update(@PathVariable UUID id,
                                                       @RequestBody UpdateDogBirthRequest dto){
        DogBirthResponse response = service.update(id,dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

}
