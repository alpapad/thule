package uk.co.serin.thule.people.domain.role;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import uk.co.serin.thule.people.datafactories.MockReferenceDataFactory;
import uk.co.serin.thule.people.datafactories.ReferenceDataFactory;
import uk.co.serin.thule.people.domain.DomainModel;

import static org.assertj.core.api.Assertions.assertThat;

public class RoleTest {
    private ReferenceDataFactory referenceDataFactory = new MockReferenceDataFactory();

    @Test
    public void builderAndSettersOperateOnTheSameField() {
        // Given
        Role expectedRole = referenceDataFactory.getRoles().get(RoleCode.ROLE_CLERK);

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
    public void businessKeyConstructorCreatesInstanceWithCorrectKey() {
        // Given

        // When
        Role actualRole = new Role(RoleCode.ROLE_ADMINISTRATOR);

        // Then
        assertThat(actualRole.getCode()).isEqualTo(RoleCode.ROLE_ADMINISTRATOR);
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
        Role expectedRole = referenceDataFactory.getRoles().get(RoleCode.ROLE_CLERK);

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
    public void toStringIsOverridden() {
        assertThat(new Role(RoleCode.ROLE_ADMINISTRATOR).toString()).contains(DomainModel.ENTITY_ATTRIBUTE_NAME_CODE);
    }

    @Test
    public void verifyEqualsConformsToContract() {
        EqualsVerifier.forClass(Role.class).withOnlyTheseFields(Role.ENTITY_ATTRIBUTE_NAME_CODE).verify();
    }
}