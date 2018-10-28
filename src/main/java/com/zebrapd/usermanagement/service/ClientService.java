package com.zebrapd.usermanagement.service;

import com.zebrapd.usermanagement.entity.Client;
import com.zebrapd.usermanagement.error.exception.ValidationException;
import com.zebrapd.usermanagement.repositoty.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static java.util.Objects.nonNull;

@Service
public class ClientService {

    private ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client getClientById(int id) {
        return clientRepository.getClientById(id);
    }

    public Client getClientByEmail(String email) {
        return clientRepository.getClientByEmail(email);
    }

    public Client createClient(Client client) {
        validateEmail(client.getEmail());
        validatePhoneNumber(client.getPhoneNumber());
        return clientRepository.createClient(client);
    }

    public boolean updateClient(Client client) {
        validateDuplications(client);
        return clientRepository.updateClient(client);
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

    private void validateDuplications(Client client) {
        Client clientByEmail = clientRepository.getClientByEmail(client.getEmail());
        if(nonNull(clientByEmail) && !Objects.equals(clientByEmail.getEntityId(), client.getEntityId())){
            throw new ValidationException("Trainer with such email already exists: " + client.getEmail());
        }
        Client clientByPhone = clientRepository.getClientByPhone(client.getPhoneNumber());
        if(nonNull(clientByPhone) && !Objects.equals(clientByPhone.getEntityId(), client.getEntityId())){
            throw new ValidationException("Trainer with such email already exists: " + client.getEmail());
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        Client clientByPhone = clientRepository.getClientByPhone(phoneNumber);
        if (nonNull(clientByPhone)) {
            throw new ValidationException("Client with such phone number already exists: " + clientByPhone.getPhoneNumber());
        }
    }

    private void validateEmail(String email) {
        Client clientByEmail = clientRepository.getClientByEmail(email);
        if (nonNull(clientByEmail)) {
            throw new ValidationException("Client with such email already exists: " + clientByEmail.getEmail());
        }
    }

}
