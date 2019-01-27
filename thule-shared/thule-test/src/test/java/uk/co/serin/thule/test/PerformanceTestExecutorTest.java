package uk.co.serin.thule.test;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PerformanceTestExecutorTest {
    @Spy
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private String logMessage = "This is a unit test of PerformanceTestExecutor";
    @Mock
    private Logger logger;
    @InjectMocks
    private PerformanceTestExecutor sut;

    @Test
    public void given_a_userid_a_oauth2resttemplate_is_returned() {
        // Given
        long userId = 12345678L;

        // When
        OAuth2RestTemplate oAuth2RestTemplate = sut.createOAuth2RestTemplate(userId);

        // Then
        assertThat(oAuth2RestTemplate).isNotNull();
    }

    @After
    public void tearDown() {
        ReflectionTestUtils.setField(PerformanceTestExecutor.class, "logger", LoggerFactory.getLogger(PerformanceTestExecutor.class));
    }

    @Test
    public void when_a_performance_test_throws_an_exception_then_the_exception_is_logged() {
        // Given
        sut.setNumberOfThreads(1);
        sut.setTimeLimit(Duration.ofMillis(500));
        sut.setStatisticsLoggingInterval(Duration.ofMillis(10));

        String exceptionMessage = "Performance Test Has Failed!";
        IllegalStateException illegalStateException = new IllegalStateException(exceptionMessage);

        ReflectionTestUtils.setField(sut, "logger", logger);

        // When
        sut.executeConcurrentThreads(() -> {
            throw illegalStateException;
        });

        // Then
        verify(logger, atLeastOnce()).error(exceptionMessage, illegalStateException);
    }

    @Test
    public void when_executing_then_the_performance_test_executes_the_required_test() {
        // Given
        sut.setNumberOfThreads(1);
        sut.setTimeLimit(Duration.ofMillis(100));

        // When
        sut.executeConcurrentThreads(performanceTest());

        // Then
        verify(logger, atLeastOnce()).trace(logMessage);
    }

    private PerformanceTestExecutor.PerformanceTest performanceTest() {
        return () -> logger.trace(logMessage);
    }

    @Test
    public void when_execution_service_termination_is_interrupted_then_thread_is_interrupted_without_an_exception() throws InterruptedException {
        // Given
        sut.setNumberOfThreads(1);
        sut.setTimeLimit(Duration.ofMillis(100));

        ReflectionTestUtils.setField(sut, "executorService", executorService);

        doThrow(InterruptedException.class).when(executorService).awaitTermination(1L, TimeUnit.MINUTES);

        // When
        sut.executeConcurrentThreads(performanceTest());

        // Then
        assertThat(Thread.currentThread().interrupted()).isTrue();
    }
}