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
import pl.lodz.p.library.adapters.rest.dto.LibrarianDTO;
import pl.lodz.p.library.adapters.rest.security.JwtService;
import pl.lodz.p.library.domain.model.Librarian;
import pl.lodz.p.library.ports.inbound.UserUseCase;

import java.util.Collections;

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

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        librarian = new Librarian("librarian", "password", "lib@example.com", 35);
        librarian.setId("lib1");
        librarian.setActive(true);

        librarianDTO = new LibrarianDTO("lib1", "librarian", "lib@example.com", 35, true, "LIBRARIAN");
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
        when(userUseCase.findUserById("lib1")).thenReturn(librarian);
        when(jwtService.generateSignatureForId("lib1")).thenReturn("mock-signature");

        given()
        .when()
                .get("/librarians/lib1")
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
