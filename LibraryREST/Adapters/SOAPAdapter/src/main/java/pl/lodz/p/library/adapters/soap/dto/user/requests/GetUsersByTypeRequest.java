package pl.lodz.p.library.adapters.soap.dto.user.requests;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "GetUsersByTypeRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetUsersByTypeRequest {
    @XmlElement(required = true)
    private String type;
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
