package com.zebrapd.usermanagement.repositoty;

import com.zebrapd.usermanagement.entity.Training;
import com.zebrapd.usermanagement.entity.TrainingType;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
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
        String trainingQuery = "INSERT INTO Training (date_time, trainer_id, type, receipts) VALUES (?,?,?,?)";

        jdbcTemplate.update(con -> {
            final PreparedStatement ps = con.prepareStatement(trainingQuery, Statement.RETURN_GENERATED_KEYS);
            long milli = training.getLocalDateTime().atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
            ps.setTimestamp(1, new Timestamp(milli));
            ps.setInt(2, training.getTrainerId());
            ps.setString(3, training.getTrainingType().name());
            ps.setInt(4, training.getReceipts());
            return ps;
        }, keyHolder);

        int trainingId = (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("entity_id");

        String trainingClientQuery = "INSERT INTO training_client (training_id, client_id) VALUES (?,?)";
        jdbcTemplate.batchUpdate(trainingClientQuery,
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setInt(1, trainingId);
                    ps.setInt(2, training.getClientIds().get(i));
                }

                @Override
                public int getBatchSize() {
                    return training.getClientIds().size();
                }
            });
        training.setEntityId(trainingId);
        return training;
    }

    public void setTrainingPrice(TrainingType type, int price) {
        String query = "UPDATE training_price SET price = ? WHERE training_type = ?";
        jdbcTemplate.update(query, price, type.name());
    }

    public Integer getTrainingPrice(TrainingType type) {
        String query = "SELECT price FROM training_price WHERE training_type = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{type.name()}, Integer.class);
    }
}
