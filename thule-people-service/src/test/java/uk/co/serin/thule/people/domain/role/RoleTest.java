package uk.co.serin.thule.people.domain.role;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import uk.co.serin.thule.people.datafactories.MockReferenceDataFactory;
import uk.co.serin.thule.people.datafactories.ReferenceDataFactory;
import uk.co.serin.thule.people.domain.DomainModel;

import static org.assertj.core.api.Assertions.assertThat;

public class RoleTest {
    private ReferenceDataFactory referenceDataFactory = new MockReferenceDataFactory();

    @Test
    public void businessKeyConstructorCreatesInstanceWithCorrectKey() {
        // Given

        // When
        Role actualRole = new Role(RoleCode.ROLE_ADMINISTRATOR);

        // Then
        assertThat(actualRole.getCode()).isEqualTo(RoleCode.ROLE_ADMINISTRATOR);
    }

    @Test
    public void copyConstructorCreatesInstanceWithSameFieldValues() {
        // Given
        Role expectedRole = referenceDataFactory.getRoles().get(RoleCode.ROLE_ADMINISTRATOR);

        // When
        Role actualRole = new Role(expectedRole);

        // Then
        assertThat(actualRole).isEqualToComparingFieldByField(expectedRole);
    }

    @Test
    public void defaultConstructorCreatesInstanceSuccessfully() {
        // Given

        // When
        Role role = new Role();

        // Then
        assertThat(role).isNotNull();
    }

    @Test
    public void gettersAndSettersOperateOnTheSameField() {
        // Given
        String description = "description";

        Role role = new Role(RoleCode.ROLE_CLERK);
        role.setDescription(description);

        // When/Then
        assertThat(role.getCode()).isEqualTo(RoleCode.ROLE_CLERK);
        assertThat(role.getDescription()).isEqualTo(description);
    }

    @Test
    public void toStringIsOverridden() {
        assertThat(new Role(RoleCode.ROLE_ADMINISTRATOR).toString()).contains(DomainModel.ENTITY_ATTRIBUTE_NAME_CODE);
    }

    @Test
    public void verifyEqualsConformsToContract() {
        EqualsVerifier.forClass(Role.class).suppress(Warning.ALL_FIELDS_SHOULD_BE_USED).verify();
    }
}