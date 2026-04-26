package pl.lodz.p.library.adapters.soap.dto.bookset.responses;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import pl.lodz.p.library.adapters.soap.dto.bookset.BookSetDTO;

@XmlRootElement(name = "GetBookSetsByIdResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetBookSetByIdResponse {
    @XmlElement(required = true)
    private BookSetDTO bookSet;
    public BookSetDTO getBookSetDTO() { return bookSet; }
    public void setBookSetDTO(BookSetDTO bookSetDTO) { this.bookSet = bookSetDTO; }
}
