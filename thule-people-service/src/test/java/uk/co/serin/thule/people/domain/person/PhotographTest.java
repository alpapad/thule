package uk.co.serin.thule.people.domain.person;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import uk.co.serin.thule.people.datafactories.TestDataFactory;
import uk.co.serin.thule.people.domain.DomainModel;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PhotographTest {
    private TestDataFactory testDataFactory = new TestDataFactory();

    @Test
    public void builderAndGettersOperateOnTheSameField() {
        // Given
        Photograph expectedPhotograph = testDataFactory.newPhotographMissScarlett(testDataFactory.newPersonWithoutAnyAssociations());

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
        Photograph expectedPhotograph = testDataFactory.newPhotographMissScarlett(testDataFactory.newPersonWithoutAnyAssociations());

        // When
        Photograph actualPhotograph = new Photograph(expectedPhotograph.getPhoto(), expectedPhotograph.getPerson());
        actualPhotograph.setPosition(1);

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

    @Test
    public void toStringIsOverridden() {
        assertThat(new Photograph(new byte[]{}, new Person("userId")).toString()).contains(DomainModel.ENTITY_ATTRIBUTE_NAME_PHOTO);
    }

    @Test
    public void verifyEqualsConformsToContract() {
        EqualsVerifier.forClass(Photograph.class).
                withPrefabValues(Person.class, new Person("userid"), new Person("another-userid")).
                withOnlyTheseFields(Photograph.ENTITY_ATTRIBUTE_NAME_HASH, Photograph.ENTITY_NAME_PERSON).
                verify();
    }
}