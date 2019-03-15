package uk.co.serin.thule.utils.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RandomUtils {
    static final int RANDOM_STRING_DEFAULT_MAX_LENGTH = 100;
    private static final int FIFTY = 50;
    private static final String RAMDOM_NUMERIC_CHARACTERS = "0123456789";
    private static final String RAMDOM_STRING_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Set<LocalDate> RANDOM_DATES = Collections.synchronizedSet(new HashSet<>());
    private static final Set<Integer> RANDOM_INTEGERS = Collections.synchronizedSet(new HashSet<>());
    private static final Set<Long> RANDOM_LONGS = Collections.synchronizedSet(new HashSet<>());
    private static final Set<String> RANDOM_STRINGS = Collections.synchronizedSet(new HashSet<>());

    public static BigDecimal generateRandomBigDecimalFromRange(BigDecimal min, BigDecimal max) {
        var range = max.subtract(min);
        var multiplicand = BigDecimal.valueOf(Math.random());
        return min.add(range.multiply(multiplicand));
    }

    public static <T extends Enum> T generateRandomEnum(Class<T> enumeration) {
        var enumConstants = enumeration.getEnumConstants();
        return enumConstants[ThreadLocalRandom.current().nextInt(enumConstants.length)];
    }

    public static LocalDate generateUniqueRandomDateAfter(LocalDate afterDate) {
        return generateUniqueRandomDateBetween(afterDate, afterDate.plusYears(FIFTY));
    }

    public static LocalDate generateUniqueRandomDateBetween(LocalDate afterDate, LocalDate beforeDate) {
        var minimum = afterDate.plusDays(1).toEpochDay();
        var maximum = beforeDate.toEpochDay();

        LocalDate randomDate = null;
        var uniqueFound = false;

        while (!uniqueFound) {
            var randomEpochDay = ThreadLocalRandom.current().nextLong(minimum, maximum);
            randomDate = LocalDate.ofEpochDay(randomEpochDay);
            uniqueFound = RANDOM_DATES.add(randomDate);
        }
        return randomDate;
    }

    public static LocalDate generateUniqueRandomDateBefore(LocalDate beforeDate) {
        return generateUniqueRandomDateBetween(beforeDate.minusYears(FIFTY), beforeDate);
    }

    public static LocalDate generateUniqueRandomDateInTheFuture() {
        return generateUniqueRandomDateBetween(LocalDate.now(), LocalDate.now().plusYears(FIFTY));
    }

    public static LocalDate generateUniqueRandomDateInThePast() {
        return generateUniqueRandomDateBetween(LocalDate.now().minusYears(FIFTY), LocalDate.now());
    }

    public static int generateUniqueRandomInteger() {
        var randomInt = 0;
        var uniqueFound = false;

        while (!uniqueFound) {
            randomInt = ThreadLocalRandom.current().nextInt();
            uniqueFound = RANDOM_INTEGERS.add(randomInt);
        }
        return randomInt;
    }

    public static long generateUniqueRandomLong() {
        long randomLong = 0;
        var uniqueFound = false;

        while (!uniqueFound) {
            randomLong = ThreadLocalRandom.current().nextLong();
            uniqueFound = RANDOM_LONGS.add(randomLong);
        }
        return randomLong;
    }

    public static String generateUniqueRandomNumericString() {
        return generateUniqueRandomNumericString(RANDOM_STRING_DEFAULT_MAX_LENGTH);
    }

    public static String generateUniqueRandomNumericString(int maxLength) {
        return generateUniqueFixedLengthRandomNumericString(ThreadLocalRandom.current().nextInt(maxLength));
    }

    public static String generateUniqueFixedLengthRandomNumericString(int length) {
        var randomString = new StringBuilder();
        var uniqueFound = false;

        while (!uniqueFound) {
            while (randomString.length() <= length) {
                var randomIndex = ThreadLocalRandom.current().nextInt(RAMDOM_NUMERIC_CHARACTERS.length());
                randomString.append(RAMDOM_NUMERIC_CHARACTERS.charAt(randomIndex));
            }
            uniqueFound = RANDOM_STRINGS.add(randomString.toString());
        }

        return randomString.toString();
    }

    public static String generateUniqueRandomString() {
        return generateUniqueRandomString(RANDOM_STRING_DEFAULT_MAX_LENGTH);
    }

    public static String generateUniqueRandomString(int maxLength) {
        return generateUniqueFixedLengthRandomString(ThreadLocalRandom.current().nextInt(maxLength));
    }

    public static String generateUniqueFixedLengthRandomString(int length) {
        var randomString = new StringBuilder();
        var uniqueFound = false;

        while (!uniqueFound) {
            while (randomString.length() <= length) {
                var randomIndex = ThreadLocalRandom.current().nextInt(RAMDOM_STRING_CHARACTERS.length());
                randomString.append(RAMDOM_STRING_CHARACTERS.charAt(randomIndex));
            }
            uniqueFound = RANDOM_STRINGS.add(randomString.toString());
        }

        return randomString.toString();
    }
}