package learn.field_agent.data;

import learn.field_agent.models.Agent;
import learn.field_agent.models.Alias;

import java.util.List;

public interface AliasRepository {
    List<Alias> findAll();

    Alias add(Alias alias);

    boolean update(Alias alias);

    void addAgencies(Agent agent);

    Agent findAgentByAliasId(int aliasId);
}
