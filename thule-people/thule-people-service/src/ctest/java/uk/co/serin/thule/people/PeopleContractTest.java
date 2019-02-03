package uk.co.serin.thule.people;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.rest.webmvc.support.ETag;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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
import uk.co.serin.thule.utils.oauth2.Oauth2Utils;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Objects;

import javax.persistence.EntityManager;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.springframework.util.StringUtils.trimLeadingCharacter;
import static org.springframework.util.StringUtils.trimTrailingCharacter;
import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@ActiveProfiles("ctest")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureWireMock(port = 0)
@Import(PeopleContractTestConfiguration.class)
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
    @Autowired
    private EntityManager entityManager;
    private OAuth2RestTemplate oAuth2RestTemplate;
    private String peopleServiceUrl;
    @Autowired
    private PersonRepository personRepository;
    @LocalServerPort
    private int port;
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
        var testPerson = createAndPersistPersonWithNoAssociations();

        // When
        var personResponseEntity =
                oAuth2RestTemplate.exchange(peopleServiceUrl, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Resources<Person>>() {
                }, 0, 1000);

        // Then
        assertThat(personResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        var actualPeople = Objects.requireNonNull(personResponseEntity.getBody()).getContent();
        assertThat(actualPeople).contains(testPerson);
    }

    private Person createAndPersistPersonWithNoAssociations() {
        var person = personRepository.saveAndFlush(testDataFactory.buildPersonWithState());
        entityManager.clear();
        person.setState(null);
        return person;
    }

    @Test
    public void given_a_new_person_when_finding_by_id_then_the_person_is_returned() {
        // Given
        var testPerson = createAndPersistPersonWithNoAssociations();

        // When
        var responseEntity = oAuth2RestTemplate.getForEntity(peopleServiceUrl + ID_PATH, Person.class, testPerson.getId());

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        var actualPerson = responseEntity.getBody();

        assertThat(actualPerson).isEqualTo(testPerson);
    }

    @Before
    public void setUp() {
        // Setup test data factories
        var referenceDataFactory = new RepositoryReferenceDataFactory(actionRepository, stateRepository, roleRepository, countryRepository);
        testDataFactory = new TestDataFactory(referenceDataFactory);

        // Set up service url
        peopleServiceUrl = String.format("http://localhost:%s/%s", port, DomainModel.ENTITY_NAME_PEOPLE);

        // Setup OAuth2
        var jwtOauth2AccessToken = Oauth2Utils.createJwtOauth2AccessToken(TestDataFactory.JUNIT_TEST_USERNAME, TestDataFactory.JUNIT_TEST_USERNAME, 0,
                Collections.singleton(new SimpleGrantedAuthority("grantedAuthority")), "clientId", "gmjtdvNVmQRz8bzw6ae");
        oAuth2RestTemplate = new OAuth2RestTemplate(new ResourceOwnerPasswordResourceDetails(), new DefaultOAuth2ClientContext(jwtOauth2AccessToken));

        // By default the OAuth2RestTemplate does not have the full set of message converters which the TestRestTemplate has, including the ResourceResourceHttpMessageConverter required for HateOAS support
        // So, add all the message converters from the TestRestTemplate
        oAuth2RestTemplate.setMessageConverters(testRestTemplate.getRestTemplate().getMessageConverters());
    }

    @Test
    public void when_checking_health_then_status_up() {
        // Given
        var actuatorUri = new ActuatorUri(URI.create(String.format("http://localhost:%s/actuator/health", port)));

        // When/Then
        assertThat(actuatorUri).waitingForMaximum(Duration.ofMinutes(5)).hasHealthStatus(Status.UP);
    }

    @Test
    public void when_creating_a_person_then_that_person_is_returned() {
        // Given
        var testPerson = testDataFactory.buildPersonWithoutAnyAssociations();

        givenThat(post(urlEqualTo(EMAILS_PATH)).withHeader(HttpHeaders.CONTENT_TYPE, containing(MediaType.APPLICATION_JSON_VALUE)).willReturn(
                aResponse().withStatus(HttpStatus.ACCEPTED.value()).withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                           .withBodyFile("thule-email-service-response.json")));

        // When
        var responseEntity = oAuth2RestTemplate.postForEntity(peopleServiceUrl, testPerson, Person.class);

        // Then
        verify(postRequestedFor(urlPathEqualTo(EMAILS_PATH)).withHeader(HttpHeaders.CONTENT_TYPE, containing(MediaType.APPLICATION_JSON_VALUE)));

        var statusCode = responseEntity.getStatusCode();
        var actualPerson = responseEntity.getBody();
        var etag = ETag.from(responseEntity.getHeaders().get(HttpHeaders.ETAG).get(0));
        var version = Long.parseLong(trimTrailingCharacter(trimLeadingCharacter(etag.toString(), '"'), '"'));

        assertThat(statusCode).isEqualTo(HttpStatus.CREATED);
        assertThat(actualPerson).isNotSameAs(testPerson);
        assertThat(actualPerson.getCreatedAt()).isNotNull();
        assertThat(actualPerson.getCreatedBy()).isEqualTo(TestDataFactory.JUNIT_TEST_USERNAME);
        assertThat(actualPerson.getDateOfBirth()).isEqualTo(testPerson.getDateOfBirth());
        assertThat(actualPerson.getDateOfExpiry()).isEqualTo(testPerson.getDateOfExpiry());
        assertThat(actualPerson.getDateOfPasswordExpiry()).isEqualTo(testPerson.getDateOfPasswordExpiry());
        assertThat(actualPerson.getEmailAddress()).isEqualTo(testPerson.getEmailAddress());
        assertThat(actualPerson.getFirstName()).isEqualTo(testPerson.getFirstName());
        assertThat(actualPerson.getHomeAddress()).isEqualTo(testPerson.getHomeAddress());
        assertThat(actualPerson.getId()).isNotNull();
        assertThat(actualPerson.getLastName()).isEqualTo(testPerson.getLastName());
        assertThat(actualPerson.getPassword()).isEqualTo(testPerson.getPassword());
        assertThat(actualPerson.getPhotographs()).containsExactlyElementsOf(testPerson.getPhotographs());
        assertThat(actualPerson.getSecondName()).isEqualTo(testPerson.getSecondName());
        assertThat(actualPerson.getState()).isEqualTo(testPerson.getState());
        assertThat(actualPerson.getTitle()).isEqualTo(testPerson.getTitle());
        assertThat(actualPerson.getUpdatedAt()).isEqualTo(actualPerson.getCreatedAt());
        assertThat(actualPerson.getUpdatedBy()).isEqualTo(TestDataFactory.JUNIT_TEST_USERNAME);
        assertThat(actualPerson.getUserId()).isEqualTo(testPerson.getUserId());
        assertThat(version).isEqualTo(0L);
        assertThat(actualPerson.getWorkAddress()).isEqualTo(testPerson.getWorkAddress());
    }

    @Test
    public void when_deleting_a_person_then_the_person_no_longer_exists() {
        // Given
        var person = createAndPersistPersonWithNoAssociations();

        givenThat(post(urlEqualTo(EMAILS_PATH)).withHeader(HttpHeaders.CONTENT_TYPE, containing(MediaType.APPLICATION_JSON_VALUE)).willReturn(
                aResponse().withStatus(HttpStatus.ACCEPTED.value()).withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                           .withBodyFile("thule-email-service-response.json")));

        // When
        oAuth2RestTemplate.delete(peopleServiceUrl + ID_PATH, person.getId());

        // Then
        verify(postRequestedFor(urlPathEqualTo(EMAILS_PATH)).withHeader(HttpHeaders.CONTENT_TYPE, containing(MediaType.APPLICATION_JSON_VALUE)));

        var deletedPerson = personRepository.findById(person.getId());

        assertThat(deletedPerson).isNotPresent();
    }

    @Test
    public void when_updating_a_person_then_that_person_is_updated() throws InterruptedException {
        // Given
        var testPerson = createAndPersistPersonWithNoAssociations();

        givenThat(post(urlEqualTo(EMAILS_PATH)).withHeader(HttpHeaders.CONTENT_TYPE, containing(MediaType.APPLICATION_JSON_VALUE)).willReturn(
                aResponse().withStatus(HttpStatus.ACCEPTED.value()).withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                           .withBodyFile("thule-email-service-response.json")));

        testPerson.setFirstName("updatedFirstName");
        testPerson.setSecondName("updatedSecondName");
        testPerson.setLastName("updatedLastName");
        testPerson.setDateOfBirth(LocalDate.of(1963, 05, 05));
        testPerson.setEmailAddress("updated@serin-consultancy.co.uk");
        testPerson.setPassword("updatedPassword");

        Thread.sleep(1000); // Allow enough time to lapse for the updatedAt to be updated with a different value

        // When
        oAuth2RestTemplate.put(peopleServiceUrl + ID_PATH, testPerson, testPerson.getId());

        // Then
        verify(postRequestedFor(urlPathEqualTo(EMAILS_PATH)).withHeader(HttpHeaders.CONTENT_TYPE, containing(MediaType.APPLICATION_JSON_VALUE)));

        var actualPerson = personRepository.findByIdAndFetchAllAssociations(testPerson.getId());

        assertThat(actualPerson).isNotSameAs(testPerson);
        assertThat(actualPerson.getCreatedAt()).isEqualTo(testPerson.getCreatedAt());
        assertThat(actualPerson.getCreatedBy()).isEqualTo(testPerson.getCreatedBy());
        assertThat(actualPerson.getDateOfBirth()).isEqualTo(testPerson.getDateOfBirth());
        assertThat(actualPerson.getDateOfExpiry()).isEqualTo(testPerson.getDateOfExpiry());
        assertThat(actualPerson.getDateOfPasswordExpiry()).isEqualTo(testPerson.getDateOfPasswordExpiry());
        assertThat(actualPerson.getEmailAddress()).isEqualTo(testPerson.getEmailAddress());
        assertThat(actualPerson.getFirstName()).isEqualTo(testPerson.getFirstName());
        assertThat(actualPerson.getHomeAddress()).isEqualTo(testPerson.getHomeAddress());
        assertThat(actualPerson.getId()).isEqualTo(testPerson.getId());
        assertThat(actualPerson.getLastName()).isEqualTo(testPerson.getLastName());
        assertThat(actualPerson.getPassword()).isEqualTo(testPerson.getPassword());
        assertThat(actualPerson.getPhotographs()).containsExactlyElementsOf(testPerson.getPhotographs());
        assertThat(actualPerson.getSecondName()).isEqualTo(testPerson.getSecondName());
        assertThat(actualPerson.getState()).isEqualTo(testDataFactory.getStates().get(StateCode.PERSON_ENABLED));
        assertThat(actualPerson.getTitle()).isEqualTo(testPerson.getTitle());
        assertThat(actualPerson.getUpdatedAt()).isAfter(testPerson.getUpdatedAt());
        assertThat(actualPerson.getUpdatedBy()).isEqualTo(TestDataFactory.JUNIT_TEST_USERNAME);
        assertThat(actualPerson.getUserId()).isEqualTo(testPerson.getUserId());
        assertThat(actualPerson.getVersion()).isEqualTo(testPerson.getVersion() + 1);
        assertThat(actualPerson.getWorkAddress()).isEqualTo(testPerson.getWorkAddress());
    }
}
