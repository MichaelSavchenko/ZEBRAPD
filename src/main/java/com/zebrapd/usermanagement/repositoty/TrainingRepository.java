package com.zebrapd.usermanagement.repositoty;

import com.zebrapd.usermanagement.entity.Training;
import com.zebrapd.usermanagement.entity.TrainingReceiptType;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import static com.zebrapd.usermanagement.entity.TrainingType.*;
import static java.util.Objects.*;

@Service
public class TrainingRepository {

    private JdbcTemplate jdbcTemplate;

    public TrainingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public Training createTraining(Training training) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String trainingQuery = "INSERT INTO Training (date_time, trainer_id, type, receipts) VALUES (?,?,?,?)";

        jdbcTemplate.update(con -> {
            final PreparedStatement ps = con.prepareStatement(trainingQuery, Statement.RETURN_GENERATED_KEYS);
            long milli = training.getLocalDateTime().atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
            ps.setTimestamp(1, new Timestamp(milli));
            ps.setInt(2, training.getTrainerId());
            ps.setString(3, training.getTrainingReceiptType().name());
            ps.setInt(4, training.getReceipts());
            return ps;
        }, keyHolder);

        final int trainingId = (Integer) requireNonNull(keyHolder.getKeys()).get("entity_id");
        final List<Integer> clientIds = training.getClientIds();

        addClientsToTraining(trainingId, clientIds);
        training.setEntityId(trainingId);
        return training;
    }

    public void addClientsToTraining(int trainingId, List<Integer> clientIds) {
        String trainingClientQuery = "INSERT INTO training_client (training_id, client_id) VALUES (?,?)";
        jdbcTemplate.batchUpdate(trainingClientQuery,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, trainingId);
                        ps.setInt(2, clientIds.get(i));
                    }

                    @Override
                    public int getBatchSize() {
                        return clientIds.size();
                    }
                });
    }

    public Training getTraining(int trainingId) {
        String query = "SELECT t.entity_id, t.date_time, t.trainer_id, t.type, t.receipts,  array_agg(tc.client_id) as client_ids" +
                "    FROM training as t inner join training_client as tc ON t.entity_id = tc.training_id" +
                "    WHERE t.entity_id = ?" +
                "    GROUP BY t.entity_id";

        try {
            return jdbcTemplate.queryForObject(query, new Object[]{trainingId}, (rs, rowNum) -> {
                Training training = new Training();
                training.setEntityId(rs.getInt("entity_id"));
                training.setLocalDateTime(rs.getTimestamp("date_time").toLocalDateTime());
                training.setTrainerId(rs.getInt("trainer_id"));
                training.setTrainingReceiptType(TrainingReceiptType.valueOf(rs.getString("type")));
                training.setReceipts(rs.getInt("receipts"));
                Array clientIds = rs.getArray("client_ids");
                if (!isNull(clientIds)) {
                    training.setClientIds(Arrays.asList((Integer[]) clientIds.getArray()));
                }
                return training;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void setTrainingPrice(TrainingReceiptType type, int price) {
        String query = "UPDATE training_price SET price = ? WHERE training_type = ?";
        jdbcTemplate.update(query, price, type.name());
    }

    public Integer getTrainingReceipt(TrainingReceiptType type) {
        String query = "SELECT price FROM training_price WHERE training_type = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{type.name()}, Integer.class);
    }

    public void removeClientFromTraining(int trainingId, int clientId) {
        String query = "DELETE FROM training_client WHERE training_id = ? AND client_id = ?";
        jdbcTemplate.update(query, trainingId, clientId);
    }

    public int getClientsCount(int trainingId) {
        String query = "SELECT COUNT(*) FROM training_client WHERE training_id = ?";
        Integer count = jdbcTemplate.queryForObject(query, new Object[]{trainingId}, Integer.class);
        if (count == null) {
            return 0;
        } else {
            return count;
        }
    }

    public void updateTraining(Training training) {
        String query =
                "UPDATE training " +
                        "SET " +
                        "date_time = ?, " +
                        "trainer_id = ?, " +
                        "type = ?, " +
                        "receipts = ? " +
                        "WHERE entity_id = ?";
        long trainingDateTime = training.getLocalDateTime().atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
        jdbcTemplate.update(
                query,
                new Timestamp(trainingDateTime),
                training.getTrainerId(),
                training.getTrainingReceiptType().name(),
                training.getReceipts(),
                training.getEntityId()
        );
    }
}
