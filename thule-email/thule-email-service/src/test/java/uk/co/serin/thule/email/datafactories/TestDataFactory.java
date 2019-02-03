package uk.co.serin.thule.email.datafactories;

import uk.co.serin.thule.email.domain.Attachment;
import uk.co.serin.thule.email.domain.Email;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestDataFactory {
    public static Email buildEmail() {
        return Email.builder().
                attachments(Collections.singleton(Attachment.builder().content("This is a test attachment").label("test-attachment.txt").build())).
                bccs(Collections.singleton("bcc@test.co.uk")).
                body("This is a test body").
                ccs(Collections.singleton("ccs@test.co.uk")).
                from("from@test.co.uk").
                subject("Test subject").
                tos(Stream.of("to1@test.co.uk", "to2@test.co.uk", "to3@test.co.uk").collect(Collectors.toSet())).
                build();
    }
}