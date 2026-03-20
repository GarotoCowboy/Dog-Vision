package br.com.dogvision.user.controller;

import br.com.dogvision.user.dto.create.CreateMonitorRequest;
import br.com.dogvision.user.dto.response.CoordinatorResponse;
import br.com.dogvision.user.dto.response.MonitorResponse;
import br.com.dogvision.user.service.MonitorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employees/monitors")
public class MonitorController {

    private final MonitorService service;

    public MonitorController(MonitorService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public MonitorResponse getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @GetMapping("/registration/{registration}")
    public MonitorResponse getByRegistration(@PathVariable String registration) {
        return service.getByRegistration(registration);
    }

    @GetMapping
    public List<MonitorResponse> getAll() {
        return service.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MonitorResponse save(@RequestBody @Valid CreateMonitorRequest dto) {
        return service.save(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
