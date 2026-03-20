package br.com.dogvision.doghealth.controller;

import br.com.dogvision.doghealth.dto.create.CreateDogWeightRequest;
import br.com.dogvision.doghealth.dto.response.DogWeightResponse;
import br.com.dogvision.doghealth.dto.update.UpdateDogWeightRequest;
import br.com.dogvision.doghealth.infra.security.TokenService;
import br.com.dogvision.doghealth.service.DogWeightService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/doghealth/weight")
@AllArgsConstructor
public class DogWeightController {
    public final DogWeightService service;
    public final TokenService tokenService;



    @PostMapping
    @Transactional
    public ResponseEntity<DogWeightResponse> save(@RequestBody CreateDogWeightRequest dto,
                                                  @RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ","");
        UUID monitorId = UUID.fromString(tokenService.getIdFromToken(token));
        DogWeightResponse response = service.save(dto, monitorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<DogWeightResponse>> listWeightByDogId(@PathVariable UUID id){
        List<DogWeightResponse> response = service.listByDogId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/month")
    public ResponseEntity<List<DogWeightResponse>> listWeightByMonth(
            @PathVariable UUID id,
            @RequestParam int month,
            @RequestParam int year
    ){
        List<DogWeightResponse> responses = service.getByMonth(id,month,year);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}/week")
    public ResponseEntity<List<DogWeightResponse>> listWeightByWeek(
            @PathVariable UUID id,
            @RequestParam LocalDate date
    ){
        List<DogWeightResponse> responses = service.getByWeek(id,date);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}/lastWeight")
    public ResponseEntity<Optional<DogWeightResponse>> getLastWeightById(@PathVariable UUID id){
        Optional<DogWeightResponse>response = service.getLastWeight(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/update/{id}")
    @Transactional
    public ResponseEntity<DogWeightResponse> update(@PathVariable UUID id,
                                                   @RequestBody UpdateDogWeightRequest dto){
        DogWeightResponse response = service.update(id,dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

}
