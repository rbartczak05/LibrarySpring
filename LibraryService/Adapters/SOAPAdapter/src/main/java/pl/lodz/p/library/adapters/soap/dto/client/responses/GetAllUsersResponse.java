package pl.lodz.p.library.adapters.soap.dto.client.responses;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import pl.lodz.p.library.adapters.soap.dto.client.UserDTO;

import java.util.List;

@XmlRootElement(name = "GetAllUsersRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetAllUsersResponse {
    @XmlElement(name = "user", required = true)
    private List<UserDTO> users;
    public List<UserDTO> getUsers() { return users; }
    public void setUsers(List<UserDTO> users) { this.users = users; }
}
