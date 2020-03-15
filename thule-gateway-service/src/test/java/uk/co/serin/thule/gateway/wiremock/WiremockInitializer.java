package uk.co.serin.thule.gateway.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.util.SocketUtils;

public class WiremockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        var wiremockServerPort = createWiremockServerPort(applicationContext);
        mockOpenidConfigurationResponse(wiremockServerPort);
    }

    private int createWiremockServerPort(ConfigurableApplicationContext applicationContext) {
        var wireMockServerPort = SocketUtils.findAvailableTcpPort();
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext, "wiremock.server.port=" + wireMockServerPort);

        return wireMockServerPort;
    }

    private void mockOpenidConfigurationResponse(int wiremockServerPort) {
        var mockHttpServer = new WireMockServer(wiremockServerPort);

        mockHttpServer.stubFor(WireMock.get(WireMock.urlEqualTo("/auth/realms/thule-test/.well-known/openid-configuration")).willReturn(
                WireMock.aResponse().withStatus(HttpStatus.OK.value()).withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(openidConfiguration(wiremockServerPort))));
        mockHttpServer.start();
    }

    private String openidConfiguration(int wiremockServerPort) {
        return "{" +
                "  \"issuer\": \"http://localhost:" + wiremockServerPort + "/auth/realms/thule-test\"," +
                "  \"authorization_endpoint\": \"http://localhost:" + wiremockServerPort + "/auth/realms/thule-test/protocol/openid-connect/auth\"," +
                "  \"token_endpoint\": \"http://localhost:" + wiremockServerPort + "/auth/realms/thule-test/protocol/openid-connect/token\"," +
                "  \"userinfo_endpoint\": \"http://localhost:" + wiremockServerPort + "/auth/realms/thule-test/protocol/openid-connect/userinfo\"," +
                "  \"jwks_uri\": \"http://localhost:" + wiremockServerPort + "/auth/realms/thule-test/protocol/openid-connect/certs\"," +
                "  \"subject_types_supported\": [" +
                "    \"public\"," +
                "    \"pairwise\"" +
                "  ]" +
                "}";
    }
}
