package pl.lodz.p.library.adapters.soap.dto.client.responses;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import pl.lodz.p.library.adapters.soap.dto.client.ClientDTO;

import java.util.List;

@XmlRootElement(name = "GetAllClientsResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetAllClientsResponse {
    @XmlElement(name = "client", required = true)
    private List<ClientDTO> clients;

    public List<ClientDTO> getClients() {
        return clients;
    }

    public void setClients(List<ClientDTO> clients) {
        this.clients = clients;
    }
}
