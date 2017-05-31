package uk.co.serin.thule.edge;

import com.netflix.zuul.context.RequestContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class LoggingFilterTest {
    @Mock
    private HttpServletRequest httpServletRequest;
    @InjectMocks
    private LoggingFilter loggingFilter;

    @Test
    public void filterOrderIsOne() {
        // Given

        // When
        int filterOrder = loggingFilter.filterOrder();

        //Then
        assertThat(filterOrder).isEqualTo(1);
    }

    @Test
    public void filterTypeIsPre() {
        // Given

        // When
        String filterType = loggingFilter.filterType();

        //Then
        assertThat(filterType).isEqualTo("pre");
    }

    @Test
    public void runReturnsNull() {
        // Given
        RequestContext requestContext = new RequestContext();
        RequestContext.testSetCurrentContext(requestContext);
        requestContext.setRequest(httpServletRequest);
        given(httpServletRequest.getMethod()).willReturn("method");
        given(httpServletRequest.getRequestURL()).willReturn(new StringBuffer("URL"));

        // When
        Object result = loggingFilter.run();

        //Then
        assertThat(result).isNull();
    }

    @Test
    public void shouldFilterIsTrue() {
        // Given

        // When
        boolean shouldFilter = loggingFilter.shouldFilter();

        //Then
        assertThat(shouldFilter).isEqualTo(true);
    }
}