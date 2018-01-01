package uk.co.serin.thule.people;

import com.fasterxml.jackson.databind.ser.FilterProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.util.ReflectionTestUtils;

import uk.co.serin.thule.people.domain.person.Person;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationConfigurerTest {
    @InjectMocks
    private ApplicationConfigurer applicationConfigurer;

    @Test
    public void exclude_password_filter_is_defined() {
        // Given

        // When
        Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder = applicationConfigurer.objectMapperBuilder();

        // Then
        FilterProvider filters = FilterProvider.class.cast(ReflectionTestUtils.getField(jackson2ObjectMapperBuilder, "filters"));
        assertThat(filters.findPropertyFilter(Person.EXCLUDE_CREDENTIALS_FILTER, null)).isNotNull();
    }
}