package uk.co.serin.thule.gateway.testcontainers;

import org.mockserver.client.MockServerClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testcontainers.containers.MockServerContainer;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class OpenIdMockServerContainer extends MockServerContainer {
    public static final String VERSION = "5.10.0";

    public OpenIdMockServerContainer() {
        super(VERSION);
    }

    public OpenIdMockServerContainer(String version) {
        super(version);
    }

    @Override
    public void start() {
        super.start();
        mockOpenidConfigurationResponse();
    }

    private void mockOpenidConfigurationResponse() {
        new MockServerClient(getContainerIpAddress(), getServerPort())
                .when(request().withMethod("GET").withPath("/auth/realms/thule-test/.well-known/openid-configuration"))
                .respond(response().withBody(openidConfiguration())
                                   .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                   .withStatusCode(HttpStatus.OK.value()));
    }

    private String openidConfiguration() {
        var authority = String.format("http://%s:%s", getContainerIpAddress(), getServerPort());
        if (getNetworkAliases().contains("mockserver")) {
            authority = String.format("http://%s:%s", "mockserver", PORT);
        }

        return "{" +
                "  \"issuer\": \"" + authority + "/auth/realms/thule-test\"," +
                "  \"authorization_endpoint\": \"" + authority + "/auth/realms/thule-test/protocol/openid-connect/auth\"," +
                "  \"token_endpoint\": \"" + authority + "/auth/realms/thule-test/protocol/openid-connect/token\"," +
                "  \"userinfo_endpoint\": \"" + authority + "/auth/realms/thule-test/protocol/openid-connect/userinfo\"," +
                "  \"jwks_uri\": \"" + authority + "/auth/realms/thule-test/protocol/openid-connect/certs\"," +
                "  \"subject_types_supported\": [" +
                "    \"public\"," +
                "    \"pairwise\"" +
                "  ]" +
                "}";
    }
}
