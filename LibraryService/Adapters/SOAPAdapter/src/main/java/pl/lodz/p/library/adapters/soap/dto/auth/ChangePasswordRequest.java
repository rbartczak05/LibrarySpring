package pl.lodz.p.library.adapters.soap.dto.auth;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ChangePasswordRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChangePasswordRequest {
    @XmlElement(required = true)
    private String login;
    @XmlElement(required = true)
    private String oldPassword;
    @XmlElement(required = true)
    private String newPassword;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}