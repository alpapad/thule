package uk.co.serin.thule.people.repository;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"itest", "itest-h2"})
class PersonEntityRepositoryH2IntTest extends PersonEntityRepositoryBaseIntTest {
}
