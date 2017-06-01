package uk.co.serin.thule.email.datafactories;

import uk.co.serin.thule.email.domain.Attachment;
import uk.co.serin.thule.email.domain.Email;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestDataFactory {
    public static Attachment buildAttachment() {
        return Attachment.AttachmentBuilder.anAttachment().withContent("content".getBytes()).withLabel("label").build();
    }

    public static Email buildEmail() {
        Attachment attachment = new Attachment("content".getBytes(), "label");

        return Email.EmailBuilder.anEmail().
                withAttachments(Collections.singleton(attachment)).
                withBccs(Collections.singleton("bcc@test.co.uk")).
                withBody("body").
                withCcs(Collections.singleton("ccs@test.co.uk")).
                withFrom("from@test.co.uk").
                withSubject("subject").
                withTos(Stream.of("to1@test.co.uk", "to2@test.co.uk", "to3@test.co.uk").collect(Collectors.toSet())).
                build();
    }
}