package br.com.dogvision.user.controller;

import br.com.dogvision.user.dto.create.CreateCoordinatorRequest;
import br.com.dogvision.user.dto.response.CoordinatorResponse;
import br.com.dogvision.user.service.CoordinatorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employees/coordinators")
public class CoordinatorController {

    private final CoordinatorService service;

    public CoordinatorController(CoordinatorService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public CoordinatorResponse getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @GetMapping("/registration/{registration}")
    public CoordinatorResponse getByRegistration(@PathVariable String registration) {
        return service.getByRegistration(registration);
    }

    @GetMapping
    public List<CoordinatorResponse> getAll() {
        return service.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CoordinatorResponse save(@RequestBody @Valid CreateCoordinatorRequest dto) {
        return service.save(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
