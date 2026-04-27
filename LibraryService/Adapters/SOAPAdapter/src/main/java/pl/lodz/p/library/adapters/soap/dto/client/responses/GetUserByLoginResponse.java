package pl.lodz.p.library.adapters.soap.dto.client.responses;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import pl.lodz.p.library.adapters.soap.dto.client.UserDTO;

@XmlRootElement(name = "GetUserByLoginResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetUserByLoginResponse {
    @XmlElement(required = true)
    private UserDTO user;
    public UserDTO getUserDTO() { return user; }
    public void setUserDTO(UserDTO userDTO) { this.user = userDTO; }
}
