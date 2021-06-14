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

        if (securityClearance.getSecurityClearanceId() != 0) {
            result.addMessage("security clearance cannot be set for `add` operation", ResultType.INVALID);
        }

        securityClearance = repository.add(securityClearance);
        result.setPayload(securityClearance);
        return result;
    }

    public Result<SecurityClearance> update(SecurityClearance securityClearance) {
        Result<SecurityClearance> result = validate(securityClearance);

        if (!result.isSuccess()) {
            return result;
        }

        if (securityClearance.getSecurityClearanceId() <= 0) {
            result.addMessage("security_clearance_id must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if (!repository.update(securityClearance)) {
            String msg = String.format("security_clearance_id: %s, not found", securityClearance.getSecurityClearanceId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }
        return result;

    }

    public boolean deleteById(int securityClearanceId) {return repository.deleteById(securityClearanceId);}

    private Result<SecurityClearance> validate(SecurityClearance securityClearance) {
        Result<SecurityClearance> result = new Result<>();

        if (securityClearance == null) {
            result.addMessage("security clearance cannot be null", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(securityClearance.getName())) {
            result.addMessage("name is required", ResultType.INVALID);
        }

        List<SecurityClearance> clearances = repository.findAll();
        for (SecurityClearance sc : clearances)
            if (sc.getName() == securityClearance.getName()) {
                result.addMessage("cannot be a duplicate", ResultType.INVALID);
                return result;

            }
        return result;
    }
}
