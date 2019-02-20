package uk.co.serin.thule.test.assertj;

import java.net.URI;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@ToString
public class ActuatorUri {
    private URI uri;

    public static ActuatorUri of(String uri) {
        return ActuatorUri.of(URI.create(uri));
    }

    public static ActuatorUri of(URI uri) {
        return new ActuatorUri(uri);
    }
}