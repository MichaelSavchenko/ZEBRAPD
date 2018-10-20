package com.zebrapd.usermanagement.repositoty;

import com.zebrapd.usermanagement.entity.Trainer;
import com.zebrapd.usermanagement.error.exception.TrainerNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Service
public class TrainerRepository {

    private JdbcTemplate jdbcTemplate;
    private static TrainerRowMapper TRAINER_ROW_MAPPER = new TrainerRowMapper();

    public TrainerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Trainer getTrainerById(int entityId) {
        String query = "SELECT * FROM Trainer WHERE entity_id = ?";
        List<Trainer> trainers = jdbcTemplate.query(query, new Object[]{entityId}, TRAINER_ROW_MAPPER);
        return trainers.stream().findAny().orElse(null);
    }

    public Trainer getTrainerByEmail(String email) {
        String query = "SELECT * FROM Trainer WHERE email = ?";
        List<Trainer> trainers = jdbcTemplate.query(query, new Object[]{email}, TRAINER_ROW_MAPPER);
        return trainers.stream().findFirst().orElse(null);
    }

    public Trainer getTrainerByPhone(String phoneNumber) {
        String query = "SELECT * FROM Trainer WHERE phone = ?";
        List<Trainer> trainers = jdbcTemplate.query(query, new Object[]{phoneNumber}, TRAINER_ROW_MAPPER);
        return trainers.stream().findFirst().orElse(null);
    }

    public Trainer createTrainer(Trainer trainer) {
        KeyHolder key = new GeneratedKeyHolder();
        String query = "INSERT INTO Trainer (first_name, last_name, email, phone, active, default_salary) VALUES (?,?,?,?,?,?)";
        jdbcTemplate.update(
                con -> {
                    final PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, trainer.getFirstName());
                    ps.setString(2, trainer.getLastName());
                    ps.setString(3, trainer.getEmail());
                    ps.setString(4, trainer.getPhoneNumber());
                    ps.setBoolean(5, trainer.isActive());
                    ps.setInt(6, trainer.getDefaultSalary());
                    return ps;
                },
                key);
        trainer.setEntityId((Integer) Objects.requireNonNull(key.getKeys()).get("entity_id"));
        return trainer;
    }

    public boolean updateTrainer(Trainer trainer) {
        String query = "UPDATE Trainer " +
                "SET first_name = ?, last_name = ?, email = ?, phone = ?, active = ?, default_salary = ? " +
                "WHERE entity_id = ?";
        return 0 < jdbcTemplate.update(
                query,
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.getEmail(),
                trainer.getPhoneNumber(),
                trainer.isActive(),
                trainer.getDefaultSalary(),
                trainer.getEntityId()
        );
    }

    public List<Trainer> getAllTrainers() {
        String query = "SELECT * FROM Trainer";
        return jdbcTemplate.query(query, TRAINER_ROW_MAPPER);
    }

    public List<Trainer> getAllActiveTrainers() {
        String query = "SELECT * FROM Trainer WHERE active = true";
        return jdbcTemplate.query(query, TRAINER_ROW_MAPPER);
    }

    public List<Trainer> getAllInactiveTrainers() {
        String query = "SELECT * FROM Trainer WHERE  active = false";
        return jdbcTemplate.query(query, TRAINER_ROW_MAPPER);
    }

    static class TrainerRowMapper implements RowMapper<Trainer> {
        @Override
        public Trainer mapRow(ResultSet rs, int rowNum) throws SQLException {
            Trainer trainer = new Trainer();
            trainer.setEntityId(rs.getInt("entity_id"));
            trainer.setFirstName(rs.getString("first_name"));
            trainer.setLastName(rs.getString("last_name"));
            trainer.setPhoneNumber(rs.getString("phone"));
            trainer.setEmail(rs.getString("email"));
            trainer.setActive(rs.getBoolean("active"));
            trainer.setDefaultSalary(rs.getInt("default_salary"));
            return trainer;
        }


    }
}
