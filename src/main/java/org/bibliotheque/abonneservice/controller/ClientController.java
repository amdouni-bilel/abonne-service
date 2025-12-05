package org.bibliotheque.abonneservice.controller;


import lombok.RequiredArgsConstructor;
import org.bibliotheque.abonneservice.dto.ClientDto;
import org.bibliotheque.abonneservice.entity.Client;
import org.bibliotheque.abonneservice.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {


    @Autowired
    ClientService clientService;


    @PostMapping
    public ResponseEntity<Client> addClient(@RequestBody ClientDto clientDto) {
        Client client = clientService.addClient(clientDto);
        return new ResponseEntity<>(client, HttpStatus.CREATED);
    }

}
