package uk.co.serin.thule.people.domain.person;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pl.pojo.tester.api.FieldPredicate;
import pl.pojo.tester.api.assertion.Method;

import uk.co.serin.thule.people.datafactory.TestDataFactory;

import javax.validation.ValidationException;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

@RunWith(MockitoJUnitRunner.class)
public class PhotographTest {
    private TestDataFactory testDataFactory = new TestDataFactory();

    @Test
    public void builder_and_getters_operate_on_the_same_field() {
        // Given
        Photograph expectedPhotograph = testDataFactory.buildPhotographMissScarlett(testDataFactory.buildPersonWithoutAnyAssociations());

        // When
        Photograph actualPhotograph = Photograph.PhotographBuilder.aPhotograph().
                withCreatedAt(expectedPhotograph.getCreatedAt()).
                withId(expectedPhotograph.getId()).
                withPerson(expectedPhotograph.getPerson()).
                withPhoto(expectedPhotograph.getPhoto()).
                withPosition(expectedPhotograph.getPosition()).
                withUpdatedAt(expectedPhotograph.getUpdatedAt()).
                withUpdatedBy(expectedPhotograph.getUpdatedBy()).
                withVersion(expectedPhotograph.getVersion()).
                build();

        // Then
        assertThat(actualPhotograph.getCreatedAt()).isEqualTo(expectedPhotograph.getCreatedAt());
        assertThat(actualPhotograph.getHash()).isEqualTo(expectedPhotograph.getHash());
        assertThat(actualPhotograph.getId()).isEqualTo(expectedPhotograph.getId());
        assertThat(actualPhotograph.getPerson()).isEqualTo(expectedPhotograph.getPerson());
        assertThat(actualPhotograph.getPhoto()).isEqualTo(expectedPhotograph.getPhoto());
        assertThat(actualPhotograph.getPosition()).isEqualTo(expectedPhotograph.getPosition());
        assertThat(actualPhotograph.getUpdatedAt()).isEqualTo(expectedPhotograph.getUpdatedAt());
        assertThat(actualPhotograph.getUpdatedBy()).isEqualTo(expectedPhotograph.getUpdatedBy());
        assertThat(actualPhotograph.getVersion()).isEqualTo(expectedPhotograph.getVersion());
    }

    @Test(expected = ValidationException.class)
    public void business_key_constructor_throws_ValidationException_when_person_is_empty() {
        // Given

        // When
        new Photograph("test photo".getBytes(), null);

        // Then (see expected in @Test annotation)
    }

    @Test(expected = ValidationException.class)
    public void business_key_constructor_throws_ValidationException_when_photo_is_empty() {
        // Given

        // When
        new Photograph(null, new Person("test userid"));

        // Then (see expected in @Test annotation)
    }

    @Test
    public void pojo_methods_are_well_implemented() {
        assertPojoMethodsFor(Photograph.class, FieldPredicate.exclude("hash", "photo", "person")).
                testing(Method.SETTER).areWellImplemented();

        assertPojoMethodsFor(Photograph.class, FieldPredicate.exclude("person")).
                testing(Method.TO_STRING).areWellImplemented();

        assertPojoMethodsFor(Photograph.class).
                testing(Method.CONSTRUCTOR, Method.GETTER).areWellImplemented();
    }

    @Test
    public void verify_equals_conforms_to_contract() {
        EqualsVerifier.forClass(Photograph.class).
                withPrefabValues(Person.class, new Person("userid"), new Person("another-userid")).
                withOnlyTheseFields(Photograph.ENTITY_ATTRIBUTE_NAME_HASH, Photograph.ENTITY_NAME_PERSON).
                verify();
    }
}