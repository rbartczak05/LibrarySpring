package pl.lodz.p.library.adapters.soap.dto.client.responses;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import pl.lodz.p.library.adapters.soap.dto.client.ClientDTO;

@XmlRootElement(name = "AddClientResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class AddClientResponse {
    @XmlElement(required = true)
    private ClientDTO clientDTO;

    public ClientDTO getClientDTO() {
        return clientDTO;
    }

    public void setClientDTO(ClientDTO clientDTO) {
        this.clientDTO = clientDTO;
    }
}
