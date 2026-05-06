package pl.lodz.p.library.adapters.soap.dto.client.responses;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import pl.lodz.p.library.adapters.soap.dto.client.ClientDTO;

@XmlRootElement(name = "GetCLientByIdResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetClientByIdResponse {
    @XmlElement(required = true)
    private ClientDTO clientDTO;

    public ClientDTO getClientDTO() {
        return clientDTO;
    }

    public void setClient(ClientDTO userDTO) {
        this.clientDTO = userDTO;
    }
}
