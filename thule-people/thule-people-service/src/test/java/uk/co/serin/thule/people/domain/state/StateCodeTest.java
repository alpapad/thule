package uk.co.serin.thule.people.domain.state;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StateCodeTest {
    @Test
    public void when_enums_evaluated_then_constant_equals_value() {
        assertThat(StateCode.ADDRESS_DISABLED).isEqualTo(StateCode.valueOf("ADDRESS_DISABLED"));
        assertThat(StateCode.ADDRESS_DISCARDED).isEqualTo(StateCode.valueOf("ADDRESS_DISCARDED"));
        assertThat(StateCode.ADDRESS_ENABLED).isEqualTo(StateCode.valueOf("ADDRESS_ENABLED"));
        assertThat(StateCode.PERSON_DISABLED).isEqualTo(StateCode.valueOf("PERSON_DISABLED"));
        assertThat(StateCode.PERSON_DISCARDED).isEqualTo(StateCode.valueOf("PERSON_DISCARDED"));
        assertThat(StateCode.PERSON_ENABLED).isEqualTo(StateCode.valueOf("PERSON_ENABLED"));
        assertThat(StateCode.values()).hasSize(6);
    }
}