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
import pl.lodz.p.user.adapters.rest.dto.LibrarianDTO;
import pl.lodz.p.user.adapters.rest.security.JwtService;
import pl.lodz.p.user.domain.model.Librarian;
import pl.lodz.p.user.ports.inbound.UserUseCase;

import java.util.Collections;
import java.util.UUID;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(LibrarianController.class)
@AutoConfigureMockMvc(addFilters = false)
public class LibrarianControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserUseCase userUseCase;

    @MockitoBean
    private JwtService jwtService;

    private Librarian librarian;
    private LibrarianDTO librarianDTO;
    private UUID librarianId;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);

        librarianId = UUID.randomUUID();
        librarian = new Librarian("librarian", "pass", "lib@example.com", "Anna", "Nowak", 30);
        librarian.setId(librarianId);
        librarian.setActive(true);

        librarianDTO = new LibrarianDTO("librarian", "lib@example.com", "Anna", "Nowak", 30, true);
        librarianDTO.setId(librarianId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllLibrarians_ShouldReturnList() {
        when(userUseCase.findAllUsers()).thenReturn(Collections.singletonList(librarian));

        given()
                .when()
                .get("/librarians")
                .then()
                .status(org.springframework.http.HttpStatus.OK)
                .body("$", hasSize(1))
                .body("[0].login", equalTo("librarian"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getLibrarianById_ShouldReturnLibrarian() {
        when(userUseCase.findUserById(librarianId)).thenReturn(librarian);
        when(jwtService.generateSignatureForId(librarianId)).thenReturn("mock-signature");

        given()
                .when()
                .get("/librarians/" + librarianId)
                .then()
                .status(org.springframework.http.HttpStatus.OK)
                .header("If-Match", "mock-signature")
                .body("login", equalTo("librarian"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addLibrarian_ShouldCreateLibrarian() {
        when(userUseCase.addUser(any(Librarian.class))).thenReturn(librarian);

        given()
                .contentType("application/json")
                .body(librarianDTO)
                .when()
                .post("/librarians")
                .then()
                .status(org.springframework.http.HttpStatus.CREATED)
                .body("login", equalTo("librarian"));
    }
}