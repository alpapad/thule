package uk.co.serin.thule.core.utils;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public final class RandomGenerators {
    static final int RANDOM_STRING_DEFAULT_MAX_LENGTH = 100;
    private static final int FIFTY = 50;
    private static final Set<LocalDate> RANDOM_DATES = Collections.synchronizedSet(new HashSet<>());
    private static final Set<Integer> RANDOM_INTEGERS = Collections.synchronizedSet(new HashSet<>());
    private static final Set<Long> RANDOM_LONGS = Collections.synchronizedSet(new HashSet<>());
    private static final Set<String> RANDOM_STRINGS = Collections.synchronizedSet(new HashSet<>());

    private RandomGenerators() {
    }

    public static <T extends Enum> T generateRandomEnum(Class<T> enumeration) {
        final T[] enumConstants = enumeration.getEnumConstants();
        return enumConstants[RandomUtils.nextInt(enumConstants.length)];
    }

    public static LocalDate generateUniqueRandomDateAfter(LocalDate afterDate) {
        return generateUniqueRandomDateBetween(afterDate, afterDate.plusYears(FIFTY));
    }

    public static LocalDate generateUniqueRandomDateBetween(LocalDate afterDate, LocalDate beforeDate) {
        long minimum = afterDate.plusDays(1).toEpochDay();
        long maximum = beforeDate.toEpochDay();

        LocalDate randomDate = null;
        boolean uniqueFound = false;

        while (!uniqueFound) {
            long randomEpochDay = ThreadLocalRandom.current().nextLong(minimum, maximum);
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
        int randomInt = 0;
        boolean uniqueFound = false;

        while (!uniqueFound) {
            randomInt = RandomUtils.nextInt();
            uniqueFound = RANDOM_INTEGERS.add(randomInt);
        }
        return randomInt;
    }

    public static long generateUniqueRandomLong() {
        long randomLong = 0;
        boolean uniqueFound = false;

        while (!uniqueFound) {
            randomLong = RandomUtils.nextLong();
            uniqueFound = RANDOM_LONGS.add(randomLong);
        }
        return randomLong;
    }

    public static String generateUniqueRandomString() {
        return generateUniqueRandomString(RANDOM_STRING_DEFAULT_MAX_LENGTH);
    }

    public static String generateUniqueRandomString(int maxLength) {
        String randomString = null;
        boolean uniqueFound = false;

        while (!uniqueFound) {
            randomString = RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(maxLength - 1) + 1);
            uniqueFound = RANDOM_STRINGS.add(randomString);
        }
        return randomString;
    }
}