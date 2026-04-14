package pl.lodz.p.library.adapters.soap.dto.user.responses;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import pl.lodz.p.library.adapters.soap.dto.user.UserDTO;

import java.util.List;

@XmlRootElement(name = "GetUsersByTypeResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetUsersByTypeResponse {
    @XmlElement(name = "user", required = true)
    private List<UserDTO> user;
    public List<UserDTO> getUser() { return user; }
    public void setUser(List<UserDTO> user) { this.user = user; }
}
