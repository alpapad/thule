package uk.co.serin.thule.people.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import uk.co.serin.thule.people.Application;
import uk.co.serin.thule.people.datafactories.DataFactory;
import uk.co.serin.thule.people.datafactories.RepositoryReferenceDataFactory;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringApplicationConfiguration(Application.class)
@WebIntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RestfulServiceIntTest {
    private static final String ID = "/{id}";
    @Autowired
    private ActionRepository actionRepository;
    @Autowired
    private CountryRepository countryRepository;
    private DataFactory dataFactory;
    @Autowired
    private Environment env;
    @Autowired
    private PersonRepository personRepository;
    private RestTemplate restTemplate;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private StateRepository stateRepository;
    private String urlForHealth;
    private String urlForPeople;
    private String urlPrefix;

    @Test
    public void createPerson() {
        // Given
        Person testPerson = dataFactory.getTestDataFactory().newPersonWithoutAnyAssociations();
        Person expectedPerson = new Person(testPerson);

        // When
        ResponseEntity<Person> responseEntity = restTemplate.postForEntity(urlForPeople, testPerson, Person.class);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Person actualPerson = responseEntity.getBody();

        assertThat(actualPerson.getAudit().getUpdatedAt()).isNotNull();
        assertThat(actualPerson.getAudit().getCreatedAt()).isEqualTo(actualPerson.getAudit().getUpdatedAt());
        assertThat(actualPerson.getAudit().getUpdatedBy()).isNotNull();

        assertThat(actualPerson).isEqualToIgnoringGivenFields(expectedPerson, DomainModel.ENTITY_ATTRIBUTE_NAME_AUDIT);
    }

    private Person createTestPerson(Person expectedPerson) {
        Person person = new Person(expectedPerson);
        person.setState(dataFactory.getReferenceDataFactory().getStates().get(StateCode.PERSON_ENABLED));
        return personRepository.save(person);
    }

    @Test
    public void deletePerson() {
        // Given
        Person person = createTestPerson(dataFactory.getTestDataFactory().newPersonWithoutAnyAssociations());

        // When
        restTemplate.delete(urlForPeople + ID, person.getId());

        // Then
        person = personRepository.findOne(person.getId());

        assertThat(person).isNull();
    }

    @Test
    public void getAllPeople() {
        // Given
        Person testPerson = dataFactory.getTestDataFactory().newPersonWithoutAnyAssociations();
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
    public void getPerson() {
        // Given
        Person testPerson = dataFactory.getTestDataFactory().newPersonWithoutAnyAssociations();
        Person expectedPerson = createTestPerson(testPerson);

        // When
        ResponseEntity<Person> responseEntity = restTemplate.getForEntity(urlForPeople + ID, Person.class, expectedPerson.getId());

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        Person actualPerson = responseEntity.getBody();

        assertThat(actualPerson).isEqualTo(expectedPerson);
    }

    @Test
    public void isHealthy() {
        // Given
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        // When
        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(urlForHealth, HttpMethod.GET, null, responseType);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().get("status")).isEqualTo(Status.UP.getCode());
    }

    @Before
    public void setUp() {
        dataFactory = new DataFactory(new RepositoryReferenceDataFactory(actionRepository, stateRepository, roleRepository, countryRepository));
        urlPrefix = "http://localhost:" + env.getRequiredProperty("server.port", Integer.class) + env.getRequiredProperty("server.context-path") + "/";
        urlForHealth = urlPrefix + "health";
        urlForPeople = urlPrefix + DomainModel.ENTITY_NAME_PEOPLE;

        // Create security context
        Person jUnitTestPerson = dataFactory.getTestDataFactory().newJUnitTest();
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

        restTemplate = new RestTemplate(requestFactory);
        restTemplate.setMessageConverters(Collections.singletonList(httpMessageConverter));
    }

    @Test
    public void updatePerson() throws InterruptedException {
        // Given
        Person testPerson = dataFactory.getTestDataFactory().newPersonWithoutAnyAssociations();
        long id = createTestPerson(testPerson).getId();

        testPerson.setDateOfBirth(testPerson.getDateOfBirth().minusDays(1));
        testPerson.setEmailAddress("updated@serin-consultancy.co.uk");
        testPerson.setFirstName("updatedFirstName");
        testPerson.setPassword("updatedPassword");
        testPerson.setSecondName("updatedSecondName");
        testPerson.setSurname("updatedSurname");

        Person expectedPerson = new Person(testPerson);

        // When
        restTemplate.put(urlForPeople + ID, testPerson, id);

        // Then
        Person actualPerson = restTemplate.getForObject(urlForPeople + ID, Person.class, id);

        assertThat(actualPerson.getAudit().getUpdatedAt()).isAfter(expectedPerson.getAudit().getUpdatedAt());
        assertThat(actualPerson.getAudit().getUpdatedBy()).isNotEmpty();

        assertThat(actualPerson).isEqualToIgnoringGivenFields(expectedPerson, DomainModel.ENTITY_ATTRIBUTE_NAME_AUDIT);
    }
}
