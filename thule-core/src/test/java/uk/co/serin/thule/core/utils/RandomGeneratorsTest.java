package uk.co.serin.thule.core.utils;

import org.junit.Test;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

public class RandomGeneratorsTest {
    private LocalDate NOW = LocalDate.now();

    @Test
    public void generateRandomEnumOfRequiredType() {
        assertThat(RandomGenerators.generateRandomEnum(Month.class)).isInstanceOf(Month.class);
    }

    @Test
    public void generateUniqueRandomDateIsAfterToday() {
        assertThat(RandomGenerators.generateUniqueRandomDateAfter(NOW)).isAfter(NOW);
    }

    @Test
    public void generateUniqueRandomDateIsBeforeToday() {
        assertThat(RandomGenerators.generateUniqueRandomDateBefore(NOW)).isBefore(NOW);
    }

    @Test
    public void generateUniqueRandomDateIsBetweenTodayAndInFiftyYearsTime() {
        // Given
        LocalDate inFiftyYearsTime = LocalDate.now().plusYears(50);

        // When
        LocalDate date = RandomGenerators.generateUniqueRandomDateBetween(NOW, inFiftyYearsTime);

        // Then
        assertThat(date).isAfter(NOW).isBefore(inFiftyYearsTime);
    }

    @Test
    public void generateUniqueRandomDateIsInTheFuture() {
        assertThat(RandomGenerators.generateUniqueRandomDateInTheFuture()).isAfter(NOW);
    }

    @Test
    public void generateUniqueRandomDateIsInThePast() {
        assertThat(RandomGenerators.generateUniqueRandomDateInThePast()).isBefore(NOW);
    }

    @Test
    public void generateUniqueRandomIntegerIsNotNull() {
        assertThat(RandomGenerators.generateUniqueRandomInteger()).isNotNull();
    }

    @Test
    public void generateUniqueRandomLongIsNotNull() {
        assertThat(RandomGenerators.generateUniqueRandomLong()).isNotNull();
    }

    @Test
    public void generateUniqueRandomStringOfExplicitMaximumLengthDoesExceedRequiredLength() {
        assertThat(RandomGenerators.generateUniqueRandomString(999).length()).isLessThanOrEqualTo(999);
    }

    @Test
    public void generateUniqueRandomStringWithDefaultMaximumLengthIsNotGreaterThanThatLength() {
        assertThat(RandomGenerators.generateUniqueRandomString().length()).isLessThanOrEqualTo(RandomGenerators.RANDOM_STRING_DEFAULT_MAX_LENGTH);
    }

    @Test
    public void privateConstructorExecutesWithoutException() {
        assertThat(BeanUtils.instantiateClass(RandomGenerators.class)).isNotNull();
    }
}
