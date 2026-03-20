package br.com.dogvision.user.service;

import br.com.dogvision.user.dto.create.CreateEmployeeRequest;
import br.com.dogvision.user.dto.response.EmployeeResponse;
import br.com.dogvision.user.dto.update.UpdateEmployeeRequest;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    EmployeeResponse getById(UUID id);

    List<EmployeeResponse> getAll();

    EmployeeResponse save(CreateEmployeeRequest dto);

    EmployeeResponse update(UUID id, UpdateEmployeeRequest dto);

    void delete(UUID id);
}
