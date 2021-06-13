package learn.field_agent.domain;

import learn.field_agent.data.AliasRepository;
import learn.field_agent.models.Alias;

import java.util.List;

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
            if (alias.getAliasId() == a.getAliasId() &&
                    alias.getName() == a.getName() &&
                    alias.getPersona() == alias.getPersona() &&
                    alias.getAgent().getAgentId() == a.getAgent().getAgentId()) {
                result.addMessage("Persona is already used for an Alias. Need a different Persona.", ResultType.INVALID);
                return result;
            }
        return result;
    }

}
