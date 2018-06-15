package uk.co.serin.thule.test.assertj;

import java.net.URI;

public class ActuatorUri {
    private URI uri;

    public ActuatorUri(URI uri) {
        this.uri = uri;
    }

    public URI getUri() {
        return uri;
    }
}