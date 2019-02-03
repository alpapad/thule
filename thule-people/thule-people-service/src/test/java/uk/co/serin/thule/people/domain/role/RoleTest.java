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
    public void when_builder_method_then_getters_operate_on_the_same_field() {
        // Given
        Role expectedRole = testDataFactory.getRoles().get(RoleCode.ROLE_CLERK);

        // When
        Role actualRole = Role.RoleBuilder.aRole().
                withCode(expectedRole.getCode()).
                withDescription(expectedRole.getDescription()).
                build();

        // Then
        assertThat(actualRole.getCode()).isEqualTo(expectedRole.getCode());
        assertThat(actualRole.getCreatedAt()).isEqualTo(expectedRole.getCreatedAt());
        assertThat(actualRole.getCreatedBy()).isEqualTo(expectedRole.getCreatedBy());
        assertThat(actualRole.getId()).isEqualTo(expectedRole.getId());
        assertThat(actualRole.getDescription()).isEqualTo(expectedRole.getDescription());
        assertThat(actualRole.getUpdatedAt()).isEqualTo(expectedRole.getUpdatedAt());
        assertThat(actualRole.getUpdatedBy()).isEqualTo(expectedRole.getUpdatedBy());
        assertThat(actualRole.getVersion()).isEqualTo(expectedRole.getVersion());
    }

    @Test
    public void when_equals_is_overridden_then_verify_equals_conforms_to_contract() {
        EqualsVerifier.forClass(Role.class).withOnlyTheseFields(Role.ENTITY_ATTRIBUTE_NAME_CODE).verify();
    }

    @Test
    public void when_pojo_methods_are_not_well_implemented_then_throw_an_exception() {
        // Given

        // When

        // Then
        assertPojoMethodsFor(Role.class, FieldPredicate.exclude("code")).
                testing(Method.SETTER).areWellImplemented();

        assertPojoMethodsFor(Role.class).
                testing(Method.CONSTRUCTOR, Method.GETTER, Method.TO_STRING).areWellImplemented();
    }
}