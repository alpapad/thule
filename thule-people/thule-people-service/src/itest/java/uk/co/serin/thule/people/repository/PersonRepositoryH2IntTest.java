package uk.co.serin.thule.people.repository;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"itest", "itest-h2"})
public class PersonRepositoryH2IntTest extends PersonRepositoryBaseIntTest {
}
