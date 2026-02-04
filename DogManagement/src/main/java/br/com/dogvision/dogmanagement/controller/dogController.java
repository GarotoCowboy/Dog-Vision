package br.com.dogvision.dogmanagement.controller;

import br.com.dogvision.dogmanagement.dto.CreateDogRequest;
import br.com.dogvision.dogmanagement.dto.DogResponse;
import br.com.dogvision.dogmanagement.dto.UpdateDogRequest;
import br.com.dogvision.dogmanagement.model.Dog;
import br.com.dogvision.dogmanagement.service.DogServiceImp;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/dogs")
public class dogController {

    private final DogServiceImp dogService;


    @GetMapping("/{id}")
    public ResponseEntity<DogResponse> getById(@PathVariable UUID id){

    DogResponse dog = dogService.getById(id).orElseThrow(() -> new RuntimeException("Dog cannot found with ID: " + id));
    return ResponseEntity.ok(dog);
    }

    @GetMapping()
    public ResponseEntity<List<DogResponse>> list(){
        List<DogResponse> dogs = dogService.getAll();
        return ResponseEntity.ok(dogs);
    }


    @PostMapping
    public ResponseEntity<Dog> create(@RequestBody @Valid CreateDogRequest createDogRequest){
        Dog savedDog = dogService.save(createDogRequest.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDog);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Dog> delete(@PathVariable UUID id){

        dogService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Dog> update(@RequestBody @Valid UpdateDogRequest updateDogRequest) {
        Dog updatedDog = dogService.update(updateDogRequest.ID(), updateDogRequest);

        return ResponseEntity.ok(updatedDog);
    }

}
