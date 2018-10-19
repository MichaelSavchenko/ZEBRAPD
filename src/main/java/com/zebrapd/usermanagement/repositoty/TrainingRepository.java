package com.zebrapd.usermanagement.repositoty;

import com.zebrapd.usermanagement.entity.Training;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneOffset;
import java.util.Objects;

@Service
public class TrainingRepository {

    private JdbcTemplate jdbcTemplate;

    public TrainingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public Training createTraining(Training training){
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String trainingQuery = "INSERT INTO Training (date_time, trainer_id, type) VALUES (?,?,?)";

        jdbcTemplate.update(con -> {
            final PreparedStatement ps = con.prepareStatement(trainingQuery, Statement.RETURN_GENERATED_KEYS);
            long milli = training.getLocalDateTime().atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
            ps.setDate(1, new Date(milli));
            ps.setInt(2, training.getTrainer().getEntityId());
            ps.setString(3, training.getTrainingType().name());
            return ps;
        }, keyHolder);

        int trainingId = (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("entity_id");

        String trainingClientQuery = "INSERT INTO training_client (training_id, client_id) VALUES (?,?)";
        jdbcTemplate.batchUpdate(trainingClientQuery,
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setInt(1, trainingId);
                    ps.setInt(2, training.getClients().get(i).getEntityId());
                }

                @Override
                public int getBatchSize() {
                    return training.getClients().size();
                }
            });
        training.setEntityId(trainingId);
        return training;
    }
}
