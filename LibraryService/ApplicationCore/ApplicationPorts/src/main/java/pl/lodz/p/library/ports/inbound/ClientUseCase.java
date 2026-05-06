package pl.lodz.p.library.ports.inbound;

import pl.lodz.p.library.domain.model.Client;

import java.util.List;
import java.util.UUID;

public interface ClientUseCase {
    Client findClientById(UUID id);

    List<Client> findAllClients();

    Client addClient(Client client);

    void deleteClient(UUID id);
}