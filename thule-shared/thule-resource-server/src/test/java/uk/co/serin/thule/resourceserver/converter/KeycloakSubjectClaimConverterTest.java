package uk.co.serin.thule.resourceserver.converter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.co.serin.thule.resourceserver.converter.KeycloakSubjectClaimConverter;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class KeycloakSubjectClaimConverterTest {
    public static final String USER_NAME = "user";
    @InjectMocks
    private KeycloakSubjectClaimConverter sut;

    @Test
    void given_claims_with_keycloak_subject_name_when_convert_claims_then_claims_contain_standard_subject_name() {
        // Given
        var claims = Map.<String, Object>of("preferred_username", USER_NAME);

        // When
        var convertedClaims = sut.convert(claims);

        // Then
        assertThat(convertedClaims).containsEntry("sub", USER_NAME);
    }
}