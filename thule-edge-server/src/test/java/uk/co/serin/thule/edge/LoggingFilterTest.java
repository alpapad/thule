package uk.co.serin.thule.edge;

import com.netflix.zuul.context.RequestContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class LoggingFilterTest {
    private LoggingFilter loggingFilter = new LoggingFilter();
    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private RequestContext requestContext;

    @Test
    public void filterTypeIsPre() {
        // Given

        // When
        String filterType = loggingFilter.filterType();

        //Then
        assertThat(filterType).isEqualTo("pre");
    }

    @Test
    public void filterOrderIsOne() {
        // Given

        // When
        int filterOrder = loggingFilter.filterOrder();

        //Then
        assertThat(filterOrder).isEqualTo(1);
    }

    @Test
    public void shouldFilterIsTrue() {
        // Given

        // When
        boolean shouldFilter = loggingFilter.shouldFilter();

        //Then
        assertThat(shouldFilter).isEqualTo(true);
    }

    @Test
    public void runReturnsNull() {
        // Given
        ReflectionTestUtils.setField(RequestContext.class, "testContext", requestContext);
        given(requestContext.getRequest()).willReturn(httpServletRequest);
        given(httpServletRequest.getMethod()).willReturn("method");
        given(httpServletRequest.getRequestURL()).willReturn(new StringBuffer("URL"));

        // When
        Object result = loggingFilter.run();

        //Then
        assertThat(result).isNull();
    }
}