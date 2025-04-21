package io.github.redexpress.httpbin.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class HttpbinController {

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get method and URL", notes = "Returns the HTTP method and URL of the request along with headers and query parameters.")
    public Mono<Map<String, Object>> get(ServerWebExchange exchange) {
        Map<String, Object> response = new HashMap<>();
        response.put("method", exchange.getRequest().getMethod().toString());
        response.put("url", exchange.getRequest().getURI().toString());
        response.put("headers", exchange.getRequest().getHeaders().toSingleValueMap());
        response.put("query", exchange.getRequest().getQueryParams());
        return Mono.just(response);
    }

    @PostMapping(value = "/post", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Put method and URL", notes = "Returns the HTTP method and URL of the request along with headers and query parameters.")
    public Mono<Map<String, Object>> post(ServerWebExchange exchange, @RequestBody(required = false) Mono<String> bodyMono) {
        return bodyMono.defaultIfEmpty("").map(body -> {
            Map<String, Object> response = new HashMap<>();
            response.put("method", exchange.getRequest().getMethod().toString());
            response.put("url", exchange.getRequest().getURI().toString());
            response.put("headers", exchange.getRequest().getHeaders().toSingleValueMap());
            response.put("query", exchange.getRequest().getQueryParams());
            response.put("data", body);
            return response;
        });
    }

    @GetMapping(value = "/ip", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get client IP address", notes = "Returns the client IP address, including X-Forwarded-For and actual request IP.")
    public Mono<Map<String, String>> ip(ServerWebExchange exchange) {
        List<String> forwardedIps = exchange.getRequest().getHeaders().getOrEmpty("X-Forwarded-For");
        String realIp = Optional.ofNullable(exchange.getRequest().getRemoteAddress())
                .map(addr -> addr.getAddress().getHostAddress())
                .orElse("unknown");

        String origin;

        if (!forwardedIps.isEmpty()) {
            origin = String.join(", ", forwardedIps) + ", " + realIp;
        } else {
            origin = realIp;
        }

        return Mono.just(Map.of("origin", origin));
    }

    @GetMapping(value = "/headers", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get headers", notes = "Returns the headers of the current request.")
    public Mono<Map<String, Map<String, String>>> headers(ServerWebExchange exchange) {
        Map<String, String> headers = exchange.getRequest().getHeaders().toSingleValueMap();
        return Mono.just(Map.of("headers", headers));
    }
}

