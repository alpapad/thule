package uk.co.serin.thule.people.domain.entity.address;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HomeAddressEntityTest {
    @Test
    public void when_constructing_then_a_home_address_is_instantiated() {
        // When
        var homeAddress = HomeAddressEntity.builder().build();

        // Then
        assertThat(homeAddress).isNotNull();
    }
}