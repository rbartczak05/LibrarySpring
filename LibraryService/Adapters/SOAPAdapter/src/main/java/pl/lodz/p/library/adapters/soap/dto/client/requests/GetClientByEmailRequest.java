package pl.lodz.p.library.adapters.soap.dto.client.requests;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "GetClientByEmailRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetClientByEmailRequest {
    @XmlElement(required = true)
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
