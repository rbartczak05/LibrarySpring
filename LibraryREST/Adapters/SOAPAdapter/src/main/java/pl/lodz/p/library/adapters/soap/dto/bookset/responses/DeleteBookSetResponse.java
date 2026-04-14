package pl.lodz.p.library.adapters.soap.dto.bookset.responses;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DeleteBookSetResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeleteBookSetResponse {
    @XmlElement(required = true)
    private boolean isDeleted;
    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean isDeleted) { this.isDeleted = isDeleted; }
}
