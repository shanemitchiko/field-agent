package learn.field_agent.domain;

import learn.field_agent.data.AliasRepository;
import learn.field_agent.models.Agent;
import learn.field_agent.models.Alias;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AliasServiceTest {

    @Autowired
    AliasService service;

    @MockBean
    AliasRepository repository;

    @Test
    void shouldAddWhenValid() {
        Alias alias = makeAlias();
        Alias mockOut = makeAlias();
        mockOut.setAliasId(1);

        when(repository.add(alias)).thenReturn(mockOut);

        Result<Alias> actual = service.add(alias);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldNotAddWhenInvalid() {
        Alias alias = makeAlias();
        alias.setName(" ");

        Result<Alias> actual = service.add(alias);
        assertEquals(ResultType.INVALID, actual.getType());

        Alias arg = new Alias();
        arg.setAliasId(1);
        arg.setName("Irina");
        arg.setPersona("Software Engineer Program Instructor Assistant by day, cat aficionado and house flipper by night");

        Agent hazel = repository.findAgentByAliasId(1);
        arg.setAgent(hazel);

        Result<Alias> expected = service.add(arg);
        assertEquals(ResultType.INVALID, expected.getType());
    }

    @Test
    void shouldUpdate() {
        Alias alias = makeAlias();
        alias.setAliasId(1);

        alias.setPersona("Muckbang Youtube personality");
        when(repository.update(alias)).thenReturn(true);
        Result<Alias> actual = service.update(alias);
        assertEquals(ResultType.SUCCESS, actual.getType());
        System.out.println(alias.getName());
    }

    @Test
    void shouldNotUpdateWhenInvalid() {
        Alias alias = makeAlias();
        Result<Alias> actual = service.update(alias);
        assertEquals(ResultType.INVALID, actual.getType());

        alias = makeAlias();
        alias.setName(" ");

        actual = service.update(alias);
        assertEquals(ResultType.INVALID, actual.getType());

        Alias arg = new Alias();
        arg.setAliasId(1);
        arg.setName("Irina");
        arg.setPersona("Software Engineer Program Instructor Assistant by day, cat aficionado and house flipper by night");

        Agent hazel = repository.findAgentByAliasId(1);
        arg.setAgent(hazel);


        Result<Alias> expected = service.add(arg);
        assertEquals(ResultType.INVALID, expected.getType());

    }

    Alias makeAlias() {
        Alias alias = new Alias();
        alias.setName("Irina");
        alias.setPersona("Software Engineer Program Instructor Assistant by day, cat aficionado and house flipper by night");

        Agent hazel = repository.findAgentByAliasId(1);
        alias.setAgent(hazel);
        return alias;
    }

}