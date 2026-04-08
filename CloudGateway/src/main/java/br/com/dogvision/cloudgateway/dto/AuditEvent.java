package br.com.dogvision.cloudgateway.dto;

import java.time.Instant;


public record AuditEvent(
        String requestId,
        String userRegistration,
        String gatewayName,
        String serviceName,
        String method,
        String path,
        String action,
        Integer statusCode,
        Boolean success,
        String ip,
        String errorMessage,
        Long latencyMs,
        Instant createdAt
) {}
