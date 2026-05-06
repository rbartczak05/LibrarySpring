package pl.lodz.p.library.adapters.mongo.aggregates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.library.adapters.mongo.documents.ClientDoc;
import pl.lodz.p.library.adapters.mongo.mappers.ClientMapper;
import pl.lodz.p.library.adapters.mongo.repositories.ClientRepository;
import pl.lodz.p.library.domain.model.Client;
import pl.lodz.p.library.ports.outbound.ClientPort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ClientRepositoryAdapter implements ClientPort {

    private final ClientRepository repository;
    private final ClientMapper mapper;

    @Autowired
    public ClientRepositoryAdapter(ClientRepository repository, ClientMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Client> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Client> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Client save(Client client) {
        if (client.getId() == null) {
            client.setId(UUID.randomUUID());
        }
        ClientDoc doc = mapper.toDocument(client);
        ClientDoc saved = repository.save(doc);
        return mapper.toDomain(saved);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}