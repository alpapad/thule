package uk.co.serin.thule.people.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pl.pojo.tester.api.FieldPredicate;
import pl.pojo.tester.api.assertion.Method;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

@RunWith(MockitoJUnitRunner.class)
public class DomainModelTest {

    @Test
    public void when_pojo_methods_are_not_well_implemented_then_throw_an_exception() {
        // Given

        // When

        // Then
        assertPojoMethodsFor(DomainModel.class, FieldPredicate.exclude("createdAt", "createdBy", "id", "updatedAt", "updatedBy", "version")).
                testing(Method.SETTER).areWellImplemented();

        assertPojoMethodsFor(DomainModel.class).
                testing(Method.CONSTRUCTOR, Method.GETTER, Method.TO_STRING).areWellImplemented();
    }
}
