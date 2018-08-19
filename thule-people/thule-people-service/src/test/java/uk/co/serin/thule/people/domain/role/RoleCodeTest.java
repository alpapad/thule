package uk.co.serin.thule.people.domain.role;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RoleCodeTest {
    @Test
    public void when_enums_evaluated_then_constant_equals_value() {
        assertThat(RoleCode.ROLE_ADMINISTRATOR).isEqualTo(RoleCode.valueOf("ROLE_ADMINISTRATOR"));
        assertThat(RoleCode.ROLE_CLERK).isEqualTo(RoleCode.valueOf("ROLE_CLERK"));
        assertThat(RoleCode.ROLE_MANAGER).isEqualTo(RoleCode.valueOf("ROLE_MANAGER"));
        assertThat(RoleCode.values()).hasSize(3);
    }
}