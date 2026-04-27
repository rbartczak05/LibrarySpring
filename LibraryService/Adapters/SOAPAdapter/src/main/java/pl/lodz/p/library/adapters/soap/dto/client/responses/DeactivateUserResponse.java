package pl.lodz.p.library.adapters.soap.dto.client.responses;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import pl.lodz.p.library.adapters.soap.dto.client.UserDTO;

@XmlRootElement(name = "DeactivateUserResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeactivateUserResponse {
    @XmlElement(required = true)
    private UserDTO user;
    public UserDTO getUser() { return user; }
    public void setUser(UserDTO user) { this.user = user; }
}
