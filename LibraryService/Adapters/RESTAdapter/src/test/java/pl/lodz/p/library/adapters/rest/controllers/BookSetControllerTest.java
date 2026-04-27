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
import pl.lodz.p.library.adapters.rest.dto.BookSetDTO;
import pl.lodz.p.library.adapters.rest.security.JwtService;
import pl.lodz.p.library.domain.model.BookSet;
import pl.lodz.p.library.ports.inbound.BookSetUseCase;

import java.util.Collections;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(BookSetController.class)
@AutoConfigureMockMvc(addFilters = false)
public class BookSetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookSetUseCase bookSetUseCase;

    @MockitoBean
    private JwtService jwtService;

    private BookSet bookSet;
    private BookSetDTO bookSetDTO;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);

        bookSet = new BookSet("The Witcher", "Andrzej Sapkowski", 1990, 5);
        bookSet.setId("book1");

        bookSetDTO = new BookSetDTO();
        bookSetDTO.setId("book1");
        bookSetDTO.setTitle("The Witcher");
        bookSetDTO.setAuthor("Andrzej Sapkowski");
        bookSetDTO.setReleaseYear(1990);
        bookSetDTO.setQuantity(5);
    }

    @Test
    @WithMockUser
    void getAllBookSets_ShouldReturnList() {
        when(bookSetUseCase.findAllBookSets()).thenReturn(Collections.singletonList(bookSet));

        given()
                .when()
                .get("/book_set")
                .then()
                .status(org.springframework.http.HttpStatus.OK)
                .body("_embedded.booksets[0].title", equalTo("The Witcher"));
    }

    @Test
    @WithMockUser
    void getBookSetById_ShouldReturnBookSet() {
        when(bookSetUseCase.findBookSetById("book1")).thenReturn(bookSet);

        given()
                .when()
                .get("/book_set/book1")
                .then()
                .status(org.springframework.http.HttpStatus.OK)
                .body("title", equalTo("The Witcher"));
    }

    @Test
    @WithMockUser
    void addBookSet_ShouldCreateBookSet() {
        when(bookSetUseCase.addBookSet(any(BookSet.class))).thenReturn(bookSet);

        given()
                .contentType("application/json")
                .body(bookSetDTO)
                .when()
                .post("/book_set")
                .then()
                .status(org.springframework.http.HttpStatus.CREATED)
                .body("title", equalTo("The Witcher"));
    }
}