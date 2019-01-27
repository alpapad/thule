package uk.co.serin.thule.utils.service.data.auditor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;

import uk.co.serin.thule.utils.service.oauth2.DelegatingSecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class SpringSecurityAuditorAwareTest {

    @InjectMocks
    private SpringSecurityAuditorAware sut;

    @Mock
    private Authentication authentication;

    @Mock
    private DelegatingSecurityContextHolder delegatingSecurityContextHolder;

    @Test
    public void getCurrentAuditorWithNoAuthenticationWillThrowException() {
        //Given/When
        Throwable exception = catchThrowable(() -> sut.getCurrentAuditor());

        //Then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class);
        assertThat(exception).hasMessage("Authentication is null");
    }

    @Test
    public void getCurrentAuditorWillReturnValueRetrievedFromSecurityContextHolder() {

        //Given
        String principalName = "testAuditor4534";
        given(delegatingSecurityContextHolder.getAuthentication()).willReturn(authentication);
        given(authentication.getName()).willReturn(principalName);

        //When
        Optional<String> currentAuditor = sut.getCurrentAuditor();

        //Then
        assertThat(currentAuditor).isEqualTo(Optional.of(principalName));
    }


    @Test
    public void getCurrentAuditorWithEmptyAuthenticationNameWillThrowException() {

        //Given
        String principalName = "";
        given(delegatingSecurityContextHolder.getAuthentication()).willReturn(authentication);
        given(authentication.getName()).willReturn(principalName);

        //When
        Throwable exception = catchThrowable(() -> sut.getCurrentAuditor());

        //Then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class);
        assertThat(exception).hasMessage("Principal name is empty");
    }
}