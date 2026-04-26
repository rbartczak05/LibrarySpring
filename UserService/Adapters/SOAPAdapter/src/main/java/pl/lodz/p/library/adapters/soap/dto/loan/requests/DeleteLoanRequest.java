package pl.lodz.p.library.adapters.soap.dto.loan.requests;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DeleteLoanRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeleteLoanRequest {
    @XmlElement(required = true)
    private String id;
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
}
