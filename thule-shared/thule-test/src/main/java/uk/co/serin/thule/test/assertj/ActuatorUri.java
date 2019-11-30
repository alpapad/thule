package uk.co.serin.thule.test.assertj;

import java.net.URI;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class ActuatorUri {
    private URI uri;

    public static ActuatorUri using(String uri) {
        return ActuatorUri.using(URI.create(uri));
    }

    public static ActuatorUri using(URI uri) {
        return new ActuatorUri(uri);
    }
}