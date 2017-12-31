package uk.co.serin.thule.people.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.WireMockServer;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import uk.co.serin.thule.people.datafactories.ReferenceDataFactory;
import uk.co.serin.thule.people.datafactories.RepositoryReferenceDataFactory;
import uk.co.serin.thule.people.datafactories.TestDataFactory;
import uk.co.serin.thule.people.domain.DomainModel;
import uk.co.serin.thule.people.domain.person.Person;
import uk.co.serin.thule.people.domain.state.StateCode;
import uk.co.serin.thule.people.repository.repositories.ActionRepository;
import uk.co.serin.thule.people.repository.repositories.CountryRepository;
import uk.co.serin.thule.people.repository.repositories.PersonRepository;
import uk.co.serin.thule.people.repository.repositories.RoleRepository;
import uk.co.serin.thule.people.repository.repositories.StateRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("itest")
@RunWith(SpringJUnit4ClassRunner.class)
public class RestfulServiceIntTest {
    private static final String ID = "/{id}";
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
    private String urlForEmails = "/" + DomainModel.ENTITY_NAME_EMAILS;
    private String urlForPeople = "/" + DomainModel.ENTITY_NAME_PEOPLE;
    @Autowired
    private WireMockServer wireMockServer;

    @Test
    public void create_person() {
        // Given
        Person testPerson = testDataFactory.buildPersonWithoutAnyAssociations();
        Person expectedPerson = testDataFactory.buildPerson(testPerson);

        // When
        ResponseEntity<Person> responseEntity = restTemplate.postForEntity(urlForPeople, testPerson, Person.class);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Person actualPerson = responseEntity.getBody();

        assertThat(actualPerson.getUpdatedAt()).isNotNull();
        assertThat(actualPerson.getCreatedAt()).isEqualTo(actualPerson.getUpdatedAt());
        assertThat(actualPerson.getUpdatedBy()).isNotNull();

        assertThat(actualPerson).isEqualToIgnoringGivenFields(expectedPerson,
                DomainModel.ENTITY_ATTRIBUTE_NAME_ID,
                DomainModel.ENTITY_ATTRIBUTE_NAME_CREATED_AT,
                DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_AT,
                DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY);

        wireMockServer.verify(postRequestedFor(urlPathEqualTo(urlForEmails))
                .withHeader(HttpHeaders.CONTENT_TYPE, containing(MediaType.APPLICATION_JSON_VALUE)));
    }

    @Test
    public void delete_person() {
        // Given
        Person person = createTestPerson(testDataFactory.buildPersonWithoutAnyAssociations());

        // When
        restTemplate.delete(urlForPeople + ID, person.getId());

        // Then
        person = personRepository.findOne(person.getId());

        assertThat(person).isNull();

        wireMockServer.verify(postRequestedFor(urlPathEqualTo(urlForEmails))
                .withHeader(HttpHeaders.CONTENT_TYPE, containing(MediaType.APPLICATION_JSON_VALUE)));
    }

    private Person createTestPerson(Person expectedPerson) {
        Person person = testDataFactory.buildPerson(expectedPerson);
        person.setState(testDataFactory.getStates().get(StateCode.PERSON_ENABLED));
        return personRepository.save(person);
    }

    @Test
    public void get_all_people() {
        // Given
        Person testPerson = testDataFactory.buildPersonWithoutAnyAssociations();
        Person expectedPerson = createTestPerson(testPerson);

        // When
        ResponseEntity<Resources<Person>> personResponseEntity
                = restTemplate.exchange(urlForPeople + "?page={page}&size={size}", HttpMethod.GET, null, new ParameterizedTypeReference<Resources<Person>>() {
        }, 0, 1000);

        // Then
        assertThat(personResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        Collection<Person> actualPeople = personResponseEntity.getBody().getContent();
        assertThat(actualPeople).contains(expectedPerson);
    }

    @Test
    public void get_person() {
        // Given
        Person testPerson = testDataFactory.buildPersonWithoutAnyAssociations();
        Person expectedPerson = createTestPerson(testPerson);

        // When
        ResponseEntity<Person> responseEntity = restTemplate.getForEntity(urlForPeople + ID, Person.class, expectedPerson.getId());

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        Person actualPerson = responseEntity.getBody();

        assertThat(actualPerson).isEqualTo(expectedPerson);
    }

    @Test
    public void is_status_up() {
        // Given
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        // When
        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange("/health", HttpMethod.GET, HttpEntity.EMPTY, responseType);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().get("status")).isEqualTo(Status.UP.getCode());
    }

    @Before
    public void setUp() {
        ReferenceDataFactory referenceDataFactory = new RepositoryReferenceDataFactory(actionRepository, stateRepository, roleRepository, countryRepository);
        testDataFactory = new TestDataFactory(referenceDataFactory);

        // Create security context
        Person jUnitTestPerson = testDataFactory.buildJUnitTest();
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken(jUnitTestPerson.getUserId(), jUnitTestPerson.getPassword()));

        // Create http message converter to handle json+hal requests
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new Jackson2HalModule());
        objectMapper.registerModule(new JavaTimeModule());

        MappingJackson2HttpMessageConverter httpMessageConverter = new MappingJackson2HttpMessageConverter();
        httpMessageConverter.setObjectMapper(objectMapper);

        // Create restTemplate with Basic Authentication security
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("admin", "admin"));
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

        restTemplate.getRestTemplate().setMessageConverters(Collections.singletonList(httpMessageConverter));
        restTemplate.getRestTemplate().setRequestFactory(requestFactory);

        // Avoid wireMockServer.verify to check requests from other tests
        wireMockServer.resetRequests();
    }

    @Test
    public void update_person() {
        // Given
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

        // When
        restTemplate.put(urlForPeople + ID, testPerson, id);

        // Then
        Person actualPerson = restTemplate.getForObject(urlForPeople + ID, Person.class, id);

        assertThat(actualPerson.getUpdatedAt()).isAfter(expectedPerson.getUpdatedAt());
        assertThat(actualPerson.getUpdatedBy()).isNotEmpty();

        assertThat(actualPerson).isEqualToIgnoringGivenFields(expectedPerson,
                DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_AT,
                DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY);

        wireMockServer.verify(postRequestedFor(urlPathEqualTo(urlForEmails))
                .withHeader(HttpHeaders.CONTENT_TYPE, containing(MediaType.APPLICATION_JSON_VALUE)));
    }
}
