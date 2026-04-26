package pl.lodz.p.library.adapters.rest.controllers;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.HttpStatus;
import pl.lodz.p.library.adapters.rest.dto.BookSetDTO;
import pl.lodz.p.library.adapters.rest.security.JwtAuthenticationFilter;
import pl.lodz.p.library.adapters.rest.security.JwtService;
import pl.lodz.p.library.adapters.rest.security.SecurityConfig;
import pl.lodz.p.library.domain.model.BookSet;
import pl.lodz.p.library.ports.inbound.BookSetUseCase;

import java.util.Collections;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(BookSetController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
@AutoConfigureMockMvc(addFilters = true)
public class BookSetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookSetUseCase bookSetUseCase;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private AuthenticationProvider authenticationProvider;

    private BookSet bookSet;
    private BookSetDTO bookSetDTO;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        bookSet = new BookSet("The Witcher", "Andrzej Sapkowski", 1993, 10);
        bookSet.setId("book1");

        bookSetDTO = new BookSetDTO("book1", "The Witcher", "Andrzej Sapkowski", 1993, 10);
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void getAllBookSets_ShouldReturnList() {
        when(bookSetUseCase.findAllBookSets()).thenReturn(Collections.singletonList(bookSet));

        given()
        .when()
                .get("/book_set")
        .then()
                .status(HttpStatus.OK)
                .body("$", hasSize(1))
                .body("[0].title", equalTo("The Witcher"));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void getBookSetById_ShouldReturnBookSet() {
        when(bookSetUseCase.findBookSetById("book1")).thenReturn(bookSet);

        given()
        .when()
                .get("/book_set/book1")
        .then()
                .status(HttpStatus.OK)
                .body("title", equalTo("The Witcher"));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void addBookSet_ShouldCreateBookSet() {
        when(bookSetUseCase.addBookSet(any(BookSet.class))).thenReturn(bookSet);

        given()
                .contentType("application/json")
                .body(bookSetDTO)
        .when()
                .post("/book_set")
        .then()
                .status(HttpStatus.CREATED)
                .body("title", equalTo("The Witcher"));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void deleteBookSet_ShouldReturnNoContent() {
        given()
        .when()
                .delete("/book_set/book1")
        .then()
                .status(HttpStatus.NO_CONTENT);
    }
}
