package de.claudioaltamura.mockserver.test;

import org.junit.jupiter.api.*;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.client.MockServerClient;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExternalApiClientTest {

    private ClientAndServer mockServer;
    private MockServerClient client;
    private ExternalApiClient externalApiClient;

    @BeforeAll
    void startServer() {
        mockServer = ClientAndServer.startClientAndServer(0);
        client = new MockServerClient("localhost", mockServer.getLocalPort());
        WebClient.Builder builder = WebClient.builder();
        builder.baseUrl("http://localhost:" + mockServer.getLocalPort() + "/api");
        externalApiClient = new ExternalApiClient(builder.build());
    }

    @AfterAll
    void stopServer() {
        mockServer.stop();
    }

    @BeforeEach
    void resetExpectations() {
        client.reset();
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
