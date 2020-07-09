package uk.co.serin.thule.people.domain.entity.address;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WorkAddressEntityTest {
    @Test
    void when_constructing_then_a_work_address_is_instantiated() {
        // When
        var workAddress = WorkAddressEntity.builder().build();

        // Then
        assertThat(workAddress).isNotNull();
    }
}