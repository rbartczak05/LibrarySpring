package pl.lodz.p.library.adapters.rest.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.library.adapters.rest.converters.BookSetConverter;
import pl.lodz.p.library.adapters.rest.dto.BookSetDTO;
import pl.lodz.p.library.domain.model.BookSet;
import pl.lodz.p.library.ports.inbound.BookSetUseCase;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/book_set")
public class BookSetController {
    private final BookSetUseCase bookSetUseCase;

    public BookSetController(BookSetUseCase bookSetUseCase) {
        this.bookSetUseCase = bookSetUseCase;
    }

    @GetMapping
    public List<BookSetDTO> getAllBookSets() {
        return bookSetUseCase.findAllBookSets().stream().map(BookSetConverter::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public BookSetDTO getBookSetByID(@PathVariable String id) {
        return BookSetConverter.toDTO(bookSetUseCase.findBookSetById(id));
    }

    @GetMapping("/title/{titleWithoutSpace}")
    public List<BookSetDTO> getBookSetByTitle(@PathVariable String titleWithoutSpace) {
        String title = titleWithoutSpace.replace("+", " ");
        return bookSetUseCase.findBookSetsByTitle(title).stream().map(BookSetConverter::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/author/{authorWithoutSpace}")
    public List<BookSetDTO> getBookSetByAuthor(@PathVariable String authorWithoutSpace) {
        String author = authorWithoutSpace.replace("+", " ");
        return bookSetUseCase.findBookSetsByAuthor(author).stream().map(BookSetConverter::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/release_year/{releaseYear}")
    public List<BookSetDTO> getBookSetByReleaseYear(@PathVariable int releaseYear) {
        return bookSetUseCase.findBookSetsByReleaseYear(releaseYear).stream().map(BookSetConverter::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/quantity/{quantity}")
    public List<BookSetDTO> getBookSetByQuantity(@PathVariable int quantity) {
        return bookSetUseCase.findAllBookSetsByQuantity(quantity).stream().map(BookSetConverter::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/available/{available}")
    public List<BookSetDTO> getBookSetByAvailable(@PathVariable boolean available) {
        return bookSetUseCase.findBookSetsByAvailable(available).stream().map(BookSetConverter::toDTO).collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookSetDTO addBookSet(@Valid @RequestBody BookSetDTO bookSetDTO) {
        BookSet toSave = BookSetConverter.fromDTO(bookSetDTO);
        return BookSetConverter.toDTO(bookSetUseCase.addBookSet(toSave));
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookSetDTO updateBookSet(@PathVariable String id, @Valid @RequestBody BookSetDTO bookSetDTO) {
        BookSet bookSetUpdates = BookSetConverter.fromDTO(bookSetDTO);
        return BookSetConverter.toDTO(bookSetUseCase.updateBookSet(id, bookSetUpdates));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBookSet(@PathVariable String id) {
        bookSetUseCase.deleteBookSet(id);
    }
}