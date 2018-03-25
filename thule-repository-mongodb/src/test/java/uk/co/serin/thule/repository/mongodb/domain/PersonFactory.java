package uk.co.serin.thule.repository.mongodb.domain;

import uk.co.serin.thule.utils.utils.RandomGenerators;

import java.time.LocalDate;

public class PersonFactory {
    private static final String EMAIL_ADDRESS_SUFFIX = "@serin-consultancy.co.uk";
    private static final int SUFFIX_LENGTH = 8;

    public static Person newPerson(Person person) {
        return Person.PersonBuilder.aPerson().
                withCreatedAt(person.getCreatedAt()).
                withCreatedBy(person.getCreatedBy()).
                withDateOfBirth(person.getDateOfBirth()).
                withDateOfExpiry(person.getDateOfExpiry()).
                withDateOfPasswordExpiry(person.getDateOfPasswordExpiry()).
                withEmailAddress(person.getEmailAddress()).
                withFirstName(person.getFirstName()).
                withId(person.getId()).
                withLastName(person.getLastName()).
                withPassword(person.getPassword()).
                withSecondName(person.getSecondName()).
                withTitle(person.getTitle()).
                withUpdatedAt(person.getUpdatedAt()).
                withUpdatedBy(person.getUpdatedBy()).
                withUserId(person.getUserId()).
                withVersion(person.getVersion()).build();
    }

    public static Person newPerson() {
        // Set the attributes
        final LocalDate dob = RandomGenerators.generateUniqueRandomDateInThePast();
        final LocalDate expiryDate = RandomGenerators.generateUniqueRandomDateInTheFuture();
        String userId = "missScarlett" + RandomGenerators.generateUniqueRandomString(SUFFIX_LENGTH);

        return Person.PersonBuilder.aPerson().
                withDateOfBirth(dob).
                withDateOfExpiry(expiryDate).
                withDateOfPasswordExpiry(expiryDate).
                withEmailAddress(userId + EMAIL_ADDRESS_SUFFIX).
                withFirstName("Elizabeth").
                withId(RandomGenerators.generateUniqueRandomLong()).
                withLastName("Scarlett").
                withPassword(userId).
                withSecondName("K").
                withTitle("Miss").
                withUserId(userId).
                build();
    }
}