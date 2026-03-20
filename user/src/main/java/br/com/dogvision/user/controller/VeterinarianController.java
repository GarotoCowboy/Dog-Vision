package br.com.dogvision.user.controller;

import br.com.dogvision.user.dto.create.CreateVeterinarianRequest;
import br.com.dogvision.user.dto.response.CoordinatorResponse;
import br.com.dogvision.user.dto.response.VeterinarianResponse;
import br.com.dogvision.user.service.VeterinarianService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employees/veterinarians")
public class VeterinarianController {

    private final VeterinarianService service;

    public VeterinarianController(VeterinarianService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public VeterinarianResponse getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @GetMapping("/registration/{registration}")
    public VeterinarianResponse getByRegistration(@PathVariable String registration) {
        return service.getByRegistration(registration);
    }

    @GetMapping
    public List<VeterinarianResponse> getAll() {
        return service.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VeterinarianResponse save(@RequestBody @Valid CreateVeterinarianRequest dto) {
        return service.save(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
