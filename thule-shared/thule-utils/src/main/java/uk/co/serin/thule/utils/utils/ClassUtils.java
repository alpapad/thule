package uk.co.serin.thule.utils.utils;

public interface ClassUtils {
    static Class<?> forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
}
