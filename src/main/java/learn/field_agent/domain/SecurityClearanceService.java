package learn.field_agent.domain;

import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.SecurityClearance;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityClearanceService {

    private final SecurityClearanceRepository repository;

    public SecurityClearanceService(SecurityClearanceRepository repository) {
        this.repository = repository;
    }

    public List<SecurityClearance> findAll() {return repository.findAll();}

    public SecurityClearance findById(int securityClearanceId) {return repository.findById(securityClearanceId);}

    public Result<SecurityClearance> add(SecurityClearance securityClearance) {
        Result<SecurityClearance> result = validate(securityClearance);
        if (!result.isSuccess()) {
            return result;
        }
        return result;}

    private Result<SecurityClearance> validate(SecurityClearance securityClearance) {
        Result<SecurityClearance> result = new Result<>();
        List<SecurityClearance> clearances = repository.findAll();
        for (SecurityClearance sc : clearances)
            if (securityClearance.getSecurityClearanceId() == sc.getSecurityClearanceId() &&
                    securityClearance.getName() == sc.getName()) {
                result.addMessage("cannot be duplicate", ResultType.INVALID);
                return result;
            }
        return result;
    }

}
