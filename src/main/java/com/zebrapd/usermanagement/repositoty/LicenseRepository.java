package com.zebrapd.usermanagement.repositoty;

import com.zebrapd.usermanagement.entity.License;
import com.zebrapd.usermanagement.entity.TrainingType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class LicenseRepository {

    private static LicenseRowMapper LICENSE_ROW_MAPPER = new LicenseRowMapper();
    private JdbcTemplate jdbcTemplate;

    public LicenseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public License createLicense(License license) {
        KeyHolder key = new GeneratedKeyHolder();
        String query = "INSERT INTO License (" +
            "client_id," +
            " type," +
            " number_of_trainings," +
            " price," +
            " start_date," +
            " expiration_date)" +
            " VALUES (?,?,?,?,?,?)";

        jdbcTemplate.update(con -> {
            final PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, license.getClientId());
            ps.setString(2, license.getType().name());
            ps.setInt(3, license.getNumberOfTrainings());
            ps.setDate(4, Date.valueOf(license.getStartDate()));
            ps.setDate(5, Date.valueOf(license.getExpiratioDate()));
            return ps;
        }, key);
        license.setEntityId((Integer) Objects.requireNonNull(key.getKeys()).get("entity_id"));
        return license;
    }

    public boolean setNumberOftrainings(int numberOfTrainings, int userId, TrainingType trainingType) {
        String query = "UPDATE License set number_of_trainings = ? WHERE client_id = ? AND type = ?";
        return 0 < jdbcTemplate.update(query, numberOfTrainings, userId, trainingType.name());
    }

    public List<License> getActiveLicense(int userId, TrainingType type){
        String query = "SELECT * FROM License" +
            " WHERE client_id = ?" +
            " AND type = ?" +
            " AND number_of_trainings > 0" +
            " AND expiration_date > ?";
        Date date = Date.valueOf(LocalDate.now());
        return jdbcTemplate.query(query, LICENSE_ROW_MAPPER, userId, type.name(), date);
    }

    static class LicenseRowMapper implements RowMapper<License>{

        @Override
        public License mapRow(ResultSet rs, int rowNum) throws SQLException {
            License license = new License();
            license.setEntityId(rs.getInt("entity_id"));
            license.setClientId(rs.getInt("client_id"));
            license.setType(TrainingType.valueOf(rs.getString("type")));
            license.setPrice(rs.getInt("price"));
            license.setStartDate(rs.getDate("start_date").toLocalDate());
            license.setExpiratioDate(rs.getDate("expiration_date").toLocalDate());
            return license;
        }
    }
}
