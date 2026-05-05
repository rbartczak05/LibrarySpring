package pl.lodz.p.library.ports.outbound;

import pl.lodz.p.library.domain.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientPort {
    Optional<Client> findById(String id);

    List<Client> findAll();

    Client save(Client client);

    void deleteById(String id);

    void deleteAll();
}