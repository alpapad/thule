package uk.co.serin.thule.people.domain.entity.address;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkAddressEntityTest {
    @Test
    public void when_constructing_then_a_work_address_is_instantiated() {
        // When
        var workAddress = WorkAddressEntity.builder().build();

        // Then
        assertThat(workAddress).isNotNull();
    }
}