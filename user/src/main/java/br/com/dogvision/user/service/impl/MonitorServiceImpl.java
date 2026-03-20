package br.com.dogvision.user.service.impl;

import br.com.dogvision.user.dto.create.CreateMonitorRequest;
import br.com.dogvision.user.dto.mapper.MonitorMapper;
import br.com.dogvision.user.dto.response.MonitorResponse;
import br.com.dogvision.user.infra.exception.MonitorNotFoundException;
import br.com.dogvision.user.infra.exception.ResourceNotFoundException;
import br.com.dogvision.user.model.*;
import br.com.dogvision.user.repository.MonitorRepository;
import br.com.dogvision.user.repository.UserRepository;
import br.com.dogvision.user.service.EmployeeCreationService;
import br.com.dogvision.user.service.MonitorService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class MonitorServiceImpl implements MonitorService {

    private final MonitorRepository monitorRepository;
    private final UserRepository userRepository;
    private final EmployeeCreationService employeeCreationService;
    private final MonitorMapper monitorMapper;

    public MonitorServiceImpl(
            MonitorRepository monitorRepository,
            UserRepository userRepository,
            EmployeeCreationService employeeCreationService,
            MonitorMapper monitorMapper
    ) {
        this.monitorRepository = monitorRepository;
        this.userRepository = userRepository;
        this.employeeCreationService = employeeCreationService;
        this.monitorMapper = monitorMapper;
    }

    @Override
    public MonitorResponse getById(UUID id) {
        Monitor monitor = monitorRepository.findByIdWithEmployeeAndUser(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Monitor",id
                ));

        return monitorMapper.toResponse(monitor);
    }

    @Override
    public List<MonitorResponse> getAll() {
        return monitorRepository.findAllWithEmployeeAndUser()
                .stream()
                .map(monitorMapper::toResponse)
                .toList();
    }

    @Override
    public MonitorResponse getByRegistration(String registration) {
        Monitor monitor = monitorRepository.findByRegistration(registration)
                .orElseThrow(() -> new MonitorNotFoundException(registration));

        return monitorMapper.toResponse(monitor);
    }

    @Override
    @Transactional
    public MonitorResponse save(CreateMonitorRequest dto) {

        // 1) cria User + Employee
        Employee employee = employeeCreationService.createEmployee(
                dto.registration(),
                dto.password(),
                dto.email(),
                dto.name(),
                dto.cpf(),
                dto.phone(),
                EmployeeType.MONITOR,
                Role.ROLE_MONITOR
        );

        // 2) cria Monitor (subtipo)
        Monitor monitor = new Monitor();
        monitor.setEmployee(employee);
        monitor.setShift(dto.shift());

        Monitor saved = monitorRepository.save(monitor);

        return monitorMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(UUID id) {

        Monitor monitor = monitorRepository.findByIdWithEmployeeAndUser(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Monitor não encontrado"
                ));

        // Soft delete da conta
        userRepository.delete(monitor.getEmployee().getUser());
    }
}
