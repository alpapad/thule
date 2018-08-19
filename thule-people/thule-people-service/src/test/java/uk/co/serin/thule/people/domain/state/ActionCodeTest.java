package uk.co.serin.thule.people.domain.state;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ActionCodeTest {
    @Test
    public void when_enums_evaluated_then_constant_equals_value() {
        assertThat(ActionCode.ADDRESS_DISABLE).isEqualTo(ActionCode.valueOf("ADDRESS_DISABLE"));
        assertThat(ActionCode.ADDRESS_DISCARD).isEqualTo(ActionCode.valueOf("ADDRESS_DISCARD"));
        assertThat(ActionCode.ADDRESS_ENABLE).isEqualTo(ActionCode.valueOf("ADDRESS_ENABLE"));
        assertThat(ActionCode.ADDRESS_RECOVER).isEqualTo(ActionCode.valueOf("ADDRESS_RECOVER"));
        assertThat(ActionCode.ADDRESS_UPDATE).isEqualTo(ActionCode.valueOf("ADDRESS_UPDATE"));
        assertThat(ActionCode.ADDRESS_VIEW).isEqualTo(ActionCode.valueOf("ADDRESS_VIEW"));
        assertThat(ActionCode.PERSON_DISABLE).isEqualTo(ActionCode.valueOf("PERSON_DISABLE"));
        assertThat(ActionCode.PERSON_DISCARD).isEqualTo(ActionCode.valueOf("PERSON_DISCARD"));
        assertThat(ActionCode.PERSON_ENABLE).isEqualTo(ActionCode.valueOf("PERSON_ENABLE"));
        assertThat(ActionCode.PERSON_RECOVER).isEqualTo(ActionCode.valueOf("PERSON_RECOVER"));
        assertThat(ActionCode.PERSON_UPDATE).isEqualTo(ActionCode.valueOf("PERSON_UPDATE"));
        assertThat(ActionCode.PERSON_VIEW).isEqualTo(ActionCode.valueOf("PERSON_VIEW"));
        assertThat(ActionCode.values()).hasSize(12);
    }
}