package uk.co.serin.thule.people;


import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ClasspathFileSource;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@Configuration
@ConditionalOnProperty(name = "thule.wiremock.port")
public class WiremockConfigurer {
    @Value("${thule.wiremock.port}")
    private int wireMockPort;

    /**
     * When using a Feign client, it will try to use the load balancer (Ribbon) to lookup the
     * service via a discovery service (Eureka). However, for the integration test, we don't use
     * Ribbon or Eureka so we need to tell Feign to use a static server, in this case Wiremock.
     */
    @Bean
    public ServerList<Server> ribbonServerList() {
        return new StaticServerList<>(new Server("localhost", wireMockPort));
    }

    @Bean
    public WireMockServer wireMockServer() {
        WireMockServer wireMockServer = new WireMockServer(wireMockConfig()
                .fileSource(new ClasspathFileSource("wiremock-mappings"))
                .port(wireMockPort));
        wireMockServer.start();

        return wireMockServer;
    }
}
