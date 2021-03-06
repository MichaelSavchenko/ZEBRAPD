package com.zebrapd.usermanagement.repositoty;

import com.zebrapd.usermanagement.entity.Subscription;
import com.zebrapd.usermanagement.entity.TrainingReceiptType;
import com.zebrapd.usermanagement.entity.TrainingType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.*;
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
                "date_of_sale," +
                " type," +
                " number_of_trainings," +
                " price," +
                " start_date," +
                " expiration_date)" +
                " VALUES (?,?,?,?,?,?,?)";

        jdbcTemplate.update(con -> {
            final PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, subscription.getClientId());
            ps.setDate(2, Date.valueOf(subscription.getDateOfSale()));
            ps.setString(3, subscription.getType().name());
            ps.setInt(4, subscription.getNumberOfTrainings());
            ps.setInt(5, subscription.getPrice());
            ps.setDate(6, Date.valueOf(subscription.getStartDate()));
            ps.setDate(7, Date.valueOf(subscription.getExpirationDate()));
            return ps;
        }, key);
        subscription.setEntityId((Integer) Objects.requireNonNull(key.getKeys()).get("entity_id"));
        return subscription;
    }

    public boolean setNumberOfTrainings(int numberOfTrainings, int licenseId) {
        String query = "UPDATE Subscription set number_of_trainings = ? WHERE entity_id = ?";
        return 0 < jdbcTemplate.update(query, numberOfTrainings, licenseId);
    }

    public List<Subscription> getActiveSubscriptions(int clientId, TrainingType trainingType) {
        String query = "SELECT * FROM Subscription" +
                " WHERE client_id = ?" +
                " AND type = ?" +
                "AND expiration_date >= now()::date";
        return jdbcTemplate.query(query, SUBSCRIPTION_ROW_MAPPER, clientId, trainingType.name());
    }

    public List<Subscription> getNotExpiredSubscription(int clientId) {
        String query = "SELECT * FROM Subscription" +
                " WHERE client_id = ?" +
                " AND number_of_trainings > 0" +
                " AND expiration_date > now()::date";
        return jdbcTemplate.query(query, SUBSCRIPTION_ROW_MAPPER, clientId);
    }


    static class SubscriptionRowMapper implements RowMapper<Subscription> {

        @Override
        public Subscription mapRow(ResultSet rs, int rowNum) throws SQLException {
            Subscription subscription = new Subscription();
            subscription.setEntityId(rs.getInt("entity_id"));
            subscription.setDateOfSale(rs.getDate("date_of_sale").toLocalDate());
            subscription.setClientId(rs.getInt("client_id"));
            subscription.setType(TrainingType.valueOf(rs.getString("type")));
            subscription.setNumberOfTrainings(rs.getInt("number_of_trainings"));
            subscription.setPrice(rs.getInt("price"));
            subscription.setStartDate(rs.getDate("start_date").toLocalDate());
            subscription.setExpirationDate(rs.getDate("expiration_date").toLocalDate());
            return subscription;
        }
    }
}
