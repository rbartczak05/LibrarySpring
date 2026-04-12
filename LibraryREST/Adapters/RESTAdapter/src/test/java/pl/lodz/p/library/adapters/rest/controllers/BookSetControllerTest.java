package pl.lodz.p.library.adapters.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.lodz.p.library.adapters.rest.dto.BookSetDTO;
import pl.lodz.p.library.adapters.rest.security.JwtService;
import pl.lodz.p.library.domain.model.BookSet;
import pl.lodz.p.library.ports.inbound.BookSetUseCase;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookSetController.class)
@AutoConfigureMockMvc(addFilters = false)
public class BookSetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookSetUseCase bookSetUseCase;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookSet bookSet;
    private BookSetDTO bookSetDTO;

    @BeforeEach
    void setUp() {
        bookSet = new BookSet("The Witcher", "Andrzej Sapkowski", 1993, 10);
        bookSet.setId("book1");

        bookSetDTO = new BookSetDTO("book1", "The Witcher", "Andrzej Sapkowski", 1993, 10);
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void getAllBookSets_ShouldReturnList() throws Exception {
        when(bookSetUseCase.findAllBookSets()).thenReturn(Collections.singletonList(bookSet));

        mockMvc.perform(get("/book_set"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("The Witcher"));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void getBookSetById_ShouldReturnBookSet() throws Exception {
        when(bookSetUseCase.findBookSetById("book1")).thenReturn(bookSet);

        mockMvc.perform(get("/book_set/book1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Witcher"));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void addBookSet_ShouldCreateBookSet() throws Exception {
        when(bookSetUseCase.addBookSet(any(BookSet.class))).thenReturn(bookSet);

        mockMvc.perform(post("/book_set")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookSetDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("The Witcher"));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void deleteBookSet_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/book_set/book1"))
                .andExpect(status().isNoContent());
    }
}
