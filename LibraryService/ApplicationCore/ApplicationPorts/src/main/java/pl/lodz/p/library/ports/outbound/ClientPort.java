package pl.lodz.p.library.ports.outbound;

import pl.lodz.p.library.domain.model.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientPort {
    Optional<Client> findById(UUID id);

    List<Client> findAll();

    Client save(Client client);

    void deleteById(UUID id);

    void deleteAll();
}