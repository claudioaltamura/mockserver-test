package de.claudioaltamura.mockserver.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@ExtendWith(MockServerExtension.class)
class ExternalApiClientMockServerExtensionTest {

    private final ClientAndServer mockServer;
    private final MockServerClient client;
    private final ExternalApiClient externalApiClient;

    public ExternalApiClientMockServerExtensionTest(ClientAndServer mockServer) {
        this.mockServer = mockServer;
        client = new MockServerClient("localhost", mockServer.getLocalPort());
        WebClient.Builder builder = WebClient.builder();
        builder.baseUrl("http://localhost:" + mockServer.getLocalPort() + "/api");
        externalApiClient = new ExternalApiClient(builder.build());
    }

    @BeforeEach
    void beforeEach() {
        mockServer.reset();
    }


    @Test
    void testGetData() {
        client.when(
                request()
                        .withMethod("GET")
                        .withPath("/api/data")
        ).respond(
                response()
                        .withStatusCode(200)
                        .withBody("{\"msg\":\"mocked response\"}")
        );

        String result = externalApiClient.getData();

        assertThat(result).contains("mocked response");
    }

    @Test
    void testSubmitData() {
        client.when(
                request()
                        .withMethod("POST")
                        .withPath("/api/submit")
                        .withBody("test-payload")
        ).respond(
                response()
                        .withStatusCode(204)
        );

        externalApiClient.submitData("test-payload");

        client.verify(
                request()
                        .withMethod("POST")
                        .withPath("/api/submit")
                        .withBody("test-payload")
        );
    }
}
