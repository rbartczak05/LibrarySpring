package pl.lodz.p.library.adapters.soap.dto.client.requests;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "GetUserByLoginRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetUserByLoginRequest {
    @XmlElement(required = true)
    private String login;
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
}
