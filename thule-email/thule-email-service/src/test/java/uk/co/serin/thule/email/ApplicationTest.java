package uk.co.serin.thule.email;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationTest {
    @Mock
    private SpringApplication springApplication;

    @Test
    public void when_application_starts_spring_boot_then_no_exception_is_thrown() {
        // Given
        var args = new String[0];
        ReflectionTestUtils.setField(Application.class, "springApplication", springApplication);

        given(springApplication.run(args)).willReturn(new StaticApplicationContext());

        // When
        var throwable = catchThrowable(() -> Application.main(args));

        // Then
        assertThat(throwable).isNull();
    }

    @Test
    public void when_default_constructor_is_invoked_then_an_instance_is_instantiated() {
        assertThat(BeanUtils.instantiateClass(Application.class)).isNotNull();
    }
}