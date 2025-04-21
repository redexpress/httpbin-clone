package io.github.redexpress.httpbin.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {
    @Value("${app.version}")
    String version;

    @GetMapping("/")
    public Mono<Void> index(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.TEMPORARY_REDIRECT);
        exchange.getResponse().getHeaders().setLocation(
                exchange.getRequest().getURI().resolve("/swagger-ui/index.html")
        );
        return exchange.getResponse().setComplete();
    }

    @GetMapping("/info")
    @ResponseBody
    public Mono<Map<String, String>> info(ServerWebExchange exchange) {
        Map<String, String> response = new HashMap<>();
        response.put("version", version);
        return Mono.just(response);
    }
}