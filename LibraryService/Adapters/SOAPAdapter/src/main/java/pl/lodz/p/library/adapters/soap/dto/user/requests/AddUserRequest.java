package pl.lodz.p.library.adapters.soap.dto.user.requests;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import pl.lodz.p.library.adapters.soap.dto.user.UserDTO;

@XmlRootElement(name = "AddUserRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class AddUserRequest {
    @XmlElement(required = true)
    private UserDTO userDTO;
    @XmlElement(required = true)
    private String password;
    public UserDTO getUserDTO() { return userDTO; }
    public void setUserDTO(UserDTO userDTO) { this.userDTO = userDTO; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
