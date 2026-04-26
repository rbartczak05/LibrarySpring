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
import pl.lodz.p.library.adapters.rest.dto.ReaderDTO;
import pl.lodz.p.library.adapters.rest.security.JwtService;
import pl.lodz.p.library.domain.model.Reader;
import pl.lodz.p.library.ports.inbound.UserUseCase;

import java.util.Collections;

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

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        reader = new Reader("testuser", "password", "test@example.com", 25);
        reader.setId("1");
        reader.setActive(true);

        readerDTO = new ReaderDTO("1", "testuser", "test@example.com", 25, true, "READER", 0);
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
        when(userUseCase.findUserById("1")).thenReturn(reader);
        when(jwtService.generateSignatureForId("1")).thenReturn("mock-signature");

        given()
        .when()
                .get("/readers/1")
        .then()
                .status(org.springframework.http.HttpStatus.OK)
                .header("If-Match", "mock-signature")
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
        when(jwtService.verifySignature(eq("1"), anyString())).thenReturn(true);
        when(userUseCase.updateUser(eq("1"), any(Reader.class))).thenReturn(reader);

        given()
                .header("If-Match", "mock-signature")
                .contentType("application/json")
                .body(readerDTO)
        .when()
                .post("/readers/1")
        .then()
                .status(org.springframework.http.HttpStatus.OK)
                .body("login", equalTo("testuser"));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void activateReader_ShouldActivate() {
        when(userUseCase.findUserById("1")).thenReturn(reader);
        when(userUseCase.activateUser("1")).thenReturn(reader);

        given()
        .when()
                .post("/readers/1/activate")
        .then()
                .status(org.springframework.http.HttpStatus.OK)
                .body("login", equalTo("testuser"));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void deactivateReader_ShouldDeactivate() {
        when(userUseCase.findUserById("1")).thenReturn(reader);
        when(userUseCase.deactivateUser("1")).thenReturn(reader);

        given()
        .when()
                .post("/readers/1/deactivate")
        .then()
                .status(org.springframework.http.HttpStatus.OK)
                .body("login", equalTo("testuser"));
    }
}
