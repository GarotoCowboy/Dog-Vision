package br.com.dogvision.user.controller;

import br.com.dogvision.user.dto.create.CreateTrainerRequest;
import br.com.dogvision.user.dto.response.CoordinatorResponse;
import br.com.dogvision.user.dto.response.TrainerResponse;
import br.com.dogvision.user.service.TrainerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employees/trainers")
public class TrainerController {

    private final TrainerService service;

    public TrainerController(TrainerService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public TrainerResponse getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @GetMapping("/registration/{registration}")
    public TrainerResponse getByRegistration(@PathVariable String registration) {
        return service.getByRegistration(registration);
    }

    @GetMapping
    public List<TrainerResponse> getAll() {
        return service.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TrainerResponse save(@RequestBody @Valid CreateTrainerRequest dto) {
        return service.save(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
