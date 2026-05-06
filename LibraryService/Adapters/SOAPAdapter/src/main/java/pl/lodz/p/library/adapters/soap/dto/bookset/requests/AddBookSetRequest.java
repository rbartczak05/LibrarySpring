package pl.lodz.p.library.adapters.soap.dto.bookset.requests;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import pl.lodz.p.library.adapters.soap.dto.bookset.BookSetDTO;

@XmlRootElement(name = "AddBookSetRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class AddBookSetRequest {
    @XmlElement(required = true)
    private BookSetDTO bookSetDTO;

    public BookSetDTO getBookSetDTO() {
        return bookSetDTO;
    }

    public void setBookSetDTO(BookSetDTO bookSetDTO) {
        this.bookSetDTO = bookSetDTO;
    }
}
