package uk.co.serin.thule.utils.oauth2;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAuthenticationDetailsTest {

    @Test
    public void when_user_authentication_details_is_created_via_constructor_then_return_object() {
        // Given

        // When
        UserAuthenticationDetails sut = new UserAuthenticationDetails(999);

        // Then
        assertThat(sut.getUserId()).isEqualTo(999);
    }

    @Test
    public void when_user_authentication_details_is_modified_then_change_is_shown() {
        // Given
        UserAuthenticationDetails sut = new UserAuthenticationDetails(999);

        // When
        sut.setUserId(888);

        // Then
        assertThat(sut.getUserId()).isEqualTo(888);
    }
}
