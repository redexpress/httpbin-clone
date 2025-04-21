package io.github.redexpress.httpbin.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest
public class HttpbinControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void get() {
        webTestClient.get()
                .uri("/get?name=chatgpt")
                .header("User-Agent", "webtest-client")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.method").isEqualTo("GET")
                .jsonPath("$.url").value(url -> ((String) url).contains("/get"))
                .jsonPath("$.query.name").isEqualTo("chatgpt")
                .jsonPath("$.headers['User-Agent']").isEqualTo("webtest-client");
    }

    @Test
    void post() {
        webTestClient.post()
                .uri("/post?x=1")
                .header("Content-Type", "application/json")
                .bodyValue("{\"message\":\"hello\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.method").isEqualTo("POST")
                .jsonPath("$.data").value(body -> ((String) body).contains("hello"));
    }

    @Test
    void ip() {
        webTestClient.get()
                .uri("/ip")
                .header("X-Forwarded-For", "123.123.123.123")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.origin").value(origin -> {
                    String val = (String) origin;
                    System.out.println(val);
                    assert val.contains("123.123.123.123");
                    assert val.contains("127.0.0.1") || val.contains("unknown");
                });
    }

    @Test
    void headers() {
        webTestClient.get()
                .uri("/headers")
                .header("X-Test-Header", "TestValue")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.headers['X-Test-Header']").isEqualTo("TestValue");
    }
}