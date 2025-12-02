package pl.lodz.p.library.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.library.converter.BookSetConverter;
import pl.lodz.p.library.dto.BookSetDTO;
import pl.lodz.p.library.model.BookSet;
import pl.lodz.p.library.service.BookSetService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/book_set")
public class BookSetController {

    private final BookSetService bookSetService;

    @Autowired
    public BookSetController(BookSetService bookSetService) {
        this.bookSetService = bookSetService;
    }

    @GetMapping
    public List<BookSetDTO> getAllBookSets() {
        return bookSetService.findAllBookSets().stream().map(BookSetConverter::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public BookSetDTO getBookSetByID(@PathVariable String id) {
        return BookSetConverter.toDTO(bookSetService.findBookSetById(id));
    }

    @GetMapping("/title/{titleWithoutSpace}")
    public List<BookSetDTO> getBookSetByTitle(@PathVariable String titleWithoutSpace) {
        String title = titleWithoutSpace.replace("+", " ");
        return bookSetService.findBookSetsByTitle(title).stream().map(BookSetConverter::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/author/{authorWithoutSpace}")
    public List<BookSetDTO> getBookSetByAuthor(@PathVariable String authorWithoutSpace) {
        String author = authorWithoutSpace.replace("+", " ");
        return bookSetService.findBookSetsByAuthor(author).stream().map(BookSetConverter::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/release_year/{releaseYear}")
    public List<BookSetDTO> getBookSetByReleaseYear(@PathVariable int releaseYear) {
        return bookSetService.findBookSetsByReleaseYear(releaseYear).stream().map(BookSetConverter::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/quantity/{quantity}")
    public List<BookSetDTO> getBookSetByQuantity(@PathVariable int quantity) {
        return bookSetService.findAllBookSetsByQuantity(quantity).stream().map(BookSetConverter::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/available/{available}")
    public List<BookSetDTO> getBookSetByAvailable(@PathVariable boolean available) {
        return bookSetService.findBookSetsByAvailable(available).stream().map(BookSetConverter::toDTO).collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookSetDTO addBookSet(@Valid @RequestBody BookSetDTO bookSetDTO) {
        BookSet toSave = BookSetConverter.fromDTO(bookSetDTO);
        return BookSetConverter.toDTO(bookSetService.addBookSet(toSave));
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookSetDTO updateBookSet(@PathVariable String id, @Valid @RequestBody BookSetDTO bookSetDTO) {
        BookSet bookSetUpdates = BookSetConverter.fromDTO(bookSetDTO);
        return BookSetConverter.toDTO(bookSetService.updateBookSet(id, bookSetUpdates));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBookSet(@PathVariable String id) {
        bookSetService.deleteBookSet(id);
    }
}
