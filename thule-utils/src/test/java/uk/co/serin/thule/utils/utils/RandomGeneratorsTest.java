package uk.co.serin.thule.utils.utils;

import org.junit.Test;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

public class RandomGeneratorsTest {
    private LocalDate NOW = LocalDate.now();

    @Test
    public void generate_random_enum_of_required_type() {
        assertThat(RandomGenerators.generateRandomEnum(Month.class)).isInstanceOf(Month.class);
    }

    @Test
    public void generate_unique_random_date_is_after_today() {
        assertThat(RandomGenerators.generateUniqueRandomDateAfter(NOW)).isAfter(NOW);
    }

    @Test
    public void generate_unique_random_date_is_before_today() {
        assertThat(RandomGenerators.generateUniqueRandomDateBefore(NOW)).isBefore(NOW);
    }

    @Test
    public void generate_unique_random_date_is_between_today_and_in_fifty_years_time() {
        // Given
        LocalDate inFiftyYearsTime = LocalDate.now().plusYears(50);

        // When
        LocalDate date = RandomGenerators.generateUniqueRandomDateBetween(NOW, inFiftyYearsTime);

        // Then
        assertThat(date).isAfter(NOW).isBefore(inFiftyYearsTime);
    }

    @Test
    public void generate_unique_random_date_is_in_the_future() {
        assertThat(RandomGenerators.generateUniqueRandomDateInTheFuture()).isAfter(NOW);
    }

    @Test
    public void generate_unique_random_date_is_in_the_past() {
        assertThat(RandomGenerators.generateUniqueRandomDateInThePast()).isBefore(NOW);
    }

    @Test
    public void generate_unique_random_integer_is_not_null() {
        assertThat(RandomGenerators.generateUniqueRandomInteger()).isNotNull();
    }

    @Test
    public void generate_unique_random_long_is_not_null() {
        assertThat(RandomGenerators.generateUniqueRandomLong()).isNotNull();
    }

    @Test
    public void generate_unique_random_string_of_explicit_maximum_length_does_exceed_required_length() {
        assertThat(RandomGenerators.generateUniqueRandomString(999).length()).isLessThanOrEqualTo(999);
    }

    @Test
    public void generate_unique_random_string_with_default_maximum_length_is_not_greater_than_that_length() {
        assertThat(RandomGenerators.generateUniqueRandomString().length()).isLessThanOrEqualTo(RandomGenerators.RANDOM_STRING_DEFAULT_MAX_LENGTH);
    }

    @Test
    public void private_constructor_executes_without_exception() {
        assertThat(BeanUtils.instantiateClass(RandomGenerators.class)).isNotNull();
    }
}
