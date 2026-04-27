package pl.lodz.p.user.adapters.soap.dto.user.responses;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import pl.lodz.p.user.adapters.soap.dto.user.UserDTO;
import java.util.List;

@XmlRootElement(name = "GetAllUsersResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetAllUsersResponse {
    @XmlElement(name = "user", required = true)
    private List<UserDTO> users;
    public List<UserDTO> getUsers() { return users; }
    public void setUsers(List<UserDTO> users) { this.users = users; }
}