package br.com.dogvision.audit.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
public class AuditEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String requestId;
    private String userRegistration;
    private String gatewayName;
    private String serviceName;
    private String method;
    private String path;
    private String action;
    private Integer statusCode;
    private Boolean success;
    private String ip;
    private String errorMessage;
    private Long latencyMs;
    private Instant createdAt;
}
