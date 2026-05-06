package pl.lodz.p.library.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.lodz.p.library.domain.exceptions.ClientException;
import pl.lodz.p.library.domain.model.Client;

import java.util.List;
import java.util.UUID;

class ClientServiceTest extends BaseServiceTest {

    @Autowired
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        super.cleanUp();
    }

    @Test
    void addClientTest() {
        Client client = new Client("Jan", "Kowalski", "jan@test.pl", 25);
        Client savedClient = clientService.addClient(client);

        Assertions.assertNotNull(savedClient.getId());
        Assertions.assertEquals("Jan", savedClient.getFirstName());
        Assertions.assertEquals("Kowalski", savedClient.getLastName());
        Assertions.assertEquals(1, clientService.findAllClients().size());
    }

    @Test
    void findClientByIdTest() {
        Client client = new Client("Anna", "Nowak", "anna@test.pl", 30);
        Client savedClient = clientPort.save(client);

        Client foundClient = clientService.findClientById(savedClient.getId());

        Assertions.assertNotNull(foundClient);
        Assertions.assertEquals("Anna", foundClient.getFirstName());
    }

    @Test
    void findClientByIdFailNotFoundTest() {
        Assertions.assertThrows(ClientException.class, () -> clientService.findClientById(UUID.randomUUID()));
    }

    @Test
    void findAllClientsTest() {
        clientPort.save(new Client("Jan", "Kowalski", "jan@test.pl", 25));
        clientPort.save(new Client("Anna", "Nowak", "anna@test.pl", 30));

        List<Client> clients = clientService.findAllClients();
        Assertions.assertEquals(2, clients.size());
    }

    @Test
    void deleteClientTest() {
        Client client = new Client("Piotr", "Zieliński", "piotr@test.pl", 40);
        Client savedClient = clientPort.save(client);

        clientService.deleteClient(savedClient.getId());

        Assertions.assertThrows(ClientException.class, () -> clientService.findClientById(savedClient.getId()));
    }
}