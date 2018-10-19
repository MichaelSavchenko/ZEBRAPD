package com.zebrapd.usermanagement.repositoty;

import com.zebrapd.usermanagement.entity.Subscription;
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
public class SubscriptionRepository {

    private static SubscriptionRowMapper SUBSCRIPTION_ROW_MAPPER = new SubscriptionRowMapper();
    private JdbcTemplate jdbcTemplate;

    public SubscriptionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Subscription createSubscription(Subscription subscription) {
        KeyHolder key = new GeneratedKeyHolder();
        String query = "INSERT INTO Subscription (" +
            "client_id," +
            " type," +
            " number_of_trainings," +
            " price," +
            " start_date," +
            " expiration_date)" +
            " VALUES (?,?,?,?,?,?)";

        jdbcTemplate.update(con -> {
            final PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, subscription.getClientId());
            ps.setString(2, subscription.getType().name());
            ps.setInt(3, subscription.getPrice());
            ps.setInt(4, subscription.getNumberOfTrainings());
            ps.setDate(5, Date.valueOf(subscription.getStartDate()));
            ps.setDate(6, Date.valueOf(subscription.getExpirationDate()));
            return ps;
        }, key);
        subscription.setEntityId((Integer) Objects.requireNonNull(key.getKeys()).get("entity_id"));
        return subscription;
    }

    public boolean setNumberOfTrainings(int numberOfTrainings, int licenseId) {
        String query = "UPDATE Subscription set number_of_trainings = ? WHERE entity_id = ?";
        return 0 < jdbcTemplate.update(query, numberOfTrainings, licenseId);
    }

    public List<Subscription> getActiveSubscriptionByType(int clientId, TrainingType type){
        String query = "SELECT * FROM Subscription" +
            " WHERE client_id = ?" +
            " AND type = ?" +
            " AND number_of_trainings > 0" +
            " AND expiration_date > ?";
        Date date = Date.valueOf(LocalDate.now());
        return jdbcTemplate.query(query, SUBSCRIPTION_ROW_MAPPER, clientId, type.name(), date);
    }

    public List<Subscription> getNotExpiredSubscription(int clientId){
        String query = "SELECT * FROM Subscription" +
            " WHERE client_id = ?" +
            " AND number_of_trainings > 0" +
            " AND expiration_date > ?";
        Date date = Date.valueOf(LocalDate.now());
        return jdbcTemplate.query(query, SUBSCRIPTION_ROW_MAPPER, clientId, date);
    }



    static class SubscriptionRowMapper implements RowMapper<Subscription>{

        @Override
        public Subscription mapRow(ResultSet rs, int rowNum) throws SQLException {
            Subscription subscription = new Subscription();
            subscription.setEntityId(rs.getInt("entity_id"));
            subscription.setClientId(rs.getInt("client_id"));
            subscription.setType(TrainingType.valueOf(rs.getString("type")));
            subscription.setPrice(rs.getInt("price"));
            subscription.setStartDate(rs.getDate("start_date").toLocalDate());
            subscription.setExpirationDate(rs.getDate("expiration_date").toLocalDate());
            return subscription;
        }
    }
}
