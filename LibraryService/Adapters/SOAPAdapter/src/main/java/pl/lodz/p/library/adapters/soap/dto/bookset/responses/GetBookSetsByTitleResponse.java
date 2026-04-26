package pl.lodz.p.library.adapters.soap.dto.bookset.responses;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import pl.lodz.p.library.adapters.soap.dto.bookset.BookSetDTO;

import java.util.List;

@XmlRootElement(name = "GetBookSetsByTitleResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetBookSetsByTitleResponse {
    @XmlElement(name = "bookSet", required = true)
    private List<BookSetDTO> bookSets;
    public List<BookSetDTO> getBookSets() { return bookSets; }
    public void setBookSets(List<BookSetDTO> bookSets) { this.bookSets = bookSets; }
}
