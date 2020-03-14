package uk.co.serin.thule.authentication.keycloak;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.spring5.SpringTemplateEngine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class KeycloakResourceInstallerTest {
    @Mock
    private KeycloakRepository keycloakRepository;
    @Mock
    private SpringTemplateEngine springTemplateEngine;
    @InjectMocks
    private KeycloakResourceInstaller sut;

    @Test
    public void given_existing_path_when_run_then_file_is_created() {
        // Given
        ReflectionTestUtils.setField(sut, "secretsFile", "build/thule-keycloak-secrets.yml");

        given(keycloakRepository.getClientSecret(any())).willReturn("client-secret");
        given(springTemplateEngine.process(anyString(), any())).willReturn("thule-secrets");
        given(keycloakRepository.createUser("thule@serin-consultancy.co.uk", "thule", "Project", "Thule")).willReturn("12345678");

        // When
        sut.run();

        // Then
        verify(keycloakRepository).createRealm();
        verify(keycloakRepository).createServiceClient("thule-gateway-service");
        verify(keycloakRepository).createRoleForClient("USER", "thule-gateway-service");
        verify(keycloakRepository).createServiceClient("thule-people-service");
        verify(keycloakRepository).createRoleForClient("USER", "thule-people-service");
        verify(keycloakRepository).createPublicClient("thule-webapp");
        verify(keycloakRepository).createRoleForClient("USER", "thule-webapp");
        verify(keycloakRepository).createUserRoleMapping("12345678", "thule-gateway-service", "USER");
        verify(keycloakRepository).createUserRoleMapping("12345678", "thule-people-service", "USER");
        verify(keycloakRepository).createUserRoleMapping("12345678", "thule-webapp", "USER");
    }

    @Test
    public void given_missing_filename_when_run_then_illegalstateexception_is_thrown() {
        // Given
        ReflectionTestUtils.setField(sut, "secretsFile", "build");

        given(keycloakRepository.getClientSecret(any())).willReturn("client-secret");
        given(springTemplateEngine.process(anyString(), any())).willReturn("thule-secrets");
        given(keycloakRepository.createUser("thule@serin-consultancy.co.uk", "thule", "Project", "Thule")).willReturn("12345678");

        // When
        var illegalStateException = catchThrowableOfType(() -> sut.run(), IllegalStateException.class);

        // Then
        assertThat(illegalStateException).isNotNull();
    }
}