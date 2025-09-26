package de.claudioaltamura.mockserver.test;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ExternalApiClient {

    private final WebClient webClient;

    public ExternalApiClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public String getData() {
        return webClient.get()
                .uri("/data")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public void submitData(String payload) {
        webClient.post()
                .uri("/submit")
                .bodyValue(payload)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
