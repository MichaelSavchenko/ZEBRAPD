package com.zebrapd.usermanagement.service;

import com.zebrapd.usermanagement.entity.Client;
import com.zebrapd.usermanagement.error.exception.ValidationException;
import com.zebrapd.usermanagement.repositoty.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ClientService {

    private ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client getClientById(int id){
        return clientRepository.getClientById(id);
    }

    public Client getClientByEmail(String email){
        return clientRepository.getClientByEmail(email);
    }

    public Client createClient(Client client) {
        Client clientByEmail = clientRepository.getClientByEmail(client.getEmail());
        validateEmail(clientByEmail, client.getEmail());
        Client clientByPhone = clientRepository.getClientByPhone(client.getPhoneNumber());
        validatePhoneNumber(client, clientByPhone);
        return clientRepository.createClient(client);
    }

    public boolean deactivateClient(int clientId){
        return clientRepository.deactivateClient(clientId);
    }

    public List<Client> getAllClients() {
        return clientRepository.getAllClients();
    }

    public List<Client> getAllActiveClients() {
        return clientRepository.getAllActiveClients();
    }

    public List<Client> getAllInactiveClients() {
        return clientRepository.getAllInactiveClients();
    }

    public Client getClientByPhoneNumber(String phoneNumber){
        return clientRepository.getClientByPhone(phoneNumber);
    }

    private void validatePhoneNumber(Client client, Client clientByPhone) {
        if (Objects.nonNull(clientByPhone)){
            throw new ValidationException("Client with such phone number already exists: " + client.getPhoneNumber());
        }
    }

    private void validateEmail(Client clientByEmail, String email) {
        if (Objects.nonNull(clientByEmail)) {
            throw new ValidationException("Client with such email already exists: " + email);
        }
    }

}
