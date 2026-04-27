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
import pl.lodz.p.user.adapters.rest.dto.AdministratorDTO;
import pl.lodz.p.user.adapters.rest.security.JwtService;
import pl.lodz.p.user.domain.model.Administrator;
import pl.lodz.p.user.ports.inbound.UserUseCase;

import java.util.Collections;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(AdministratorController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AdministratorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserUseCase userUseCase;

    @MockitoBean
    private JwtService jwtService;

    private Administrator admin;
    private AdministratorDTO adminDTO;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);

        admin = new Administrator("admin", "pass", "admin@example.com", "Adam", "Kowalski", 30);
        admin.setId("admin1");
        admin.setActive(true);

        adminDTO = new AdministratorDTO("admin", "admin@example.com", "Adam", "Kowalski", 30, true);
        adminDTO.setId("admin1");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllAdmins_ShouldReturnList() {
        when(userUseCase.findAllUsers()).thenReturn(Collections.singletonList(admin));

        given()
                .when()
                .get("/admins")
                .then()
                .status(org.springframework.http.HttpStatus.OK)
                .body("$", hasSize(1))
                .body("[0].login", equalTo("admin"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAdminById_ShouldReturnAdmin() {
        when(userUseCase.findUserById("admin1")).thenReturn(admin);
        when(jwtService.generateSignatureForId("admin1")).thenReturn("mock-signature");

        given()
                .when()
                .get("/admins/admin1")
                .then()
                .status(org.springframework.http.HttpStatus.OK)
                .header("If-Match", "mock-signature")
                .body("login", equalTo("admin"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addAdmin_ShouldCreateAdmin() {
        when(userUseCase.addUser(any(Administrator.class))).thenReturn(admin);

        given()
                .contentType("application/json")
                .body(adminDTO)
                .when()
                .post("/admins")
                .then()
                .status(org.springframework.http.HttpStatus.CREATED)
                .body("login", equalTo("admin"));
    }
}