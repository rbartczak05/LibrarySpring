package pl.lodz.p.library.adapters.soap.dto.loan;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.*;

import javax.xml.datatype.XMLGregorianCalendar;

@XmlType(name = "loanDTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoanDTO {
    @XmlElement(name = "id", required = true)
    private String id;

    @NotNull(message = "Stan wypożyczenia nie może być pusty.")
    @XmlElement(name = "active", required = true)
    private boolean active;

    @NotNull(message = "Data rozpoczęcia wypożyczenia nie może być pusta.")
    @XmlSchemaType(name = "dateTime")
    @XmlElement(name = "startTime", required = true)
    private XMLGregorianCalendar startTime;

    @XmlSchemaType(name = "dateTime")
    @XmlElement(name = "returnTime", required = false)
    private XMLGregorianCalendar returnTime;

    @NotNull(message = "Data planowanego zakończenia nie może być pusta.")
    @Future(message = "Planowana data zakończenia musi być w przyszłości.")
    @XmlSchemaType(name = "dateTime")
    @XmlElement(name = "endTime", required = true)
    private XMLGregorianCalendar endTime;

    @NotBlank(message = "ID klienta jest wymagane do utworzenia wypożyczenia.")
    @XmlElement(name = "clientId", required = true)
    private String clientId;

    @NotBlank(message = "ID książki jest wymagane do utworzenia wypożyczenia.")
    @XmlElement(name = "bookSetId", required = true)
    private String bookSetId;

    public LoanDTO() {
    }

    public LoanDTO(String id, boolean active, XMLGregorianCalendar startTime, XMLGregorianCalendar endTime, XMLGregorianCalendar returnTime, String clientId, String bookSetId) {
        this.id = id;
        this.active = active;
        this.startTime = startTime;
        this.returnTime = returnTime;
        this.endTime = endTime;
        this.clientId = clientId;
        this.bookSetId = bookSetId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public XMLGregorianCalendar getStartTime() {
        return startTime;
    }

    public void setStartTime(XMLGregorianCalendar startTime) {
        this.startTime = startTime;
    }

    public XMLGregorianCalendar getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(XMLGregorianCalendar returnTime) {
        this.returnTime = returnTime;
    }

    public XMLGregorianCalendar getEndTime() {
        return endTime;
    }

    public void setEndTime(XMLGregorianCalendar endTime) {
        this.endTime = endTime;
    }

    public String getBookSetId() {
        return bookSetId;
    }

    public void setBookSetId(String bookSetId) {
        this.bookSetId = bookSetId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}