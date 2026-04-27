package pl.lodz.p.library.ports.inbound;

import pl.lodz.p.library.domain.model.Client;

import java.util.List;

public interface ClientUseCase {
    Client findClientById(String id);

    List<Client> findAllClients();

    Client addClient(Client client);

    void deleteClient(String id);
}