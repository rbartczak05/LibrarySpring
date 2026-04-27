package pl.lodz.p.library.adapters.rest.controllers;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.lodz.p.library.adapters.rest.dto.ClientDTO;
import pl.lodz.p.library.adapters.rest.security.JwtService;
import pl.lodz.p.library.domain.model.Client;
import pl.lodz.p.library.ports.inbound.ClientUseCase;

import java.util.Collections;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(ClientController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientUseCase clientUseCase;

    @MockitoBean
    private JwtService jwtService;

    private Client client;
    private ClientDTO clientDTO;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);

        client = new Client("Jan", "Kowalski", "jan@test.pl", 25);
        client.setId("1");
        client.setActive(true);

        clientDTO = new ClientDTO();
        clientDTO.setId("1");
        clientDTO.setFirstName("Jan");
        clientDTO.setLastName("Kowalski");
        clientDTO.setEmail("jan@test.pl");
        clientDTO.setAge(25);
        clientDTO.setActive(true);
    }

    @Test
    @WithMockUser
    void getAllClients_ShouldReturnList() {
        when(clientUseCase.findAllClients()).thenReturn(Collections.singletonList(client));

        given()
                .when()
                .get("/clients")
                .then()
                .status(org.springframework.http.HttpStatus.OK)
                .body("_embedded.clients", hasSize(1))
                .body("_embedded.clients[0].firstName", equalTo("Jan"));
    }

    @Test
    @WithMockUser
    void getClientById_ShouldReturnClient() {
        when(clientUseCase.findClientById("1")).thenReturn(client);

        given()
                .when()
                .get("/clients/1")
                .then()
                .status(org.springframework.http.HttpStatus.OK)
                .body("firstName", equalTo("Jan"));
    }

    @Test
    @WithMockUser
    void addClient_ShouldCreateClient() {
        when(clientUseCase.addClient(any(Client.class))).thenReturn(client);

        given()
                .contentType("application/json")
                .body(clientDTO)
                .when()
                .post("/clients")
                .then()
                .status(org.springframework.http.HttpStatus.CREATED)
                .body("firstName", equalTo("Jan"));
    }

    @Test
    @WithMockUser
    void deleteClient_ShouldReturnOk() {
        given()
                .when()
                .delete("/clients/1")
                .then()
                .status(org.springframework.http.HttpStatus.OK);
    }
}