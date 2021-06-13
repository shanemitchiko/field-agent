package learn.field_agent.data;

import learn.field_agent.models.Agent;
import learn.field_agent.models.Alias;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Equals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AliasJdbcTemplateRepositoryTest {

    @Autowired
    AliasJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setUp() { knownGoodState.set();}

    @Test
    void shouldAddAlias() {
        Alias alias = new Alias();
        alias.setName("TEST ALIAS");
        alias.setPersona("TEST PERSONA");

        Agent hazel = repository.findAgentByAliasId(1);
        assertNotNull(hazel);
        alias.setAgent(hazel);

        Alias actual = repository.add(alias);
        assertNotNull(actual);
        assertEquals(3, actual.getAliasId());
    }
}