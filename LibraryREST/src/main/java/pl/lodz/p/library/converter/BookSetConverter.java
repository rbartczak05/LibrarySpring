package pl.lodz.p.library.converter;

import pl.lodz.p.library.dto.BookSetDTO;
import pl.lodz.p.library.model.BookSet;

public class BookSetConverter {
    private BookSetConverter() {
    }

    public static BookSetDTO toDTO(BookSet bookSet) {
        if (bookSet == null) {
            return null;
        }
        BookSetDTO dto = new BookSetDTO();
        dto.setId(bookSet.getId());
        dto.setTitle(bookSet.getTitle());
        dto.setAuthor(bookSet.getAuthor());
        dto.setReleaseYear(bookSet.getReleaseYear());
        dto.setQuantity(bookSet.getQuantity());

        return dto;
    }

    public static BookSet fromDTO(BookSetDTO dto) {
        if (dto == null) {
            return null;
        }
        return new BookSet(dto.getTitle(), dto.getAuthor(), dto.getReleaseYear(), dto.getQuantity());
    }
}