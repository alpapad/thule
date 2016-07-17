package uk.co.serin.thule.people.domain;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.After;
import org.junit.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class AuditTest {

    @Test
    public void copyConstructorCreatesInstanceWithSameFieldValues() {
        // Given
        Audit expectedAudit = new Audit();

        // When
        Audit actualAudit = new Audit(expectedAudit);

        // Then
        assertThat(actualAudit).isEqualToComparingFieldByField(expectedAudit);
    }

    @Test
    public void defaultConstructorCreatesInstanceSuccessfully() {
        // Given

        // When
        Audit audit = new Audit();

        // Then
        assertThat(audit).isNotNull();
    }


    @Test
    public void gettersAndSettersOperateOnTheSameField() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        String updatedBy = "updatedBy";

        Audit audit = new Audit();
        audit.setCreatedAt(now);
        audit.setUpdatedAt(now);
        audit.setUpdatedBy(updatedBy);

        // When/Then
        assertThat(audit.getCreatedAt()).isEqualTo(now);
        assertThat(audit.getUpdatedAt()).isEqualTo(now);
        assertThat(audit.getUpdatedBy()).isEqualTo(updatedBy);
    }

    @After
    public void tearDown() {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof CredentialsContainer) {
            CredentialsContainer.class.cast(SecurityContextHolder.getContext().getAuthentication()).eraseCredentials();
        }
    }

    @Test
    public void toStringIsOverridden() {
        assertThat(new Audit().toString()).contains(Audit.ENTITY_ATTRIBUTE_NAME_UPDATED_BY);
    }

    @Test
    public void updateAuditUpdatesJustUpdatedAt() throws InterruptedException {
        // Given
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("JUnitTest", "password"));

        Audit expectedAudit = new Audit();
        expectedAudit.initialise();

        Audit actualAudit = new Audit(expectedAudit);
        // Using Awaitility.await() rather than Thread.sleep() to overcome SonarQube code smell Remove this use of "Thread.sleep()"
        await().timeout(10, TimeUnit.SECONDS); // Allow enough time to lapse for the updatedAt to be updated with a different value.

        // When
        actualAudit.update();

        // Then
        assertThat(actualAudit.getUpdatedAt()).isAfter(expectedAudit.getUpdatedAt());
        assertThat(actualAudit).isEqualToIgnoringGivenFields(expectedAudit, Audit.ENTITY_ATTRIBUTE_NAME_UPDATED_AT);
    }

    @Test
    public void verifyEqualsConformsToContract() {
        EqualsVerifier.forClass(Audit.class).verify();
    }
}
