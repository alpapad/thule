package uk.co.serin.thule.people.contract;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;

import org.awaitility.Duration;
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
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
import uk.co.serin.thule.test.assertj.ActuatorUri;

import java.net.URI;
import java.sql.Connection;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.awaitility.Awaitility.given;
import static org.awaitility.pollinterval.FibonacciPollInterval.fibonacci;
import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@ActiveProfiles("ctest")
@AutoConfigureWireMock(port = 0)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser(username=TestDataFactory.JUNIT_TEST_USERNAME, password = TestDataFactory.JUNIT_TEST_USERNAME)
public class PeopleContractTest {
    private static final String ACTUATOR_HEALTH_PATH = "/actuator/health";
    private static final String EMAILS_PATH = "/" + DomainModel.ENTITY_NAME_EMAILS;
    private static final String ID_PATH = "/{id}";
    private static final String PEOPLE_PATH = "/" + DomainModel.ENTITY_NAME_PEOPLE;
    @Autowired
    private ActionRepository actionRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private StateRepository stateRepository;
    private TestDataFactory testDataFactory;

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
        ResponseEntity<Resources<Person>> personResponseEntity
                = restTemplate.exchange(PEOPLE_PATH + "?page={page}&size={size}", HttpMethod.GET, null, new ParameterizedTypeReference<Resources<Person>>() {
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
        ResponseEntity<Person> responseEntity = restTemplate.getForEntity(PEOPLE_PATH + ID_PATH, Person.class, expectedPerson.getId());

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        Person actualPerson = responseEntity.getBody();

        assertThat(actualPerson).isEqualTo(expectedPerson);
    }

    @Before
    public void setUp() throws Throwable {
        ReferenceDataFactory referenceDataFactory = new RepositoryReferenceDataFactory(actionRepository, stateRepository, roleRepository, countryRepository);
        testDataFactory = new TestDataFactory(referenceDataFactory);

        // Create restTemplate with Basic Authentication security
        restTemplate = restTemplate.withBasicAuth(TestDataFactory.JUNIT_TEST_USERNAME, TestDataFactory.JUNIT_TEST_USERNAME);

        // Create new OjectMapper to create an "excludePasswordFilter" which does not exclude the password so it is serialized into Json for test purposes
        ObjectMapper objectMapper =
                restTemplate.getRestTemplate().getMessageConverters().stream() // Get all existing MessageConverters
                        .filter(httpMessageConverter -> httpMessageConverter instanceof MappingJackson2HttpMessageConverter) // Filter out everything that is not a MappingJackson2HttpMessageConverter
                        .map(MappingJackson2HttpMessageConverter.class::cast) // Cast all to a MappingJackson2HttpMessageConverter
                        .findFirst() // Find the first MappingJackson2HttpMessageConverter
                        .orElseThrow((Supplier<Throwable>) () -> new IllegalStateException("RestTemplate does not contain any MappingJackson2HttpMessageConverter converters!")) // Throw an exception if there aren't any MappingJackson2HttpMessageConverters
                        .getObjectMapper().copy() // Copy the ObjectMapper
                        .setFilterProvider(new SimpleFilterProvider().addFilter(Person.EXCLUDE_CREDENTIALS_FILTER, SimpleBeanPropertyFilter.serializeAll())); // Set the excludePasswordFilter

        restTemplate.getRestTemplate().setMessageConverters(Collections.singletonList(new MappingJackson2HttpMessageConverter(objectMapper)));
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

        Person expectedPerson = testDataFactory.buildPerson(testPerson);
        expectedPerson.setState(null);
        ReflectionTestUtils.setField(expectedPerson, DomainModel.ENTITY_ATTRIBUTE_NAME_VERSION, null);

        Thread.sleep(1000); // Allow enough time to lapse for the updatedAt to be updated with a different value

        // When
        restTemplate.put(PEOPLE_PATH + ID_PATH, testPerson, id);

        // Then
        verify(postRequestedFor(urlPathEqualTo(EMAILS_PATH)).
                withHeader(HttpHeaders.CONTENT_TYPE, containing(MediaType.APPLICATION_JSON_VALUE)));

        Person actualPerson = restTemplate.getForObject(PEOPLE_PATH + ID_PATH, Person.class, id);

        assertThat(actualPerson.getUpdatedAt()).isAfter(expectedPerson.getUpdatedAt());
        assertThat(actualPerson.getUpdatedBy()).isNotEmpty();

        assertThat(actualPerson).isEqualToIgnoringGivenFields(expectedPerson,
                DomainModel.ENTITY_ATTRIBUTE_NAME_CREATED_AT,
                DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_AT,
                DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY);
    }

    @Test
    public void when_checking_health_then_status_up() {
        // Given
        ActuatorUri actuatorUri = new ActuatorUri(URI.create(restTemplate.getRootUri() + ACTUATOR_HEALTH_PATH));

        // When/Then
        assertThat(actuatorUri).withHttpBasic("user", "user").waitingForMaximum(java.time.Duration.ofMinutes(5)).hasStatus(Status.UP);
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
        ResponseEntity<Person> responseEntity = restTemplate.postForEntity(PEOPLE_PATH, testPerson, Person.class);

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
        restTemplate.delete(PEOPLE_PATH + ID_PATH, person.getId());

        // Then
        verify(postRequestedFor(urlPathEqualTo(EMAILS_PATH)).
                withHeader(HttpHeaders.CONTENT_TYPE, containing(MediaType.APPLICATION_JSON_VALUE)));

        Optional<Person> deletedPerson = personRepository.findById(person.getId());

        assertThat(deletedPerson).isNotPresent();
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
                        await().timeout(Duration.FIVE_MINUTES).
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
