package uk.co.serin.thule.utils.utils;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

public class RandomUtilsTest {
    private LocalDate today = LocalDate.now();

    @Test
    public void given_default_maximum_length_when_generate_unique_random_numeric_string_then_it_does_not_exceed_that_length() {
        // When
        var randomNumericString = RandomUtils.generateUniqueRandomNumericString();

        // Then
        assertThat(randomNumericString).containsOnlyDigits();
        assertThat(randomNumericString.length()).isLessThanOrEqualTo(RandomUtils.RANDOM_STRING_DEFAULT_MAX_LENGTH);
    }

    @Test
    public void given_default_maximum_length_when_generate_unique_random_string_then_it_does_not_exceed_that_length() {
        assertThat(RandomUtils.generateUniqueRandomString().length()).isLessThanOrEqualTo(RandomUtils.RANDOM_STRING_DEFAULT_MAX_LENGTH);
    }

    @Test
    public void given_explicit_maximum_length_when_generate_unique_random_numeric_string_then_it_does_not_exceed_required_length() {
        // When
        var randomNumericString = RandomUtils.generateUniqueRandomNumericString(999);

        // Then
        assertThat(randomNumericString).containsOnlyDigits();
        assertThat(randomNumericString.length()).isLessThanOrEqualTo(999);
    }

    @Test
    public void given_explicit_maximum_length_when_generate_unique_random_string_then_it_does_not_exceed_required_length() {
        assertThat(RandomUtils.generateUniqueRandomString(999).length()).isLessThanOrEqualTo(999);
    }

    @Test
    public void when_generate_random_big_decimal_from_range_then_within_defined_range() {
        // Given
        var minBigDecimal = BigDecimal.valueOf(10L);
        var maxBigDecimal = BigDecimal.valueOf(100L);

        // When
        var randomBigDecimalFromRange = RandomUtils.generateRandomBigDecimalFromRange(minBigDecimal, maxBigDecimal);

        // Then
        assertThat(randomBigDecimalFromRange).isNotNull();
        assertThat(randomBigDecimalFromRange).isBetween(minBigDecimal, maxBigDecimal);
    }

    @Test
    public void when_generate_random_enum_then_required_type_is_returned() {
        assertThat(RandomUtils.generateRandomEnum(Month.class)).isInstanceOf(Month.class);
    }

    @Test
    public void when_generate_unique_random_date_after_today_then_date_is_after_today() {
        assertThat(RandomUtils.generateUniqueRandomDateAfter(today)).isAfter(today);
    }

    @Test
    public void when_generate_unique_random_date_before_today_then_date_is_before_today() {
        assertThat(RandomUtils.generateUniqueRandomDateBefore(today)).isBefore(today);
    }

    @Test
    public void when_generate_unique_random_date_between_today_and_in_fifty_years_time_then_date_is_after_today_and_within_fifty_years() {
        // Given
        var inFiftyYearsTime = LocalDate.now().plusYears(50);

        // When
        var date = RandomUtils.generateUniqueRandomDateBetween(today, inFiftyYearsTime);

        // Then
        assertThat(date).isAfter(today).isBefore(inFiftyYearsTime);
    }

    @Test
    public void when_generate_unique_random_date_in_the_future_then_date_is_in_the_future() {
        assertThat(RandomUtils.generateUniqueRandomDateInTheFuture()).isAfter(today);
    }

    @Test
    public void when_generate_unique_random_date_in_the_past_then_date_is_in_the_past() {
        assertThat(RandomUtils.generateUniqueRandomDateInThePast()).isBefore(today);
    }

    @Test
    public void when_generate_unique_random_integer_then_it_is_not_null() {
        assertThat(RandomUtils.generateUniqueRandomInteger()).isNotNull();
    }

    @Test
    public void when_generate_unique_random_long_then_it_is_not_null() {
        assertThat(RandomUtils.generateUniqueRandomLong()).isNotNull();
    }
}
