package pl.lodz.p.library.adapters.soap.dto.bookset.requests;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import pl.lodz.p.library.adapters.soap.dto.bookset.BookSetDTO;

@XmlRootElement(name = "UpdateBookSetRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdateBookSetRequest {
    @XmlElement(required = true)
    private String id;
    @XmlElement(required = true)
    private BookSetDTO bookSetDTO;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public BookSetDTO getBookSetDTO() {
        return bookSetDTO;
    }
    public void setBookSetDTO(BookSetDTO bookSetDTO) {
        this.bookSetDTO = bookSetDTO;
    }
}
