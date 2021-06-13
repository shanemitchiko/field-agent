package learn.field_agent.domain;

import learn.field_agent.data.AliasRepository;
import learn.field_agent.models.Alias;
import learn.field_agent.models.SecurityClearance;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AliasService {

    private final AliasRepository repository;

    public AliasService(AliasRepository repository) {
        this.repository = repository;
    }

    public List<Alias> findAll() { return repository.findAll(); }

    public Result<Alias> add(Alias alias) {
        Result<Alias> result = validate(alias);
        if(!result.isSuccess()) {
            return result;
        }

        if (alias.getAliasId() != 0) {
            result.addMessage("aliasId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        alias = repository.add(alias);
        result.setPayload(alias);
        return result;
    }

    public Result<Alias> update(Alias alias) {
        Result<Alias> result = validate(alias);

        if (!result.isSuccess()) {
            return result;
        }

        if (alias.getAliasId() <= 0) {
            result.addMessage("alias id must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if (!repository.update(alias)) {
            String msg = String.format("security_clearance_id: %s, not found", alias.getAliasId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }
        return result;
    }

    private Result<Alias> validate(Alias alias) {
        Result<Alias> result = new Result<>();

        if (alias == null) {
            result.addMessage("alias cannot be null", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(alias.getName())) {
            result.addMessage("name is required", ResultType.INVALID);
        }

        List<Alias> aliases = repository.findAll();
        for (Alias a : aliases)
            if (alias.getPersona() == alias.getPersona() &&
                    alias.getAgent().getAgentId() == a.getAgent().getAgentId()) {
                result.addMessage("Persona is already used for an Alias. Need a different Persona for this alias name.", ResultType.INVALID);
                return result;
            }
        return result;
    }

}
