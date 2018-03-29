package uk.co.serin.thule.people.domain.role;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import pl.pojo.tester.api.FieldPredicate;
import pl.pojo.tester.api.assertion.Method;

import uk.co.serin.thule.people.datafactory.TestDataFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

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
    public void pojo_methods_are_well_implemented() {
        // Given

        // When

        // Then
        assertPojoMethodsFor(Role.class, FieldPredicate.exclude("code")).
                testing(Method.SETTER).areWellImplemented();

        assertPojoMethodsFor(Role.class).
                testing(Method.CONSTRUCTOR, Method.GETTER, Method.TO_STRING).areWellImplemented();
    }

    @Test
    public void verify_equals_conforms_to_contract() {
        EqualsVerifier.forClass(Role.class).withOnlyTheseFields(Role.ENTITY_ATTRIBUTE_NAME_CODE).verify();
    }
}