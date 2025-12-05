package org.bibliotheque.abonneservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bibliotheque.abonneservice.dto.ClientDto;
import org.bibliotheque.abonneservice.entity.Client;
import org.bibliotheque.abonneservice.mapper.ClientMapper;
import org.bibliotheque.abonneservice.repository.ClientRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final KafkaTemplate<String, ClientDto> kafkaTemplate;

    public Client addClient(ClientDto clientDto) {

        Client client = clientMapper.toEntity(clientDto);

        clientRepository.save(client);
        kafkaTemplate.send("client-topic", clientDto);

        return client;
    }


}


















/*@Service
@Slf4j
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final AgencyRepository agencyRepository;
    private final ClientMapper clientMapper;
    private final KafkaTemplate<String, ClientDto> kafkaTemplate;

    public Client addClient(ClientDto clientDto) {
        validateEmail(clientDto.getEmail());
        Client client = clientMapper.toEntity(clientDto);
        handleAgency(clientDto, client);
        clientRepository.save(client);
        kafkaTemplate.send("client-topic", clientDto);
        return client;
    }

    private void validateEmail(String email) {
        if (clientRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("The email already exists!");
        }
    }

    private void handleAgency(ClientDto clientDto, Client client) {
        if (StringUtils.isNotBlank(clientDto.getAgencyName())) {
            Agency agency = new Agency();
            agency.setName(clientDto.getAgencyName());
            agencyRepository.save(agency);
            client.setAgencyManager(true);
            client.setAgencies(Collections.singletonList(agency));
        } else {
            client.setAgencyManager(false);
            client.setAgencies(null);
        }
    }

}*/
