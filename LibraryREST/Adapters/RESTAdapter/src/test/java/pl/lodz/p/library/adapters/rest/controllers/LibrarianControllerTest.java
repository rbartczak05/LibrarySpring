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
import pl.lodz.p.library.adapters.rest.dto.LibrarianDTO;
import pl.lodz.p.library.adapters.rest.security.JwtService;
import pl.lodz.p.library.domain.model.Librarian;
import pl.lodz.p.library.ports.inbound.UserUseCase;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LibrarianController.class)
@AutoConfigureMockMvc(addFilters = false)
public class LibrarianControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserUseCase userUseCase;

    @MockitoBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private Librarian librarian;
    private LibrarianDTO librarianDTO;

    @BeforeEach
    void setUp() {
        librarian = new Librarian("librarian", "password", "lib@example.com", 35);
        librarian.setId("lib1");
        librarian.setActive(true);

        librarianDTO = new LibrarianDTO("lib1", "librarian", "lib@example.com", 35, true, "LIBRARIAN");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllLibrarians_ShouldReturnList() throws Exception {
        when(userUseCase.findAllUsers()).thenReturn(Collections.singletonList(librarian));

        mockMvc.perform(get("/librarians"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].login").value("librarian"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getLibrarianById_ShouldReturnLibrarian() throws Exception {
        when(userUseCase.findUserById("lib1")).thenReturn(librarian);
        when(jwtService.generateSignatureForId("lib1")).thenReturn("mock-signature");

        mockMvc.perform(get("/librarians/lib1"))
                .andExpect(status().isOk())
                .andExpect(header().string("If-Match", "mock-signature"))
                .andExpect(jsonPath("$.login").value("librarian"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addLibrarian_ShouldCreateLibrarian() throws Exception {
        when(userUseCase.addUser(any(Librarian.class))).thenReturn(librarian);

        mockMvc.perform(post("/librarians")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(librarianDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.login").value("librarian"));
    }
}
