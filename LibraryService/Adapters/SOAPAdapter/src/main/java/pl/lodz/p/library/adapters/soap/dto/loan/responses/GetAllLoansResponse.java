package pl.lodz.p.library.adapters.soap.dto.loan.responses;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import pl.lodz.p.library.adapters.soap.dto.loan.LoanDTO;

import java.util.List;

@XmlRootElement(name = "GetAllLoansResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetAllLoansResponse {
    @XmlElement(name = "loan", required = true)
    private List<LoanDTO> loans;

    public List<LoanDTO> getLoans() {
        return loans;
    }

    public void setLoans(List<LoanDTO> loans) {
        this.loans = loans;
    }
}
