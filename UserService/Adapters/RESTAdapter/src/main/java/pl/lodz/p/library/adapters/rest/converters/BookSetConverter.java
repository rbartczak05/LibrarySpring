package pl.lodz.p.library.adapters.rest.converters;

import pl.lodz.p.library.adapters.rest.dto.BookSetDTO;
import pl.lodz.p.library.domain.model.BookSet;

public class BookSetConverter {
    private BookSetConverter() {
    }

    public static BookSetDTO toDTO(BookSet bookSet) {
        if (bookSet == null) return null;
        BookSetDTO dto = new BookSetDTO();
        dto.setId(bookSet.getId());
        dto.setTitle(bookSet.getTitle());
        dto.setAuthor(bookSet.getAuthor());
        dto.setReleaseYear(bookSet.getReleaseYear());
        dto.setQuantity(bookSet.getQuantity());
        return dto;
    }

    public static BookSet fromDTO(BookSetDTO dto) {
        if (dto == null) return null;
        BookSet bookSet = new BookSet(dto.getTitle(), dto.getAuthor(), dto.getReleaseYear(), dto.getQuantity());
        bookSet.setId(dto.getId());
        return bookSet;
    }
}