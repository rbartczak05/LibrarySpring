package pl.lodz.p.library.adapters.soap.dto.loan.requests;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.UUID;

@XmlRootElement(name = "CreateLoanRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateLoanRequest {
    @XmlElement(name = "clientId", required = true)
    private UUID clientId;

    @XmlElement(name = "bookSetId", required = true)
    private UUID bookSetId;

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public UUID getBookSetId() {
        return bookSetId;
    }

    public void setBookSetId(UUID bookSetId) {
        this.bookSetId = bookSetId;
    }
}