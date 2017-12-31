package uk.co.serin.thule.people;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
public class CorsRepositoryRestConfigurer extends RepositoryRestConfigurerAdapter {
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.getCorsRegistry().
                // Add CORS headers for ALL paths
                addMapping("/**").
                // By default only "GET", "HEAD" and "POST" methods are allowed but we need to also allow OPTIONS and possibly PUT, DELETE in the future
                allowedMethods("*");
    }
}
