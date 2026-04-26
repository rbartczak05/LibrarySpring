package pl.lodz.p.library.adapters.soap.dto.loan.responses;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import pl.lodz.p.library.adapters.soap.dto.loan.LoanDTO;

import java.util.List;

@XmlRootElement(name = "GetLoansByReaderResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetLoansByReaderResponse {
    @XmlElement(name = "loan", required = true)
    private List<LoanDTO> loans;
    public List<LoanDTO> getLoanDTO() { return loans; }
    public void setLoanDTO(List<LoanDTO> loanDTO) { this.loans = loanDTO; }
}
