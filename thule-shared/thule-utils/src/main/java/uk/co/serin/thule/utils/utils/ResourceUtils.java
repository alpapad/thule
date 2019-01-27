package uk.co.serin.thule.utils.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ResourceUtils {

    private ResourceUtils() {}

    public static String readFromClasspath(String resourcePath) {

        try {
            Path path = Paths.get(ResourceUtils.class.getClassLoader().getResource(resourcePath).toURI());
            byte[] fileBytes = Files.readAllBytes(path);
            return new String(fileBytes);

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}