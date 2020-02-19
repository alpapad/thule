package uk.co.serin.thule.people;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.data.rest.webmvc.support.ETag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

import uk.co.serin.thule.people.domain.entity.person.PersonEntity;
import uk.co.serin.thule.people.domain.entity.state.StateEntity;
import uk.co.serin.thule.people.domain.model.email.Email;
import uk.co.serin.thule.people.domain.model.state.StateCode;
import uk.co.serin.thule.people.repository.repositories.PersonRepository;
import uk.co.serin.thule.people.repository.repositories.StateRepository;
import uk.co.serin.thule.security.oauth2.utils.JwtUtils;
import uk.co.serin.thule.utils.utils.RandomUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import javax.persistence.EntityManager;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.springframework.util.StringUtils.trimLeadingCharacter;
import static org.springframework.util.StringUtils.trimTrailingCharacter;
import static uk.co.serin.thule.test.assertj.ThuleAssertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@WithMockUser
public class PeopleContractTest extends ContractBaseTest {
    private static final String ID_PATH = "/{id}";
    private static final String MOCK_USERS_NAME = "user";
    private static final String PEOPLE_PATH = "/people";
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private StateRepository stateRepository;
    @Autowired
    private WebTestClient webTestClient;

    @Before
    public void before() {
        var jwt = JwtUtils.createKeycloakJwt(MOCK_USERS_NAME, 0, AuthorityUtils.createAuthorityList("grantedAuthority"), "clientId");
        webTestClient = webTestClient.mutate().defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.getTokenValue()).build();
    }

//    @Test
//    public void given_a_new_person_when_finding_all_people_then_the_new_person_is_returned() {
//        // Given
//        var testPerson = createAndPersistPersonWithNoAssociations();
//
//        // When
//        var personResponseEntity =
//                webTestClient.get().uri("/people"), HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<CollectionModel<PersonEntity>>() {
//                }, 0, 1000);
//
//        // Then
//        var actualPeople = Objects.requireNonNull(personResponseEntity.getBody()).getContent();
//
//        assertThat(personResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(actualPeople).contains(testPerson);
//    }

    @Test
    public void given_a_new_person_when_finding_by_id_then_the_person_is_returned() {
        // Given
        var testPerson = createAndPersistPersonWithNoAssociations();

        // When
        var entityExchangeResult = webTestClient.get().uri(PEOPLE_PATH + ID_PATH, testPerson.getId()).exchange()
                                                .expectStatus().isOk()
                                                .expectBody(PersonEntity.class)
                                                .returnResult();

        // Then
        var actualPerson = entityExchangeResult.getResponseBody();
        assertThat(actualPerson).isEqualTo(testPerson);
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
    public void when_creating_a_person_then_that_person_is_returned() throws JsonProcessingException {
        // Given
        var testPerson = buildPersonWithoutAnyAssociations();
        var email = objectMapper.writeValueAsString(Email.builder().build());
        givenThat(post(urlEqualTo("/emails")).willReturn(
                aResponse().withStatus(HttpStatus.OK.value()).withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                           .withBody(email)));

        // When
        var entityExchangeResult = webTestClient.post().uri(PEOPLE_PATH)
                                                .bodyValue(testPerson).exchange()
                                                .expectStatus().isCreated()
                                                .expectBody(PersonEntity.class)
                                                .returnResult();

        // Then
        var actualPerson = entityExchangeResult.getResponseBody();
        var etag = ETag.from(Objects.requireNonNull(entityExchangeResult.getResponseHeaders().get(HttpHeaders.ETAG)).stream().findFirst());
        var version = Long.parseLong(trimTrailingCharacter(trimLeadingCharacter(etag.toString(), '"'), '"'));

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
    public void when_deleting_a_person_then_the_person_no_longer_exists() throws JsonProcessingException {
        // Given
        var person = createAndPersistPersonWithNoAssociations();
        var email = objectMapper.writeValueAsString(Email.builder().build());
        givenThat(post(urlEqualTo("/emails")).willReturn(
                aResponse().withStatus(HttpStatus.OK.value()).withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                           .withBody(email)));

        // When
        webTestClient.delete().uri(PEOPLE_PATH + ID_PATH, person.getId()).exchange();

        // Then
        var deletedPerson = personRepository.findById(person.getId());
        assertThat(deletedPerson).isNotPresent();
    }

    @Test
    public void when_updating_a_person_then_that_person_is_updated() throws InterruptedException, JsonProcessingException {
        // Given
        var testPerson = createAndPersistPersonWithNoAssociations();
        var email = objectMapper.writeValueAsString(Email.builder().build());
        givenThat(post(urlEqualTo("/emails")).willReturn(
                aResponse().withStatus(HttpStatus.OK.value()).withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                           .withBody(email)));

        testPerson.setFirstName("updatedFirstName");
        testPerson.setSecondName("updatedSecondName");
        testPerson.setLastName("updatedLastName");
        testPerson.setDateOfBirth(LocalDate.of(1963, 5, 5));
        testPerson.setEmailAddress("updated@serin-consultancy.co.uk");
        testPerson.setPassword("updatedPassword");

        Thread.sleep(1000); // Allow enough time to lapse for the updatedAt to be updated with a different value

        // When
        webTestClient.put().uri(PEOPLE_PATH + ID_PATH, testPerson.getId()).bodyValue(testPerson).exchange().expectStatus().isOk();

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
