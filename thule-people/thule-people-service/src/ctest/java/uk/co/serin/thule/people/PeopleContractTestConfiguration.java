package uk.co.serin.thule.people;

import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class PeopleContractTestConfiguration {
    @Value("${wiremock.server.port}")
    private int wireMockServerPort;

    /**
     * When using a Feign client, it will try to use the load balancer (Ribbon) to lookup the
     * service via a discovery service (Eureka). However, for the contract test, we don't use
     * Ribbon or Eureka so we need to tell Feign to use a static server, in this case Wiremock.
     */
    @Bean
    public ServerList<Server> ribbonServerList() {
        return new StaticServerList<>(new Server("localhost", wireMockServerPort));
    }
}
