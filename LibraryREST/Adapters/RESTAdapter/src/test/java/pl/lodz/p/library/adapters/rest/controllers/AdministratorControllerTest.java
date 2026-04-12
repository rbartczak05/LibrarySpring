package pl.lodz.p.library.adapters.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.lodz.p.library.adapters.rest.dto.AdministratorDTO;
import pl.lodz.p.library.adapters.rest.security.JwtService;
import pl.lodz.p.library.domain.model.Administrator;
import pl.lodz.p.library.ports.inbound.UserUseCase;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdministratorController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AdministratorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserUseCase userUseCase;

    @MockitoBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private Administrator admin;
    private AdministratorDTO adminDTO;

    @BeforeEach
    void setUp() {
        admin = new Administrator("admin", "password", "admin@example.com", 30);
        admin.setId("admin1");
        admin.setActive(true);

        adminDTO = new AdministratorDTO("admin1", "admin", "admin@example.com", 30, true, "ADMIN");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllAdmins_ShouldReturnList() throws Exception {
        when(userUseCase.findAllUsers()).thenReturn(Collections.singletonList(admin));

        mockMvc.perform(get("/admins"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].login").value("admin"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAdminById_ShouldReturnAdmin() throws Exception {
        when(userUseCase.findUserById("admin1")).thenReturn(admin);
        when(jwtService.generateSignatureForId("admin1")).thenReturn("mock-signature");

        mockMvc.perform(get("/admins/admin1"))
                .andExpect(status().isOk())
                .andExpect(header().string("If-Match", "mock-signature"))
                .andExpect(jsonPath("$.login").value("admin"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addAdmin_ShouldCreateAdmin() throws Exception {
        when(userUseCase.addUser(any(Administrator.class))).thenReturn(admin);

        mockMvc.perform(post("/admins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.login").value("admin"));
    }
}
