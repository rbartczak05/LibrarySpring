package pl.lodz.p.user.adapters.soap.dto.user.responses;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DeleteUserResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeleteUserResponse {
    @XmlElement(required = true)
    private boolean isDeleted;

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
