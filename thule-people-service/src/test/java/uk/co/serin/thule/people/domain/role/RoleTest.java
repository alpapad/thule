package uk.co.serin.thule.people.domain.role;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import uk.co.serin.thule.people.datafactories.TestDataFactory;
import uk.co.serin.thule.people.domain.DomainModel;

import static org.assertj.core.api.Assertions.assertThat;

public class RoleTest {
    private TestDataFactory testDataFactory = new TestDataFactory();

    @Test
    public void builder_and_getters_operate_on_the_same_field() {
        // Given
        Role expectedRole = testDataFactory.getRoles().get(RoleCode.ROLE_CLERK);

        // When
        Role actualRole = Role.RoleBuilder.aRole().
                withCode(expectedRole.getCode()).
                withCreatedAt(expectedRole.getCreatedAt()).
                withId(expectedRole.getId()).
                withDescription(expectedRole.getDescription()).
                withUpdatedAt(expectedRole.getUpdatedAt()).
                withUpdatedBy(expectedRole.getUpdatedBy()).
                withVersion(expectedRole.getVersion()).
                build();

        // Then
        assertThat(actualRole.getCode()).isEqualTo(expectedRole.getCode());
        assertThat(actualRole.getCreatedAt()).isEqualTo(expectedRole.getCreatedAt());
        assertThat(actualRole.getId()).isEqualTo(expectedRole.getId());
        assertThat(actualRole.getDescription()).isEqualTo(expectedRole.getDescription());
        assertThat(actualRole.getUpdatedAt()).isEqualTo(expectedRole.getUpdatedAt());
        assertThat(actualRole.getUpdatedBy()).isEqualTo(expectedRole.getUpdatedBy());
        assertThat(actualRole.getVersion()).isEqualTo(expectedRole.getVersion());
    }

    @Test
    public void business_key_constructor_creates_instance_with_correct_key() {
        // Given

        // When
        Role actualRole = new Role(RoleCode.ROLE_ADMINISTRATOR);

        // Then
        assertThat(actualRole.getCode()).isEqualTo(RoleCode.ROLE_ADMINISTRATOR);
    }

    @Test
    public void default_constructor_creates_instance_successfully() {
        // Given

        // When
        Role role = new Role();

        // Then
        assertThat(role).isNotNull();
    }

    @Test
    public void getters_and_setters_operate_on_the_same_field() {
        // Given
        Role expectedRole = testDataFactory.getRoles().get(RoleCode.ROLE_CLERK);

        // When
        Role actualRole = new Role(expectedRole.getCode());
        actualRole.setDescription(expectedRole.getDescription());

        // Then
        assertThat(actualRole.getCode()).isEqualTo(expectedRole.getCode());
        assertThat(actualRole.getCreatedAt()).isEqualTo(expectedRole.getCreatedAt());
        assertThat(actualRole.getId()).isEqualTo(expectedRole.getId());
        assertThat(actualRole.getDescription()).isEqualTo(expectedRole.getDescription());
        assertThat(actualRole.getUpdatedAt()).isEqualTo(expectedRole.getUpdatedAt());
        assertThat(actualRole.getUpdatedBy()).isEqualTo(expectedRole.getUpdatedBy());
        assertThat(actualRole.getVersion()).isEqualTo(expectedRole.getVersion());
    }

    @Test
    public void toString_is_overridden() {
        assertThat(new Role(RoleCode.ROLE_ADMINISTRATOR).toString()).contains(DomainModel.ENTITY_ATTRIBUTE_NAME_CODE);
    }

    @Test
    public void verify_equals_conforms_to_contract() {
        EqualsVerifier.forClass(Role.class).withOnlyTheseFields(Role.ENTITY_ATTRIBUTE_NAME_CODE).verify();
    }
}