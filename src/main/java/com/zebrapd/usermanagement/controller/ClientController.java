package com.zebrapd.usermanagement.controller;

import com.zebrapd.usermanagement.entity.Client;
import com.zebrapd.usermanagement.service.ClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {

    private ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/getClientById/{clientId}")
    public Client getClientById(@PathVariable int clientId){
        return clientService.getClientById(clientId);
    }

    @GetMapping("getClientByEmail/{email}")
    public Client getClientByEmail(@PathVariable String email){
        return clientService.getClientByEmail(email);
    }

    @GetMapping("getClientByPhone/{phoneNumber}")
    public Client getClientByPhoneNumber(@PathVariable String phoneNumber){
        return clientService.getClientByEmail(phoneNumber);
    }

    @GetMapping("/getAll")
    public List<Client> getAllClients(){
        return clientService.getAllClients();
    }

    @GetMapping("/getAllActive")
    public List<Client> getAllActiveClients(){
        return clientService.getAllActiveClients();
    }

    @GetMapping("/getAllInactive")
    public List<Client> getAllInactiveClients(){
        return clientService.getAllInactiveClients();
    }

    @PostMapping("/create")
    public Client createClient(@RequestBody Client client) {
        return clientService.createClient(client);
    }

    @PostMapping("/update}")
    public boolean updateClient(@RequestBody Client client){
        return clientService.updateClient(client);
    }
}
