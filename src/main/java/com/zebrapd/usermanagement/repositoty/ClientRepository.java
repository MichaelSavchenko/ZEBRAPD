package com.zebrapd.usermanagement.repositoty;

import com.zebrapd.usermanagement.entity.Client;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.List;
import java.util.Objects;

@Service
public class ClientRepository {

    private JdbcTemplate jdbcTemplate;
    private static ClientRowMapper CLIENT_ROW_MAPPER = new ClientRowMapper();

    public ClientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Client getClientById(int clientId) {
        String query = "SELECT * FROM Client WHERE entity_id = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{clientId}, CLIENT_ROW_MAPPER);
    }

    public Client getClientByEmail(String email) {
        String query = "SELECT * FROM Client WHERE email = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{email}, CLIENT_ROW_MAPPER);
    }

    public Client getClientByPhone(String phoneNumber){
        String query = "SELECT * FROM Client WHERE phone = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{phoneNumber}, CLIENT_ROW_MAPPER);
    }

    public Client createClient(Client client) {
        KeyHolder key = new GeneratedKeyHolder();
        String query = "INSERT INTO Client (first_name, last_name, email, phone, active) Values (?,?,?,?,?)";

        jdbcTemplate.update(
            con -> {
                final PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, client.getFirstName());
                ps.setString(2, client.getLastName());
                ps.setString(3, client.getEmail());
                ps.setString(4, client.getPhoneNumber());
                ps.setBoolean(5, client.isActive());
                return ps;
            },
            key);
        client.setEntityId((Integer) Objects.requireNonNull(key.getKeys()).get("entity_id"));
        return client;
    }

    public boolean deactivateClient(int clientId){
        String query = "UPDATE Client SET active = false WHERE entity_id = ?";
        int updatedRowsNumber = jdbcTemplate.update(query, clientId);
        return updatedRowsNumber > 0;
    }

    public List<Client> getAllClients() {
        String query = "SELECT * FROM Client";
        return jdbcTemplate.query(query, CLIENT_ROW_MAPPER);
    }

    public List<Client> getAllActiveClients() {
        String query = "SELECT * FROM Client WHERE active = true";
        return jdbcTemplate.query(query, CLIENT_ROW_MAPPER);
    }

    public List<Client> getAllInactiveClients() {
        String query = "SELECT * FROM Client WHERE active = false";
        return jdbcTemplate.query(query, CLIENT_ROW_MAPPER);
    }

    static class ClientRowMapper implements RowMapper<Client> {

        @Override
        public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
            Client client = new Client();
            client.setEntityId(rs.getInt("entity_id"));
            client.setFirstName(rs.getString("first_name"));
            client.setLastName(rs.getString("last_name"));
            client.setPhoneNumber(rs.getString("phone"));
            client.setEmail(rs.getString("email"));
            client.setActive(rs.getBoolean("active"));
            return client;
        }
    }
}
