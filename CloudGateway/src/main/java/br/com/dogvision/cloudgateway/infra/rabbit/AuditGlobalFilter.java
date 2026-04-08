package br.com.dogvision.cloudgateway.infra.rabbit;

import br.com.dogvision.cloudgateway.dto.AuditEvent;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class AuditGlobalFilter implements GlobalFilter, Ordered {

    private final StreamBridge streamBridge;
    private final ObjectMapper objectMapper;

    public AuditGlobalFilter(StreamBridge streamBridge, ObjectMapper objectMapper) {
        this.streamBridge = streamBridge;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();

        String method = exchange.getRequest().getMethod() != null
                ? exchange.getRequest().getMethod().name()
                : "UNKNOWN";

        String path = exchange.getRequest().getURI().getPath();
        String ip = getClientIp(exchange);
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String serviceName = getServiceName(exchange);

        final AtomicReference<Throwable> errorRef = new AtomicReference<>();

        return chain.filter(exchange)
                .doOnError(errorRef::set)
                .doFinally(signalType -> {
                    long latency = System.currentTimeMillis() - startTime;

                    String userRegistration = extractUserId(authHeader);
                    HttpStatusCode statusCodeObj = exchange.getResponse().getStatusCode();
                    int statusCode = statusCodeObj != null ? statusCodeObj.value() : 500;

                    boolean success = errorRef.get() == null && statusCode < 400;
                    String errorMessage = errorRef.get() != null ? errorRef.get().getMessage() : null;

                    String requestId = exchange.getRequest()
                            .getHeaders()
                            .getFirst("X-Request-ID");
                    if (requestId == null || requestId.isBlank()){
                        requestId = UUID.randomUUID().toString();
                    }

                    AuditEvent event = new AuditEvent(
                            requestId,
                            userRegistration,
                            "api-gateway",
                            serviceName,
                            method,
                            path,
                            buildAction(method, path),
                            statusCode,
                            success,
                            ip,
                            errorMessage,
                            latency,
                            Instant.now()
                    );

                    try {
                        streamBridge.send("audit-out-0", event);
                    } catch (Exception e) {
                        System.err.println("Error to send auditory event: " + e.getMessage());
                    }
                });
    }

    private String extractUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return "anonymous";
        }

        try {
            String token = authHeader.substring(7);
            String[] parts = token.split("\\.");

            if (parts.length < 2) {
                return "anonymous";
            }

            byte[] decodedBytes = Base64.getUrlDecoder().decode(parts[1]);
            JsonNode node = objectMapper.readTree(decodedBytes);

            if (node.has("sub")) {
                return node.get("sub").asText();
            }

            if (node.has("user_id")) {
                return node.get("user_id").asText();
            }

            return "anonymous";
        } catch (Exception e) {
            return "anonymous";
        }
    }

    private String getServiceName(ServerWebExchange exchange){
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        if (route != null){
            return route.getId();
        }
        return "unknown-service";
    }

    private String getClientIp(ServerWebExchange exchange) {
        String xForwardedFor = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = exchange.getRequest().getHeaders().getFirst("X-Real-IP");
        if (xRealIp != null && !xRealIp.isBlank()) {
            return xRealIp.trim();
        }

        InetSocketAddress remoteAddress = exchange.getRequest().getRemoteAddress();
        if (remoteAddress != null && remoteAddress.getAddress() != null) {
            return remoteAddress.getAddress().getHostAddress();
        }

        return "unknown";
    }

    private String buildAction(String method, String path) {
        return method + " " + path;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}

