package uk.co.serin.thule.people.domain.person;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import uk.co.serin.thule.people.domain.DomainModel;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PhotographTest {
    @Test
    public void businessKeyConstructorCreatesInstanceWithCorrectKey() {
        // Given
        Person person = new Person("userId");
        byte[] photo = {};

        // When
        Photograph photograph = new Photograph(photo, person);

        // Then
        assertThat(photograph.getHash()).isNotNull();
        assertThat(photograph.getPerson()).isEqualTo(photograph.getPerson());
        assertThat(photograph.getPhoto()).isEqualTo(photograph.getPhoto());
    }

    @Test
    public void copyConstructorCreatesInstanceWithSameFieldValues() {
        // Given
        Person person = new Person("userId");
        byte[] photo = {};

        Photograph expectedPhotograph = new Photograph(photo, person);
        expectedPhotograph.setPosition(1);

        // When
        Photograph actualPhotograph = new Photograph(expectedPhotograph, person);

        // Then
        assertThat(actualPhotograph).isEqualToComparingFieldByField(expectedPhotograph);
    }

    @Test
    public void defaultConstructorCreatesInstanceSuccessfully() {
        // Given

        // When
        Photograph photograph = new Photograph();

        // Then
        assertThat(photograph).isNotNull();
    }

    @Test
    public void gettersAndSettersOperateOnTheSameField() {
        // Given
        Person person = new Person("person");
        byte[] photo = {};

        Photograph photograph = new Photograph(photo, person).setPosition(1);

        // When/Then
        assertThat(photograph.getHash()).isNotEmpty();
        assertThat(photograph.getPerson()).isEqualTo(person);
        assertThat(photograph.getPhoto()).isEqualTo(photo);
        assertThat(photograph.getPosition()).isEqualTo(1);
    }

    @Test
    public void toStringIsOverridden() {
        assertThat(new Photograph(new byte[]{}, new Person("userId")).toString()).contains(DomainModel.ENTITY_ATTRIBUTE_NAME_PHOTO);
    }

    @Test
    public void verifyEqualsConformsToContract() {
        EqualsVerifier.forClass(Photograph.class).
                withPrefabValues(Person.class, new Person("userid"), new Person("another-userid")).
                suppress(Warning.ALL_FIELDS_SHOULD_BE_USED).verify();
    }
}