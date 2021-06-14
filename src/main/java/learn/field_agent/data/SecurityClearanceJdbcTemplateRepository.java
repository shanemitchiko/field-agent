package learn.field_agent.data;

import learn.field_agent.data.mappers.AgencyAgentMapper;
import learn.field_agent.data.mappers.SecurityClearanceMapper;
import learn.field_agent.models.SecurityClearance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class SecurityClearanceJdbcTemplateRepository implements SecurityClearanceRepository {

    private final JdbcTemplate jdbcTemplate;

    public SecurityClearanceJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<SecurityClearance> findAll() {
        final String sql = "select security_clearance_id, name security_clearance_name "
                + "from security_clearance limit 1000;";
        return jdbcTemplate.query(sql, new SecurityClearanceMapper());
    }

    @Override
    public SecurityClearance findById(int securityClearanceId) {

        final String sql = "select security_clearance_id, name security_clearance_name "
                + "from security_clearance "
                + "where security_clearance_id = ?;";

        return jdbcTemplate.query(sql, new SecurityClearanceMapper(), securityClearanceId)
                .stream()
                .findFirst().orElse(null);
    }

    @Override
    public SecurityClearance add(SecurityClearance securityClearance) {

        final String sql = "insert into security_clearance (name) "
                + " values (?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, securityClearance.getName());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        securityClearance.setSecurityClearanceId(keyHolder.getKey().intValue());
        return securityClearance;
    }

    @Override
    public boolean update(SecurityClearance securityClearance) {
        final String sql = "update security_clearance set "
                    + "name = ? "
                    + "where security_clearance_id = ?;";

        int rowsUpdated = jdbcTemplate.update(sql,
                    securityClearance.getName(),
                securityClearance.getSecurityClearanceId());

        return rowsUpdated > 0;
    }

    @Override
    public boolean deleteById(int securityClearanceId) {
        final String sql = "select aa.agency_id, aa.identifier, aa.activation_date, aa.is_active, " +
                "sc.security_clearance_id, sc.name security_clearance_name, " +
                "a.agent_id, a.first_name, a.middle_name, a.last_name, a.dob, a.height_in_inches " +
                "from agency_agent aa " +
                "inner join security_clearance sc on aa.security_clearance_id = sc.security_clearance_id " +
                "inner join agent a on aa.agent_id = a.agent_id " +
                "where sc.security_clearance_id = ? limit 1;";

        var agencyAgent = jdbcTemplate.query(sql, new AgencyAgentMapper(), securityClearanceId)
                .stream()
                .findFirst().orElse(null);

        if(agencyAgent != null) {
            return false;
        }

        final String Deletesql = "delete from security_clearance where security_clearance_id = ?;";
        return jdbcTemplate.update(Deletesql, securityClearanceId) > 0;
    }
}
