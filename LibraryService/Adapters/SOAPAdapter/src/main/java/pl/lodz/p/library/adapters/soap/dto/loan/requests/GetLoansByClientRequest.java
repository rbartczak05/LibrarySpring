package pl.lodz.p.library.adapters.soap.dto.loan.requests;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.UUID;

@XmlRootElement(name = "GetLoansByClientRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetLoansByClientRequest {
    @XmlElement(name = "clientId", required = true)
    private UUID clientId;

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }
}