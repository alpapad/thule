package uk.co.serin.thule.people.contract;

import com.gohenry.oauth2.Oauth2Utils;
import com.gohenry.test.assertj.ActuatorUri;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;

import org.flywaydb.core.internal.util.jdbc.JdbcUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import uk.co.serin.thule.people.datafactory.ReferenceDataFactory;
import uk.co.serin.thule.people.datafactory.RepositoryReferenceDataFactory;
import uk.co.serin.thule.people.datafactory.TestDataFactory;
import uk.co.serin.thule.people.docker.MySqlDockerContainer;
import uk.co.serin.thule.people.domain.DomainModel;
import uk.co.serin.thule.people.domain.person.Person;
import uk.co.serin.thule.people.domain.state.StateCode;
import uk.co.serin.thule.people.repository.repositories.ActionRepository;
import uk.co.serin.thule.people.repository.repositories.CountryRepository;
import uk.co.serin.thule.people.repository.repositories.PersonRepository;
import uk.co.serin.thule.people.repository.repositories.RoleRepository;
import uk.co.serin.thule.people.repository.repositories.StateRepository;

import java.net.URI;
import java.sql.Connection;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.gohenry.test.assertj.GoHenryAssertions.assertThat;
import static org.awaitility.Awaitility.given;
import static org.awaitility.pollinterval.FibonacciPollInterval.fibonacci;

@ActiveProfiles("ctest")
@AutoConfigureWireMock(port = 0)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser(username = TestDataFactory.JUNIT_TEST_USERNAME, password = TestDataFactory.JUNIT_TEST_USERNAME)
public class PeopleContractTest {
    private static final String EMAILS_PATH = "/" + DomainModel.ENTITY_NAME_EMAILS;
    private static final String ID_PATH = "/{id}";
    @Autowired
    private ActionRepository actionRepository;
    @Autowired
    private CountryRepository countryRepository;
    private String peopleServiceUrl;
    @Autowired
    private PersonRepository personRepository;
    @LocalServerPort
    private int port;
    private OAuth2RestTemplate oAuth2RestTemplate;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private StateRepository stateRepository;
    private TestDataFactory testDataFactory;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeClass
    public static void setUpClass() {
        MySqlDockerContainer.instance().startMySqlContainerIfDown();
    }

    @AfterClass
    public static void tearDownClass() {
        MySqlDockerContainer.instance().stopMySqlContainerIfup();
    }

