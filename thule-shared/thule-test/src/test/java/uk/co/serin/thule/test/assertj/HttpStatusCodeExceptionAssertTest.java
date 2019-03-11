package uk.co.serin.thule.test.assertj;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.HttpStatusCodeException;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class HttpStatusCodeExceptionAssertTest {

    @InjectMocks
    private HttpStatusCodeExceptionAssert sut;
    @Mock
    private HttpStatusCodeException assertedInstance;

    @Test
    public void when_asserting_matching_message_error_attribute_then_it_succeeds() {

        // Given
        var messageErrorAttribute = "hewkwhf";
        given(assertedInstance.getResponseBodyAsString()).willReturn(
                "{\"message\" : \"" + messageErrorAttribute + "\"}");

        // When
        sut.hasMessageErrorAttribute(messageErrorAttribute);
    }

    @Test
    public void when_asserting_unmatched_message_error_attribute_then_assertion_exception_is_thrown() {

        // Given
        var messageErrorAttribute = "hewkwhf";
        given(assertedInstance.getResponseBodyAsString()).willReturn(
                "{\"message\" : \"" + messageErrorAttribute + "\"}");

        // When
        var thrown = catchThrowable(() -> sut.hasMessageErrorAttribute("some other"));
        Assertions.assertThat(thrown).isInstanceOf(AssertionError.class);
    }

    @Test
    public void when_asserting_message_error_from_incorrectly_formed_response_json_body_then_runtime_exception_is_thrown() {

        // Given
        given(assertedInstance.getResponseBodyAsString()).willReturn("{");

        // When
        var thrown = catchThrowable(() -> sut.hasMessageErrorAttribute("some other"));
        Assertions.assertThat(thrown).isExactlyInstanceOf(IllegalStateException.class);
    }
}