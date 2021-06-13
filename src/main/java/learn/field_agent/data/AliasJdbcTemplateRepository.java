package learn.field_agent.data;

import learn.field_agent.data.mappers.AgentAgencyMapper;
import learn.field_agent.data.mappers.AgentMapper;
import learn.field_agent.data.mappers.AliasMapper;
import learn.field_agent.models.Agent;
import learn.field_agent.models.Alias;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class AliasJdbcTemplateRepository implements AliasRepository {

    private final JdbcTemplate jdbcTemplate;

    public AliasJdbcTemplateRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    @Override
    public List<Alias> findAll() {
        final String sql = "select a.alias_id, a.name, a.persona, a.agent_id, " +
                "ag.agent_id, ag.first_name, ag.middle_name, ag.last_name, ag.dob, ag. height_in_inches " +
                "from alias a " +
                "inner join agent ag on ag.agent_id = a.agent_id;";
        return jdbcTemplate.query(sql, new AliasMapper());
    }

    public Alias add(Alias alias) {

        final String sql = "insert into alias (name, persona, agent_id) "
                + " values (?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, alias.getName());
            ps.setString(2, alias.getPersona());
            ps.setInt(3, alias.getAgent().getAgentId());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        alias.setAliasId(keyHolder.getKey().intValue());
        return alias;
    }

    public boolean update(Alias alias) {

        final String sql = "update alias set "
                + "name = ?, "
                + "persona = ? "
                + "where alias_id = ? and agent_id = ?;";

        return jdbcTemplate.update(sql,
                alias.getAliasId(),
                alias.getName(),
                alias.getPersona(),
                alias.getAgent().getAgentId()) > 0;
    }

    public boolean deleteById(int aliasId) {
        return jdbcTemplate.update("delete from alias where alias_id = ?;", aliasId) > 0;
    }

    private void addAgencies(Agent agent) {

        final String sql = "select aa.agency_id, aa.agent_id, aa.identifier, aa.activation_date, aa.is_active, "
                + "sc.security_clearance_id, sc.name security_clearance_name, "
                + "a.short_name, a.long_name "
                + "from agency_agent aa "
                + "inner join agency a on aa.agency_id = a.agency_id "
                + "inner join security_clearance sc on aa.security_clearance_id = sc.security_clearance_id "
                + "where aa.agent_id = ?;";

        var agentAgencies = jdbcTemplate.query(sql, new AgentAgencyMapper(), agent.getAgentId());
        agent.setAgencies(agentAgencies);
    }

    public Agent findAgentByAliasId(int aliasId) {
        final String sql = "select ag.agent_id, ag.first_name, ag.middle_name, ag.last_name, ag.dob, ag.height_in_inches "
                + "from agent ag "
                + "inner join alias al on al.agent_id = ag.agent_id "
                + "where alias_id = ? limit 1;";
        Agent agent = jdbcTemplate.query(sql, new AgentMapper(), aliasId).stream()
                .findFirst().orElse(null);
        if (agent != null) {
            addAgencies(agent);
        }
        return agent;
    }
}
