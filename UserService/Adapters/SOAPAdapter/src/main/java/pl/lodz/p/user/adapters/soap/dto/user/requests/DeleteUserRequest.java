package pl.lodz.p.user.adapters.soap.dto.user.requests;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DeleteUserRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeleteUserRequest {
    @XmlElement(required = true)
    private String id;
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
}
