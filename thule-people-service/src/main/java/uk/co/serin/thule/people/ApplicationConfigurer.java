package uk.co.serin.thule.people;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import uk.co.serin.thule.people.domain.person.Person;

@Configuration
public class ApplicationConfigurer {
    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        SimpleBeanPropertyFilter excludePasswordFilter = SimpleBeanPropertyFilter.serializeAllExcept("password");
        FilterProvider filters = new SimpleFilterProvider().addFilter(Person.EXCLUDE_CREDENTIALS_FILTER, excludePasswordFilter);

        return new Jackson2ObjectMapperBuilder().filters(filters);
    }
}
