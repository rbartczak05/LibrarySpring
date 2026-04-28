package pl.lodz.p.library.adapters.soap.dto.client.requests;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "GetClientByIdRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetClientByIdRequest {
    @XmlElement(required = true)
    private String id;
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
}
