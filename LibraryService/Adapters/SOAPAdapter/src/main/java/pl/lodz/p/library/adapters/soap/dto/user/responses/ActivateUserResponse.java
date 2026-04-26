package pl.lodz.p.library.adapters.soap.dto.user.responses;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import pl.lodz.p.library.adapters.soap.dto.user.UserDTO;

@XmlRootElement(name = "ActivateUserResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class ActivateUserResponse {
    @XmlElement(required = true)
    private UserDTO user;
    public UserDTO getUser() { return user; }
    public void setUser(UserDTO user) { this.user = user; }
}