    @Test
    public void given_a_new_person_when_finding_all_people_then_the_new_person_is_returned() {
        // Given
        Person testPerson = testDataFactory.buildPersonWithoutAnyAssociations();
        Person expectedPerson = createTestPerson(testPerson);

        // When
        ResponseEntity<Resources<Person>> personResponseEntity = oAuth2RestTemplate.exchange(peopleServiceUrl, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Resources<Person>>() {
        }, 0, 1000);

        // Then
        assertThat(personResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        Collection<Person> actualPeople = Objects.requireNonNull(personResponseEntity.getBody()).getContent();
        assertThat(actualPeople).contains(expectedPerson);
    }

    private Person createTestPerson(Person person) {
        Person testPerson = testDataFactory.buildPerson(person);
        testPerson.setState(testDataFactory.getStates().get(StateCode.PERSON_ENABLED));
        return personRepository.save(testPerson);
    }

    @Test
    public void given_a_new_person_when_finding_by_id_then_the_person_is_returned() {
        // Given
        Person testPerson = testDataFactory.buildPersonWithoutAnyAssociations();
        Person expectedPerson = createTestPerson(testPerson);

        // When
        ResponseEntity<Person> responseEntity = oAuth2RestTemplate.getForEntity(peopleServiceUrl + ID_PATH, Person.class, expectedPerson.getId());

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        Person actualPerson = responseEntity.getBody();

        assertThat(actualPerson).isEqualTo(expectedPerson);
    }

    @Before
    public void setUp() {
        // Setup test data factories
        ReferenceDataFactory referenceDataFactory = new RepositoryReferenceDataFactory(actionRepository, stateRepository, roleRepository, countryRepository);
        testDataFactory = new TestDataFactory(referenceDataFactory);

        // Set up service url
        peopleServiceUrl = String.format("http://localhost:%s/%s", port, DomainModel.ENTITY_NAME_PEOPLE);

        // Setup OAuth2
        OAuth2AccessToken jwtOauth2AccessToken = Oauth2Utils.createJwtOauth2AccessToken(
                TestDataFactory.JUNIT_TEST_USERNAME, TestDataFactory.JUNIT_TEST_USERNAME, 0, Collections.singleton(new SimpleGrantedAuthority("grantedAuthority")), "clientId", "gmjtdvNVmQRz8bzw6ae");
        oAuth2RestTemplate = new OAuth2RestTemplate(new ResourceOwnerPasswordResourceDetails(), new DefaultOAuth2ClientContext(jwtOauth2AccessToken));

        // By default the OAuth2RestTemplate does not have the full set of message converters which the TestRestTemplate has, including the ResourceResourceHttpMessageConverter required for HateOas support
        // So, add all the message converters from the TestRestTemplate
        oAuth2RestTemplate.setMessageConverters(testRestTemplate.getRestTemplate().getMessageConverters());
    }

    @Test
    public void when_checking_health_then_status_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(String.format("http://localhost:%s/actuator/health", port)));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }

    @Test
    public void when_creating_a_person_then_that_person_is_returned() {
        // Given
        Person testPerson = testDataFactory.buildPersonWithoutAnyAssociations();
        Person expectedPerson = testDataFactory.buildPerson(testPerson);

        stubFor(post(
                urlEqualTo(EMAILS_PATH)).
                withHeader(HttpHeaders.CONTENT_TYPE, containing(MediaType.APPLICATION_JSON_VALUE)).
                willReturn(aResponse().
                        withStatus(HttpStatus.ACCEPTED.value()).
                        withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).
                        withBodyFile("thule-email-service-response.json")));

        // When
        ResponseEntity<Person> responseEntity = oAuth2RestTemplate.postForEntity(peopleServiceUrl, testPerson, Person.class);

        // Then
        verify(postRequestedFor(
                urlPathEqualTo(EMAILS_PATH)).
                withHeader(HttpHeaders.CONTENT_TYPE, containing(MediaType.APPLICATION_JSON_VALUE)));

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Person actualPerson = responseEntity.getBody();

        assertThat(actualPerson.getId()).isNotNull();
        assertThat(actualPerson.getUpdatedAt()).isNotNull();
        assertThat(actualPerson.getCreatedAt()).isEqualTo(actualPerson.getUpdatedAt());
        assertThat(actualPerson.getCreatedBy()).isNotNull();
        assertThat(actualPerson.getUpdatedBy()).isNotNull();

        assertThat(actualPerson).isEqualToIgnoringGivenFields(expectedPerson,
                DomainModel.ENTITY_ATTRIBUTE_NAME_ID,
                DomainModel.ENTITY_ATTRIBUTE_NAME_CREATED_AT,
                DomainModel.ENTITY_ATTRIBUTE_NAME_CREATED_BY,
                DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_AT,
                DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY);
    }

    @Test
    public void when_deleting_a_person_then_the_person_no_longer_exists() {
        // Given
        Person person = createTestPerson(testDataFactory.buildPersonWithoutAnyAssociations());

        stubFor(
                post(urlEqualTo(EMAILS_PATH)).
                        withHeader(HttpHeaders.CONTENT_TYPE, containing(MediaType.APPLICATION_JSON_VALUE)).
                        willReturn(aResponse().
                                withStatus(HttpStatus.ACCEPTED.value()).
                                withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE).
                                withBodyFile("thule-email-service-response.json")));

        // When
        oAuth2RestTemplate.delete(peopleServiceUrl + ID_PATH, person.getId());

        // Then
        verify(postRequestedFor(urlPathEqualTo(EMAILS_PATH)).
                withHeader(HttpHeaders.CONTENT_TYPE, containing(MediaType.APPLICATION_JSON_VALUE)));

        Optional<Person> deletedPerson = personRepository.findById(person.getId());

        assertThat(deletedPerson).isNotPresent();
    }

    @Test
    public void when_updating_a_person_then_that_persons_is_updated() throws InterruptedException {
        // Given
        stubFor(post(
                urlEqualTo(EMAILS_PATH)).
                withHeader(HttpHeaders.CONTENT_TYPE, containing(MediaType.APPLICATION_JSON_VALUE)).
                willReturn(aResponse().
                        withStatus(HttpStatus.ACCEPTED.value()).
                        withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).
                        withBodyFile("thule-email-service-response.json")));

        Person testPerson = testDataFactory.buildPersonWithoutAnyAssociations();
        testPerson = createTestPerson(testPerson);
        long id = testPerson.getId();

        testPerson.setFirstName("updatedFirstName");
        testPerson.setSecondName("updatedSecondName");
        testPerson.setLastName("updatedLastName");
        testPerson.setDateOfBirth(testPerson.getDateOfBirth().minusDays(1));
        testPerson.setEmailAddress("updated@serin-consultancy.co.uk");
        testPerson.setPassword("updatedPassword");
        testPerson.setState(null);

        Person expectedPerson = testDataFactory.buildPerson(testPerson);
        expectedPerson.setState(null);
        ReflectionTestUtils.setField(expectedPerson, DomainModel.ENTITY_ATTRIBUTE_NAME_VERSION, null);

        Thread.sleep(1000); // Allow enough time to lapse for the updatedAt to be updated with a different value

        // When
        oAuth2RestTemplate.put(peopleServiceUrl + ID_PATH, testPerson, id);

        // Then
        verify(postRequestedFor(urlPathEqualTo(EMAILS_PATH)).
                withHeader(HttpHeaders.CONTENT_TYPE, containing(MediaType.APPLICATION_JSON_VALUE)));

        Person actualPerson = oAuth2RestTemplate.getForObject(peopleServiceUrl + ID_PATH, Person.class, id);

        assertThat(actualPerson.getUpdatedAt()).isAfter(expectedPerson.getUpdatedAt());
        assertThat(actualPerson.getUpdatedBy()).isNotEmpty();

        assertThat(actualPerson).isEqualToIgnoringGivenFields(expectedPerson,
                DomainModel.ENTITY_ATTRIBUTE_NAME_CREATED_AT,
                DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_AT,
                DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY);
    }

    @TestConfiguration
    static class BankTransferRequestIntTestConfiguration {
        @Value("${wiremock.server.port}")
        private int wireMockServerPort;

        @Bean
        public FlywayMigrationStrategy flywayMigrationStrategy() {
            return flyway -> {
                // Wait until the database is available because otherwise flyway migrate will fail
                // resulting in the application context not loading
                given().ignoreExceptions().pollInterval(fibonacci()).
                        await().timeout(org.awaitility.Duration.FIVE_MINUTES).
                        untilAsserted(() -> {
                            Connection connection = JdbcUtils.openConnection(flyway.getDataSource());
                            JdbcUtils.closeConnection(connection);
                        });
                flyway.migrate();
            };
        }

        /**
         * When using a Feign client, it will try to use the load balancer (Ribbon) to lookup the
         * service via a discovery service (Eureka). However, for the integration test, we don't use
         * Ribbon or Eureka so we need to tell Feign to use a static server, in this case Wiremock.
         */
        @Bean
        public ServerList<Server> ribbonServerList() {
            return new StaticServerList<>(new Server("localhost", wireMockServerPort));
        }

    }
}
