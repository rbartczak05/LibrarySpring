package pl.lodz.p.library.adapters.soap.converters;

import pl.lodz.p.library.adapters.soap.dto.client.ClientDTO;
import pl.lodz.p.library.domain.model.Client;

public class ClientSoapConverter {
    private ClientSoapConverter() {
    }

    public static ClientDTO toDTO(Client client) {
        if (client == null) return null;
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setFirstName(client.getFirstName());
        dto.setLastName(client.getLastName());
        dto.setEmail(client.getEmail());
        dto.setAge(client.getAge());
        dto.setActive(client.isActive());
        dto.setCurrentLoansCount(client.getCurrentLoansCount());
        return dto;
    }

    public static Client fromDTO(ClientDTO dto) {
        if (dto == null) return null;
        Client client = new Client(dto.getFirstName(), dto.getLastName(), dto.getEmail(), dto.getAge());
        client.setId(dto.getId());
        client.setActive(dto.isActive());
        client.setCurrentLoansCount(dto.getCurrentLoansCount());
        return client;
    }
}