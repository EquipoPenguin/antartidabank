package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(value = "/api")
public class ClientController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(value = "/clients")
    public List<ClientDTO> getClients() {
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }

    @RequestMapping(value = "/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    }

    @GetMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication) {
        Client client = this.clientRepository.findByEmail(authentication.getName());
        return new ClientDTO(client);
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    //Crea un nuevo servicio para registrar un cliente
    @PostMapping("/clients")
    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (clientRepository.findByEmail(email) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        Client client = clientRepository.save(new Client(firstName, lastName, email, passwordEncoder.encode(password)));

        accountRepository.save(new Account(
                "VIN"+String.format("%08d",getRandomNumber(1,100000000)),
                LocalDateTime.now(),
                0,
                client));

        return new ResponseEntity<>(HttpStatus.CREATED);

    }
}
