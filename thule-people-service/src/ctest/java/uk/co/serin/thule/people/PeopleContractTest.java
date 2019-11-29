package uk.co.serin.thule.people;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.rest.webmvc.support.ETag;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.test.context.support.WithMockUser;

import uk.co.serin.thule.people.domain.entity.person.PersonEntity;
import uk.co.serin.thule.people.domain.entity.state.StateEntity;
import uk.co.serin.thule.people.domain.model.email.Email;
import uk.co.serin.thule.people.domain.model.state.StateCode;
import uk.co.serin.thule.people.repository.repositories.PersonRepository;
import uk.co.serin.thule.people.repository.repositories.StateRepository;
import uk.co.serin.thule.people.service.email.EmailServiceClient;
import uk.co.serin.thule.security.oauth2.utils.Oauth2Utils;
import uk.co.serin.thule.utils.utils.RandomUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Objects;

import javax.persistence.EntityManager;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.util.StringUtils.trimLeadingCharacter;
import static org.springframework.util.StringUtils.trimTrailingCharacter;
import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@WithMockUser
public class PeopleContractTest extends ContractBaseTest {
    private static final String ID_PATH = "/{id}";
    private static final String MOCK_USERS_NAME = "user";
    @MockBean
    private EmailServiceClient emailServiceClient;
    @Autowired
    private EntityManager entityManager;
    private OAuth2RestTemplate oAuth2RestTemplate;
    private String baseUrl;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private StateRepository stateRepository;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void given_a_new_person_when_finding_all_people_then_the_new_person_is_returned() {
        // Given
        var testPerson = createAndPersistPersonWithNoAssociations();

        // When
        var personResponseEntity =
                oAuth2RestTemplate.exchange(baseUrl, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Resources<PersonEntity>>() {
                }, 0, 1000);

        // Then
        var actualPeople = Objects.requireNonNull(personResponseEntity.getBody()).getContent();

        assertThat(personResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualPeople).contains(testPerson);
    }

    private PersonEntity createAndPersistPersonWithNoAssociations() {
        var person = buildPersonWithoutAnyAssociations();
        person.setState(stateRepository.findByCode(StateCode.PERSON_ENABLED));

        personRepository.saveAndFlush(person);
        entityManager.clear();
        person.setState(null);

        return person;
    }

    private PersonEntity buildPersonWithoutAnyAssociations() {
        var dateOfExpiry = RandomUtils.generateUniqueRandomDateAfter(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var userId = "missScarlett" + RandomUtils.generateUniqueRandomString(8);

        return PersonEntity.builder().
                dateOfBirth(RandomUtils.generateUniqueRandomDateInThePast()).
                                   dateOfExpiry(RandomUtils.generateUniqueRandomDateInTheFuture()).
                                   dateOfPasswordExpiry(RandomUtils.generateUniqueRandomDateBetween(LocalDate.now(), dateOfExpiry)).
                                   emailAddress(userId + "@serin-consultancy.co.uk").
                                   firstName("Elizabeth").
                                   lastName("Scarlett").
                                   password(userId).
                                   secondName("K").
                                   title("Miss").
                                   userId(userId).
                                   build();
    }

    @Test
    public void given_a_new_person_when_finding_by_id_then_the_person_is_returned() {
        // Given
        var testPerson = createAndPersistPersonWithNoAssociations();

        // When
        var responseEntity = oAuth2RestTemplate.getForEntity(baseUrl + ID_PATH, PersonEntity.class, testPerson.getId());

        // Then
        var actualPerson = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualPerson).isEqualTo(testPerson);
    }

