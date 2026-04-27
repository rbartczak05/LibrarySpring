package pl.lodz.p.library.adapters.mongo.mappers;

import org.springframework.stereotype.Component;
import pl.lodz.p.library.adapters.mongo.documents.ClientDoc;
import pl.lodz.p.library.domain.model.Client;

@Component
public class ClientMapper {

    public Client toDomain(ClientDoc doc) {
        if (doc == null) return null;

        Client client = new Client(doc.getFirstName(), doc.getLastName(), doc.getEmail(), doc.getAge());
        client.setId(doc.getId());
        client.setActive(doc.isActive());
        client.setCurrentLoansCount(doc.getCurrentLoansCount());

        return client;
    }

    public ClientDoc toDocument(Client client) {
        if (client == null) return null;

        ClientDoc doc = new ClientDoc(client.getFirstName(), client.getLastName(), client.getEmail(), client.getAge());
        doc.setId(client.getId());
        doc.setActive(client.isActive());
        doc.setCurrentLoansCount(client.getCurrentLoansCount());

        return doc;
    }
}