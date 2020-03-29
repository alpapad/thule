package uk.co.serin.thule.people;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import uk.co.serin.thule.utils.utils.ClassUtils;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ApplicationConfigurerTest {
    @InjectMocks
    private ApplicationConfigurer sut;

    @Test
    public void when_accessing_class_static_initializer_then_context_holder_strategy_is_inheritable_thread_local() {
        // When
        ClassUtils.forName(ApplicationConfigurer.class.getName());

        // Then
        var contextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
        assertThat(contextHolderStrategy.getClass().getName())
                .isEqualTo("org.springframework.security.core.context.InheritableThreadLocalSecurityContextHolderStrategy");
    }
}