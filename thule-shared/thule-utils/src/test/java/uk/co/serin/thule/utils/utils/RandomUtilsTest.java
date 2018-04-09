package uk.co.serin.thule.utils.utils;

import org.junit.Test;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

public class RandomUtilsTest {
    private LocalDate NOW = LocalDate.now();

    @Test
    public void generate_random_enum_of_required_type() {
        assertThat(RandomUtils.generateRandomEnum(Month.class)).isInstanceOf(Month.class);
    }

    @Test
    public void generate_unique_random_date_is_after_today() {
        assertThat(RandomUtils.generateUniqueRandomDateAfter(NOW)).isAfter(NOW);
    }

    @Test
    public void generate_unique_random_date_is_before_today() {
        assertThat(RandomUtils.generateUniqueRandomDateBefore(NOW)).isBefore(NOW);
    }

    @Test
    public void generate_unique_random_date_is_between_today_and_in_fifty_years_time() {
        // Given
        LocalDate inFiftyYearsTime = LocalDate.now().plusYears(50);

        // When
        LocalDate date = RandomUtils.generateUniqueRandomDateBetween(NOW, inFiftyYearsTime);

        // Then
        assertThat(date).isAfter(NOW).isBefore(inFiftyYearsTime);
    }

    @Test
    public void generate_unique_random_date_is_in_the_future() {
        assertThat(RandomUtils.generateUniqueRandomDateInTheFuture()).isAfter(NOW);
    }

    @Test
    public void generate_unique_random_date_is_in_the_past() {
        assertThat(RandomUtils.generateUniqueRandomDateInThePast()).isBefore(NOW);
    }

    @Test
    public void generate_unique_random_integer_is_not_null() {
        assertThat(RandomUtils.generateUniqueRandomInteger()).isNotNull();
    }

    @Test
    public void generate_unique_random_long_is_not_null() {
        assertThat(RandomUtils.generateUniqueRandomLong()).isNotNull();
    }

    @Test
    public void generate_unique_random_string_of_explicit_maximum_length_does_exceed_required_length() {
        assertThat(RandomUtils.generateUniqueRandomString(999).length()).isLessThanOrEqualTo(999);
    }

    @Test
    public void generate_unique_random_string_with_default_maximum_length_is_not_greater_than_that_length() {
        assertThat(RandomUtils.generateUniqueRandomString().length()).isLessThanOrEqualTo(RandomUtils.RANDOM_STRING_DEFAULT_MAX_LENGTH);
    }

    @Test
    public void private_constructor_executes_without_exception() {
        assertThat(BeanUtils.instantiateClass(RandomUtils.class)).isNotNull();
    }
}
