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
import pl.lodz.p.library.converter.BookSetConverter;
import pl.lodz.p.library.dto.BookSetDTO;
import pl.lodz.p.library.exception.BookSetNotAvailableException;
import pl.lodz.p.library.exception.BookSetNotFoundException;
import pl.lodz.p.library.exception.BookSetTitleException;
import pl.lodz.p.library.model.BookSet;
import pl.lodz.p.library.service.BookSetService;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookSetController.class)
class BookSetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookSetService bookSetService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookSet bookSet;
    private BookSetDTO bookSetDTO;
    private String testId;

    @BeforeEach
    void setUp() {
        bookSet = new BookSet("Diuna", "Frank Herbert", 1965, 5);
        bookSet.setId(UUID.randomUUID().toString());

        testId = bookSet.getId();

        bookSetDTO = BookSetConverter.toDTO(bookSet);
    }

    @Test
    void getAllBookSetsTest() throws Exception {
        when(bookSetService.findAllBookSets()).thenReturn(List.of(bookSet));

        mockMvc.perform(get("/book_set"))
                .andExpect(status().isOk())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Diuna")));
    }

    @Test
    void getBookSetByIDTest() throws Exception {
        when(bookSetService.findBookSetById(testId)).thenReturn(bookSet);

        mockMvc.perform(get("/book_set/{id}", testId))
                .andExpect(status().isOk())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(testId)));
    }

    @Test
    void getBookSetByIDNotFoundTest() throws Exception {
        when(bookSetService.findBookSetById(any(String.class)))
                .thenThrow(new BookSetNotFoundException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/book_set/{id}", testId))
                .andExpect(status().isNotFound())
                .andExpect(status().is(404));
    }

    @Test
    void getBookSetByTitleTest() throws Exception {
        when(bookSetService.findBookSetsByTitle("Diuna 1")).thenReturn(List.of(bookSet));

        mockMvc.perform(get("/book_set/title/{titleWithoutSpace}", "Diuna+1"))
                .andExpect(status().isOk())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getBookSetByAuthorTest() throws Exception {
        when(bookSetService.findBookSetsByAuthor("Frank Herbert")).thenReturn(List.of(bookSet));

        mockMvc.perform(get("/book_set/author/{authorWithoutSpace}", "Frank+Herbert"))
                .andExpect(status().isOk())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getBookSetByReleaseYearTest() throws Exception {
        when(bookSetService.findBookSetsByReleaseYear(1965)).thenReturn(List.of(bookSet));

        mockMvc.perform(get("/book_set/release_year/{releaseYear}", 1965))
                .andExpect(status().isOk())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getBookSetByQuantityTest() throws Exception {
        when(bookSetService.findAllBookSetsByQuantity(5)).thenReturn(List.of(bookSet));

        mockMvc.perform(get("/book_set/quantity/{quantity}", 5))
                .andExpect(status().isOk())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getBookSetByAvailableTest() throws Exception {
        when(bookSetService.findBookSetsByAvailable(true)).thenReturn(List.of(bookSet));

        mockMvc.perform(get("/book_set/available/{available}", true))
                .andExpect(status().isOk())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void addBookSetTest() throws Exception {
        when(bookSetService.addBookSet(any(BookSet.class))).thenReturn(bookSet);

        mockMvc.perform(post("/book_set")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookSetDTO)))
                .andExpect(status().isCreated())
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.title", is("Diuna")));
    }

    @Test
    void addBookSetBadRequestTest() throws Exception {
        when(bookSetService.addBookSet(any(BookSet.class)))
                .thenThrow(new BookSetTitleException(HttpStatus.BAD_REQUEST));

        mockMvc.perform(post("/book_set")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookSetDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(status().is(400));
    }

    @Test
    void updateBookSetTest() throws Exception {
        BookSet updatedBook = new BookSet("Diuna", "Frank Herbert", 1965, 10);
        when(bookSetService.updateBookSet(any(String.class), any(BookSet.class))).thenReturn(updatedBook);

        mockMvc.perform(post("/book_set/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookSetDTO)))
                .andExpect(status().isOk())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.quantity", is(10)));
    }

    @Test
    void deleteBookSetTest() throws Exception {
        mockMvc.perform(delete("/book_set/{id}", testId))
                .andExpect(status().isNoContent())
                .andExpect(status().is(204));

        verify(bookSetService, times(1)).deleteBookSet(testId);
    }

    @Test
    void deleteBookSetFailsTest() throws Exception {
        doThrow(new BookSetNotAvailableException(HttpStatus.BAD_REQUEST))
                .when(bookSetService).deleteBookSet(testId);

        mockMvc.perform(delete("/book_set/{id}", testId))
                .andExpect(status().isBadRequest())
                .andExpect(status().is(400));
    }
}