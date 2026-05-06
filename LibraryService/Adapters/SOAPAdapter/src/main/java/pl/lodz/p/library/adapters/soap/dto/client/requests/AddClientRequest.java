package pl.lodz.p.library.adapters.soap.dto.client.requests;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import pl.lodz.p.library.adapters.soap.dto.client.ClientDTO;

@XmlRootElement(name = "AddClientRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class AddClientRequest {
    @XmlElement(required = true)
    private ClientDTO clientDTO;
    @XmlElement(required = true)
    private String password;

    public ClientDTO getClientDTO() {
        return clientDTO;
    }

    public void setClientDTO(ClientDTO clientDTO) {
        this.clientDTO = clientDTO;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
