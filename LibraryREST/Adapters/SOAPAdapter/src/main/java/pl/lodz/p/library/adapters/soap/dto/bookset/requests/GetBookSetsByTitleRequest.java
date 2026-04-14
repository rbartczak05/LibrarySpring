package pl.lodz.p.library.adapters.soap.dto.bookset.requests;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "GetBookSetsByTitleRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetBookSetsByTitleRequest {
    @XmlElement(required = true)
    private String title;
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}
