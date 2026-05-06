package pl.lodz.p.user.adapters.rest.controllers;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.lodz.p.user.adapters.rest.dto.ReaderDTO;
import pl.lodz.p.user.adapters.rest.security.JwtService;
import pl.lodz.p.user.domain.model.Reader;
import pl.lodz.p.user.ports.inbound.UserUseCase;

import java.util.Collections;
import java.util.UUID;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(ReaderController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ReaderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserUseCase userUseCase;

    @MockitoBean
    private JwtService jwtService;

    private Reader reader;
    private ReaderDTO readerDTO;
    private UUID readerId;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);

        readerId = UUID.randomUUID();
        reader = new Reader("testuser", "pass", "test@example.com", "Tomasz", "Zieliński", 25);
        reader.setId(readerId);
        reader.setActive(true);

        readerDTO = new ReaderDTO("testuser", "test@example.com", "Tomasz", "Zieliński", 25, true);
        readerDTO.setId(readerId);
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void getAllReaders_ShouldReturnList() {
        when(userUseCase.findAllUsers()).thenReturn(Collections.singletonList(reader));

        given()
                .when()
                .get("/readers")
                .then()
                .status(org.springframework.http.HttpStatus.OK)
                .body("$", hasSize(1))
                .body("[0].login", equalTo("testuser"));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void getReaderById_ShouldReturnReader() {
        when(userUseCase.findUserById(readerId)).thenReturn(reader);
        when(jwtService.generateSignatureForId(readerId)).thenReturn("mock-signature");

        given()
                .when()
                .get("/readers/" + readerId)
                .then()
                .status(org.springframework.http.HttpStatus.OK)
                .header("ETag", "\"mock-signature\"")
                .body("login", equalTo("testuser"));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void addReader_ShouldCreateReader() {
        when(userUseCase.addUser(any(Reader.class))).thenReturn(reader);

        given()
                .contentType("application/json")
                .body(readerDTO)
                .when()
                .post("/readers")
                .then()
                .status(org.springframework.http.HttpStatus.CREATED)
                .body("login", equalTo("testuser"));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void updateReader_ShouldUpdateReader() {
        when(jwtService.verifySignature(eq(readerId), anyString())).thenReturn(true);
        when(userUseCase.updateUser(eq(readerId), any(Reader.class))).thenReturn(reader);

        given()
                .header("If-Match", "mock-signature")
                .contentType("application/json")
                .body(readerDTO)
                .when()
                .post("/readers/" + readerId)
                .then()
                .status(org.springframework.http.HttpStatus.OK)
                .body("login", equalTo("testuser"));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void activateReader_ShouldActivate() {
        when(userUseCase.findUserById(readerId)).thenReturn(reader);
        when(userUseCase.activateUser(readerId)).thenReturn(reader);

        given()
                .when()
                .post("/readers/" + readerId + "/activate")
                .then()
                .status(org.springframework.http.HttpStatus.OK)
                .body("login", equalTo("testuser"));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void deactivateReader_ShouldDeactivate() {
        when(userUseCase.findUserById(readerId)).thenReturn(reader);
        when(userUseCase.deactivateUser(readerId)).thenReturn(reader);

        given()
                .when()
                .post("/readers/" + readerId + "/deactivate")
                .then()
                .status(org.springframework.http.HttpStatus.OK)
                .body("login", equalTo("testuser"));
    }
}