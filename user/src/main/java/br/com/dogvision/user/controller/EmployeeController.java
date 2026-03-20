package br.com.dogvision.user.controller;

import br.com.dogvision.user.dto.response.EmployeeResponse;
import br.com.dogvision.user.dto.update.UpdateEmployeeRequest;
import br.com.dogvision.user.model.Employee;
import br.com.dogvision.user.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.apache.http.protocol.HTTP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/v1/employees")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService service;

    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeResponse> update(@PathVariable UUID id, @RequestBody UpdateEmployeeRequest dto){
        EmployeeResponse response = service.update(id,dto);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> list(){
        List<EmployeeResponse> response = service.getAll();
        return ResponseEntity.ok().body(response);
    }
}