    @Before
    public void setUp() {
        // Set up service url
        baseUrl = testRestTemplate.getRootUri() + "/people";

        // Setup OAuth2
        var jwtOauth2AccessToken = Oauth2Utils.createJwtOauth2AccessToken(MOCK_USERS_NAME, 0,
                Collections.singleton(new SimpleGrantedAuthority("grantedAuthority")), "clientId", "gmjtdvNVmQRz8bzw6ae");
        oAuth2RestTemplate = new OAuth2RestTemplate(new ResourceOwnerPasswordResourceDetails(), new DefaultOAuth2ClientContext(jwtOauth2AccessToken));

        // By default the OAuth2RestTemplate does not have the full set of message converters which the TestRestTemplate has, including the ResourceResourceHttpMessageConverter required for HateOAS support
        // So, add all the message converters from the TestRestTemplate
        oAuth2RestTemplate.setMessageConverters(testRestTemplate.getRestTemplate().getMessageConverters());
    }

    @Test
    public void when_creating_a_person_then_that_person_is_returned() {
        // Given
        var testPerson = buildPersonWithoutAnyAssociations();
        given(emailServiceClient.sendEmail(any())).willReturn(Email.builder().build());

        // When
        var responseEntity = oAuth2RestTemplate.postForEntity(baseUrl, testPerson, PersonEntity.class);

        // Then
        var statusCode = responseEntity.getStatusCode();
        var actualPerson = responseEntity.getBody();
        var etag = ETag.from(Objects.requireNonNull(responseEntity.getHeaders().get(HttpHeaders.ETAG)).stream().findFirst());
        var version = Long.parseLong(trimTrailingCharacter(trimLeadingCharacter(etag.toString(), '"'), '"'));

        assertThat(statusCode).isEqualTo(HttpStatus.CREATED);
        assertThat(actualPerson).isNotNull();
        assertThat(actualPerson).isNotSameAs(testPerson);
        assertThat(actualPerson.getCreatedAt()).isNotNull();
        assertThat(actualPerson.getCreatedBy()).isEqualTo(MOCK_USERS_NAME);
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
        assertThat(actualPerson.getUpdatedBy()).isEqualTo(MOCK_USERS_NAME);
        assertThat(actualPerson.getUserId()).isEqualTo(testPerson.getUserId());
        assertThat(version).isEqualTo(0L);
        assertThat(actualPerson.getWorkAddress()).isEqualTo(testPerson.getWorkAddress());
    }

    @Test
    public void when_deleting_a_person_then_the_person_no_longer_exists() {
        // Given
        var person = createAndPersistPersonWithNoAssociations();
        given(emailServiceClient.sendEmail(any())).willReturn(Email.builder().build());

        // When
        oAuth2RestTemplate.delete(baseUrl + ID_PATH, person.getId());

        // Then
        var deletedPerson = personRepository.findById(person.getId());
        assertThat(deletedPerson).isNotPresent();
    }

    @Test
    public void when_updating_a_person_then_that_person_is_updated() throws InterruptedException {
        // Given
        var testPerson = createAndPersistPersonWithNoAssociations();
        given(emailServiceClient.sendEmail(any())).willReturn(Email.builder().build());

        testPerson.setFirstName("updatedFirstName");
        testPerson.setSecondName("updatedSecondName");
        testPerson.setLastName("updatedLastName");
        testPerson.setDateOfBirth(LocalDate.of(1963, 5, 5));
        testPerson.setEmailAddress("updated@serin-consultancy.co.uk");
        testPerson.setPassword("updatedPassword");

        Thread.sleep(1000); // Allow enough time to lapse for the updatedAt to be updated with a different value

        // When
        oAuth2RestTemplate.put(baseUrl + ID_PATH, testPerson, testPerson.getId());

        // Then
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
        assertThat(actualPerson.getState()).isEqualTo(StateEntity.builder().code(StateCode.PERSON_ENABLED).build());
        assertThat(actualPerson.getTitle()).isEqualTo(testPerson.getTitle());
        assertThat(actualPerson.getUpdatedAt()).isAfter(testPerson.getUpdatedAt());
        assertThat(actualPerson.getUpdatedBy()).isEqualTo(MOCK_USERS_NAME);
        assertThat(actualPerson.getUserId()).isEqualTo(testPerson.getUserId());
        assertThat(actualPerson.getVersion()).isEqualTo(testPerson.getVersion() + 1);
        assertThat(actualPerson.getWorkAddress()).isEqualTo(testPerson.getWorkAddress());
    }
}
