package pl.lodz.p.library.adapters.soap.dto.bookset.requests;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.UUID;

@XmlRootElement(name = "GetBookSetByIdRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetBookSetByIdRequest {
    @XmlElement(required = true)
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
