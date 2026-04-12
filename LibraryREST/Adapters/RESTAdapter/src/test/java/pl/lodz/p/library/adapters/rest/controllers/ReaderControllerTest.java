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
import pl.lodz.p.library.adapters.rest.dto.ReaderDTO;
import pl.lodz.p.library.adapters.rest.security.JwtService;
import pl.lodz.p.library.domain.model.Reader;
import pl.lodz.p.library.ports.inbound.UserUseCase;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReaderController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ReaderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserUseCase userUseCase;

    @MockitoBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private Reader reader;
    private ReaderDTO readerDTO;

    @BeforeEach
    void setUp() {
        reader = new Reader("testuser", "password", "test@example.com", 25);
        reader.setId("1");
        reader.setActive(true);

        readerDTO = new ReaderDTO("1", "testuser", "test@example.com", 25, true, "READER", 0);
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void getAllReaders_ShouldReturnList() throws Exception {
        when(userUseCase.findAllUsers()).thenReturn(Collections.singletonList(reader));

        mockMvc.perform(get("/readers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].login").value("testuser"));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void getReaderById_ShouldReturnReader() throws Exception {
        when(userUseCase.findUserById("1")).thenReturn(reader);
        when(jwtService.generateSignatureForId("1")).thenReturn("mock-signature");

        mockMvc.perform(get("/readers/1"))
                .andExpect(status().isOk())
                .andExpect(header().string("If-Match", "mock-signature"))
                .andExpect(jsonPath("$.login").value("testuser"));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void addReader_ShouldCreateReader() throws Exception {
        when(userUseCase.addUser(any(Reader.class))).thenReturn(reader);

        mockMvc.perform(post("/readers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(readerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.login").value("testuser"));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void updateReader_ShouldUpdateReader() throws Exception {
        when(jwtService.verifySignature(eq("1"), anyString())).thenReturn(true);
        when(userUseCase.updateUser(eq("1"), any(Reader.class))).thenReturn(reader);

        mockMvc.perform(post("/readers/1")
                        .header("If-Match", "mock-signature")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(readerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("testuser"));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void activateReader_ShouldActivate() throws Exception {
        when(userUseCase.findUserById("1")).thenReturn(reader);
        when(userUseCase.activateUser("1")).thenReturn(reader);

        mockMvc.perform(post("/readers/1/activate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("testuser"));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void deactivateReader_ShouldDeactivate() throws Exception {
        when(userUseCase.findUserById("1")).thenReturn(reader);
        when(userUseCase.deactivateUser("1")).thenReturn(reader);

        mockMvc.perform(post("/readers/1/deactivate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("testuser"));
    }
}
