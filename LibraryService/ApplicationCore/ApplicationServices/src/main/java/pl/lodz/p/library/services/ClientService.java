package pl.lodz.p.library.services;

import io.micrometer.core.annotation.Timed;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Timed(value = "library.client.register.time", description = "Czas rejestracji klienta z kolejki MQ")
    @Transactional
    @Override
    public Client registerClientFromEvent(UUID id, String firstName, String lastName, String email, int age) {
        if (clientPort.findById(id).isPresent()) {
            return clientPort.findById(id).get();
        }

        Client client = new Client(firstName, lastName, email, age);
        client.setId(id);
        client.setActive(true);

        return addClient(client);
    }

    @Override
    public void deleteClient(UUID id) {
        clientPort.deleteById(id);
    }
}