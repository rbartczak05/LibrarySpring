package pl.lodz.p.library.adapters.soap.dto.loan.requests;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CreateLoanRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateLoanRequest {
    @XmlElement(required = true)
    private String bookSetId;
    @XmlElement(required = true)
    private String readerId;
    public String getBookSetId() { return bookSetId; }
    public void setBookSetId(String bookSetId) { this.bookSetId = bookSetId; }
    public String getReaderId() { return readerId; }
    public void setReaderId(String readerId) { this.readerId = readerId; }
}
