package br.com.dogvision.user.service;

import br.com.dogvision.user.dto.create.CreateMonitorRequest;
import br.com.dogvision.user.dto.response.MonitorResponse;

import java.util.List;
import java.util.UUID;

public interface MonitorService {

    MonitorResponse getById(UUID id);

    MonitorResponse getByRegistration(String registration);

    List<MonitorResponse> getAll();

    MonitorResponse save(CreateMonitorRequest dto);

    void delete(UUID id);
}
