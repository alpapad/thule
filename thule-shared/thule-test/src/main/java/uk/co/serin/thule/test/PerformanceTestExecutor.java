package uk.co.serin.thule.test;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import uk.co.serin.thule.utils.oauth2.Oauth2Utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class PerformanceTestExecutor {
    private static final Object STATS_UPDATE_MONITOR_LOCK = new Object();
    private static Logger logger = LoggerFactory.getLogger(PerformanceTestExecutor.class);
    private Duration elapsedTimeOfAllExecutions = Duration.ZERO;
    private Duration elapsedTimeOfAllExecutionsSinceLastLogMessage = Duration.ZERO;
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private LocalDateTime lastLogMessageTime;
    private Long numberOfExecutionsCompleted = 0L;
    private Long numberOfExecutionsCompletedSinceLastLogMessage = 0L;
    private int numberOfThreads = 10;
    private LocalDateTime performanceTestFinishTime;
    private LocalDateTime performanceTestStartTime;
    private Future statisticsLogger;
    private Duration statisticsLoggingInterval = Duration.ofSeconds(10);
    private Duration timeLimit = Duration.ofMinutes(1);

    public OAuth2RestTemplate createOAuth2RestTemplate(long userId) {
        OAuth2AccessToken jwtOauth2AccessToken = Oauth2Utils.createJwtOauth2AccessToken(
                "username", "password", userId, Collections.singleton(new SimpleGrantedAuthority("grantedAuthority")), "clientId",
                "gmjtdvNVmQRz8bzw6ae");

        return new OAuth2RestTemplate(new ResourceOwnerPasswordResourceDetails(), new DefaultOAuth2ClientContext(jwtOauth2AccessToken));
    }

    public void executeConcurrentThreads(PerformanceTest performanceTest) {
        initialiseTimes();
        startStatisticsLogging();
        executePerformanceTestInEachThread(performanceTest);
        waitForThreadsToFinish();
        logStatistics();
    }

    private void initialiseTimes() {
        performanceTestStartTime = LocalDateTime.now();
        performanceTestFinishTime = performanceTestStartTime.plus(timeLimit);
    }

    private void startStatisticsLogging() {
        lastLogMessageTime = LocalDateTime.now();

        var logStatisticsExecutorService = Executors.newScheduledThreadPool(1);
        Runnable logStatistics = this::logStatistics;
        var initialDelay = 0;

        statisticsLogger =
                logStatisticsExecutorService.scheduleAtFixedRate(logStatistics, initialDelay, statisticsLoggingInterval.toMillis(), TimeUnit.MILLISECONDS);
    }

    private void executePerformanceTestInEachThread(PerformanceTest performanceTest) {
        for (var threadCount = 0; threadCount < numberOfThreads; threadCount++) {
            executorService.execute(createRunnable(performanceTest));
        }
    }

    private void waitForThreadsToFinish() {
        executorService.shutdown();
        try {
            executorService.awaitTermination(timeLimit.toMinutes() + 1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        statisticsLogger.cancel(true);
    }

    private void logStatistics() {
        synchronized (STATS_UPDATE_MONITOR_LOCK) {
            var averageResponseTimeInMillis = elapsedTimeOfAllExecutions.toMillis() / Math.max(1, numberOfExecutionsCompleted);
            logger.trace("Average response times (since start of test): {} requests averaging {} ms", numberOfExecutionsCompleted, averageResponseTimeInMillis);

            var elapsedTimeInSecondsSinceStartOfTest = Duration.between(performanceTestStartTime, LocalDateTime.now()).getSeconds();
            var processRatePerSecond = (double) numberOfExecutionsCompleted / Math.max(1, elapsedTimeInSecondsSinceStartOfTest);
            String processingRatePerSecondFormatted = String.format("%.2f", processRatePerSecond);
            logger.trace("Average processing rate (since start of test): {} per second", processingRatePerSecondFormatted);

            var averageResponseTimeInMillisSinceLastLogMessage =
                    elapsedTimeOfAllExecutionsSinceLastLogMessage.toMillis() / Math.max(1, numberOfExecutionsCompletedSinceLastLogMessage);
            logger.trace("Average response times (in the last {} seconds): {} requests averaging {} ms", this.statisticsLoggingInterval.toSeconds(),
                    numberOfExecutionsCompletedSinceLastLogMessage, averageResponseTimeInMillisSinceLastLogMessage);

            var elapsedTimeInSecondsSinceLastLogMessage = Duration.between(lastLogMessageTime, LocalDateTime.now()).getSeconds();
            var processRatePerSecondSinceLastLogMessage =
                    (double) numberOfExecutionsCompletedSinceLastLogMessage / Math.max(1, elapsedTimeInSecondsSinceLastLogMessage);
            String processingRatePerSecondFormattedSinceLastLogMessage = String.format("%.2f", processRatePerSecondSinceLastLogMessage);

            logger.trace("Average processing rate (in the last {} seconds): {} per second\n", this.statisticsLoggingInterval.toSeconds(),
                    processingRatePerSecondFormattedSinceLastLogMessage);

            lastLogMessageTime = LocalDateTime.now();
            numberOfExecutionsCompletedSinceLastLogMessage = 0L;
            elapsedTimeOfAllExecutionsSinceLastLogMessage = Duration.ZERO;
        }
    }

    private Runnable createRunnable(PerformanceTest performanceTest) {
        return () -> {
            while (performanceTestFinishTime.isAfter(LocalDateTime.now())) {
                var startTime = LocalDateTime.now();
                try {
                    performanceTest.run();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
                var finishTime = LocalDateTime.now();

                var performanceTestDuration = Duration.between(startTime, finishTime);
                updateStatistics(performanceTestDuration);
            }
        };
    }

    private void updateStatistics(Duration elapsedDuration) {
        synchronized (STATS_UPDATE_MONITOR_LOCK) {
            var durationInMillis = elapsedDuration.toMillis();
            numberOfExecutionsCompleted++;
            numberOfExecutionsCompletedSinceLastLogMessage++;
            elapsedTimeOfAllExecutions = elapsedTimeOfAllExecutions.plusMillis(durationInMillis);
            elapsedTimeOfAllExecutionsSinceLastLogMessage = elapsedTimeOfAllExecutionsSinceLastLogMessage.plusMillis(durationInMillis);
        }
    }

    public void setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    public void setStatisticsLoggingInterval(Duration statisticsLoggingInterval) {
        this.statisticsLoggingInterval = statisticsLoggingInterval;
    }

    public void setTimeLimit(Duration timeLimit) {
        this.timeLimit = timeLimit;
    }

    @FunctionalInterface
    public interface PerformanceTest {
        void run();
    }
}