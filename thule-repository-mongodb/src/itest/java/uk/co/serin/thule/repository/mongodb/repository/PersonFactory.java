package uk.co.serin.thule.repository.mongodb.repository;


import uk.co.serin.thule.repository.mongodb.domain.PersonEntity;
import uk.co.serin.thule.utils.utils.RandomUtils;

public class PersonFactory {
    private static final String EMAIL_ADDRESS_SUFFIX = "@serin-consultancy.co.uk";
    private static final int SUFFIX_LENGTH = 8;

    public static PersonEntity newPerson(PersonEntity personEntity) {
        return PersonEntity.builder().createdAt(personEntity.getCreatedAt()).createdBy(personEntity.getCreatedBy()).dateOfBirth(personEntity.getDateOfBirth())
                           .dateOfExpiry(personEntity.getDateOfExpiry()).dateOfPasswordExpiry(personEntity.getDateOfPasswordExpiry())
                           .emailAddress(personEntity.getEmailAddress()).firstName(personEntity.getFirstName()).id(personEntity.getId())
                           .lastName(personEntity.getLastName()).password(personEntity.getPassword()).secondName(personEntity.getSecondName())
                           .title(personEntity.getTitle()).updatedAt(personEntity.getUpdatedAt()).updatedBy(personEntity.getUpdatedBy())
                           .userId(personEntity.getUserId()).version(personEntity.getVersion()).build();
    }

    public static PersonEntity newPerson() {
        var dob = RandomUtils.generateUniqueRandomDateInThePast();
        var expiryDate = RandomUtils.generateUniqueRandomDateInTheFuture();
        var userId = "missScarlett" + RandomUtils.generateUniqueRandomString(SUFFIX_LENGTH);

        return PersonEntity.builder().dateOfBirth(dob).dateOfExpiry(expiryDate).dateOfPasswordExpiry(expiryDate).emailAddress(userId + EMAIL_ADDRESS_SUFFIX)
                           .firstName("Elizabeth").id(RandomUtils.generateUniqueRandomLong()).lastName("Scarlett").password(userId).secondName("K").title("Miss")
                           .userId(userId).build();
    }
}