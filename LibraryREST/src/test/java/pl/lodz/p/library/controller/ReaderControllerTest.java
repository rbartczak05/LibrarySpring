package pl.lodz.p.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.lodz.p.library.converter.UserConverter;
import pl.lodz.p.library.dto.ReaderDTO;
import pl.lodz.p.library.exception.UserLoginAlreadyExistException;
import pl.lodz.p.library.exception.UserNotFoundException;
import pl.lodz.p.library.model.Reader;
import pl.lodz.p.library.service.UserService;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReaderController.class)
class ReaderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private Reader reader;
    private ReaderDTO readerDTO;
    private String testId;

    @BeforeEach
    void setUp() {
        reader = new Reader("testLogin", "test@email.com", 30);
        reader.setId(UUID.randomUUID().toString());
        testId = reader.getId();

        readerDTO = UserConverter.toReaderDTO(reader);
    }

    @Test
    void getAllReadersTest() throws Exception {
        when(userService.findAllUsers()).thenReturn(List.of(reader));

        mockMvc.perform(get("/readers"))
                .andExpect(status().isOk())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].login", is("testLogin")));
    }

    @Test
    void getReaderByIdTest() throws Exception {
        when(userService.findUserById(testId)).thenReturn(reader);

        mockMvc.perform(get("/readers/{id}", testId))
                .andExpect(status().isOk())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(testId)))
                .andExpect(jsonPath("$.login", is("testLogin")));
    }

    @Test
    void getReaderByIdNotFoundTest() throws Exception {
        when(userService.findUserById(any(String.class)))
                .thenThrow(new UserNotFoundException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/readers/{id}", UUID.randomUUID().toString()))
                .andExpect(status().isNotFound())
                .andExpect(status().is(404));
    }

    @Test
    void addReaderTest() throws Exception {
        when(userService.addUser(any(Reader.class))).thenReturn(reader);

        mockMvc.perform(post("/readers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(readerDTO)))
                .andExpect(status().isCreated())
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.login", is("testLogin")));
    }

    @Test
    void addReaderLoginConflictTest() throws Exception {
        when(userService.addUser(any(Reader.class)))
                .thenThrow(new UserLoginAlreadyExistException(HttpStatus.CONFLICT));

        mockMvc.perform(post("/readers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(readerDTO)))
                .andExpect(status().isConflict())
                .andExpect(status().is(409));
    }

    @Test
    void updateReaderTest() throws Exception {
        Reader updatedReader = new Reader("nowyLogin", "nowy@email.com", 31);
        when(userService.updateUser(any(String.class), any(Reader.class))).thenReturn(updatedReader);

        mockMvc.perform(post("/readers/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(readerDTO)))
                .andExpect(status().isOk())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.login", is("nowyLogin")));
    }

    @Test
    void activateReaderTest() throws Exception {
        reader.setActive(true);
        when(userService.findUserById(testId)).thenReturn(reader);
        when(userService.activateUser(testId)).thenReturn(reader);

        mockMvc.perform(post("/readers/{id}/activate", testId))
                .andExpect(status().isOk())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.active", is(true)));
    }
}