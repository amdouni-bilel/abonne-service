package org.bibliotheque.abonneservice.mapper;


import lombok.RequiredArgsConstructor;
import org.bibliotheque.abonneservice.dto.ClientDto;
import org.bibliotheque.abonneservice.entity.Client;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientMapper {

    private final ModelMapper modelMapper;



    public ClientDto toDto(Client client) {
        return modelMapper.map(client, ClientDto.class);
    }

    public Client toEntity(ClientDto clientDto) {
        return modelMapper.map(clientDto, Client.class);
    }
}

