package pl.lodz.p.library.adapters.soap.dto.client.responses;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import pl.lodz.p.library.adapters.soap.dto.client.ClientDTO;

@XmlRootElement(name = "ActivateClientResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class ActivateClientResponse {
    @XmlElement(required = true)
    private ClientDTO clientDTO;
    public ClientDTO getUser() { return clientDTO; }
    public void setUser(ClientDTO clientDTO) { this.clientDTO = clientDTO; }
}
