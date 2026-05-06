package pl.lodz.p.library.services;

import org.springframework.stereotype.Service;
import pl.lodz.p.library.domain.exceptions.ClientException;
import pl.lodz.p.library.domain.model.Client;
import pl.lodz.p.library.ports.inbound.ClientUseCase;
import pl.lodz.p.library.ports.outbound.ClientPort;

import java.util.List;
import java.util.UUID;

@Service
public class ClientService implements ClientUseCase {
    private final ClientPort clientPort;

    public ClientService(ClientPort clientPort) {
        this.clientPort = clientPort;
    }

    @Override
    public Client findClientById(UUID id) {
        return clientPort.findById(id)
                .orElseThrow(() -> new ClientException("Client with id " + id + " not found."));
    }

    @Override
    public List<Client> findAllClients() {
        return clientPort.findAll();
    }

    @Override
    public Client addClient(Client client) {
        return clientPort.save(client);
    }

    @Override
    public void deleteClient(UUID id) {
        clientPort.deleteById(id);
    }
}