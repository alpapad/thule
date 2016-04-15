package uk.co.serin.thule.people.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import uk.co.serin.thule.people.domain.Audit;
import uk.co.serin.thule.people.domain.DomainModel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Audit.class)
public class DomainModelTest {
    @Mock
    private Audit audit;

    @Test
    public void gettersAndSettersOperateOnTheSameField() {
        // Given
        Audit audit = new Audit();

        DomainModel domainModel = new DomainModel() {
            private static final long serialVersionUID = -5732896444138835908L;
        };
        domainModel.setId(1L);
        domainModel.setVersion(1L);
        ReflectionTestUtils.setField(domainModel, DomainModel.ENTITY_ATTRIBUTE_NAME_AUDIT, audit);

        // When/then
        assertThat(domainModel.getAudit()).isEqualTo(audit);
        assertThat(domainModel.getId()).isEqualTo(1L);
        assertThat(domainModel.getVersion()).isEqualTo(1L);
    }

    @Test
    public void prePersistInitialisesAudit() {
        // Given
        DomainModel domainModel = new DomainModel() {
            private static final long serialVersionUID = 3430896183842157405L;
        };
        ReflectionTestUtils.setField(domainModel, DomainModel.ENTITY_ATTRIBUTE_NAME_AUDIT, audit);
        mock(Audit.class);

        // When
        domainModel.prePersist();

        // Then
        verify(audit).initialise();
    }

    @Test
    public void preUpdateUpdatesAudit() {
        // Given
        DomainModel domainModel = new DomainModel() {
            private static final long serialVersionUID = 5480441630947100568L;
        };
        ReflectionTestUtils.setField(domainModel, DomainModel.ENTITY_ATTRIBUTE_NAME_AUDIT, audit);
        mock(Audit.class);

        // When
        domainModel.preUpdate();

        // Then
        verify(audit).update();
    }

    @Test
    public void toStringIsOverridden() {
        assertThat(new DomainModel() {
            private static final long serialVersionUID = -7917846140115672916L;
        }.toString()).contains(DomainModel.ENTITY_ATTRIBUTE_NAME_ID);
    }
}
