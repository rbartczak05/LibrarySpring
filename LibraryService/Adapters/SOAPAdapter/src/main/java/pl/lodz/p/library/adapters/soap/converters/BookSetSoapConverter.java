package pl.lodz.p.library.adapters.soap.converters;


import pl.lodz.p.library.adapters.soap.dto.bookset.BookSetDTO;
import pl.lodz.p.library.domain.model.BookSet;

public class BookSetSoapConverter {
    private BookSetSoapConverter() {
    }

    public static BookSet toDomain(BookSetDTO dto) {
        if (dto == null) return null;

        BookSet dom = new BookSet(dto.getTitle(), dto.getAuthor(), dto.getReleaseYear(), dto.getQuantity());
        dom.setId(dto.getId());

        return dom;
    }

    public static BookSetDTO toDTO(BookSet dom) {
        if (dom == null) return null;

        return new BookSetDTO(dom.getId(), dom.getTitle(), dom.getAuthor(), dom.getReleaseYear(),
                dom.getQuantity()
        );
    }
}
