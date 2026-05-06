package pl.lodz.p.user.adapters.soap.dto.user.requests;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "GetUserByEmailRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetUserByEmailRequest {
    @XmlElement(required = true)
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
