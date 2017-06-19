package uk.co.serin.thule.email.datafactories;

import uk.co.serin.thule.email.domain.Attachment;
import uk.co.serin.thule.email.domain.Email;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestDataFactory {
    public static Attachment buildAttachment() {
        return Attachment.AttachmentBuilder.anAttachment().withContent("This is a test attachment".getBytes()).withLabel("test-attachment.txt").build();
    }

    public static Email buildEmail() {
        return Email.EmailBuilder.anEmail().
                withAttachments(Collections.singleton(buildAttachment())).
                withBccs(Collections.singleton("bcc@test.co.uk")).
                withBody("This is a test body").
                withCcs(Collections.singleton("ccs@test.co.uk")).
                withFrom("from@test.co.uk").
                withSubject("Test subject").
                withTos(Stream.of("to1@test.co.uk", "to2@test.co.uk", "to3@test.co.uk").collect(Collectors.toSet())).
                build();
    }
}