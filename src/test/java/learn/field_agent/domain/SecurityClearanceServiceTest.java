package learn.field_agent.domain;

import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.Location;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SecurityClearanceServiceTest {

    @Autowired
    SecurityClearanceService service;

    @MockBean
    SecurityClearanceRepository repository;

    @Test
    void shouldAddWhenValid() {
        SecurityClearance securityClearance = makeSecurityClearance();
        SecurityClearance mockOut = makeSecurityClearance();
        mockOut.setSecurityClearanceId(1);

        when(repository.add(securityClearance)).thenReturn(mockOut);

        Result<SecurityClearance> actual = service.add(securityClearance);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldNotAddWhenInvalid() {
        SecurityClearance securityClearance = makeSecurityClearance();
        securityClearance.setName(" ");

        Result<SecurityClearance> actual = service.add(securityClearance);
        assertEquals(ResultType.INVALID, actual.getType());

        SecurityClearance arg = new SecurityClearance();
        arg.setSecurityClearanceId(1);
        arg.setName("Secret");

        Result<SecurityClearance> expected = service.add(arg);
        assertEquals(ResultType.INVALID, expected.getType());

    }

    @Test
    void shouldUpdate() {
        SecurityClearance securityClearance = makeSecurityClearance();
        securityClearance.setSecurityClearanceId(1);

        securityClearance.setName("Updated Secret");
        when(repository.update(securityClearance)).thenReturn(true);
        Result<SecurityClearance> actual = service.update(securityClearance);
        assertEquals(ResultType.SUCCESS, actual.getType());
        System.out.println(securityClearance.getName());
    }

    @Test
    void shouldNotUpdateWhenInvalid() {
        SecurityClearance securityClearance = makeSecurityClearance();
        Result<SecurityClearance> actual = service.update(securityClearance);
        assertEquals(ResultType.INVALID, actual.getType());

        securityClearance = makeSecurityClearance();
        securityClearance.setName(" ");

        actual = service.add(securityClearance);
        assertEquals(ResultType.INVALID, actual.getType());

        SecurityClearance arg = new SecurityClearance();
        arg.setSecurityClearanceId(1);
        arg.setName("Secret");

        Result<SecurityClearance> expected = service.add(arg);
        assertEquals(ResultType.INVALID, expected.getType());

    }

    // delete tests aren't valuable

    SecurityClearance makeSecurityClearance(){
        SecurityClearance securityClearance = new SecurityClearance();
        securityClearance.setName("Secret");
        return securityClearance;
    }
}