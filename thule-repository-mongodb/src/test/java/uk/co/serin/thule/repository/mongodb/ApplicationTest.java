package uk.co.serin.thule.repository.mongodb;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationTest {
    @Test
    public void default_constructor_creates_instance_successfully() {
        assertThat(new Application()).isNotNull();
    }
}