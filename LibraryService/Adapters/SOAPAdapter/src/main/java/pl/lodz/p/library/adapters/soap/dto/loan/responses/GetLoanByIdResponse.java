package pl.lodz.p.library.adapters.soap.dto.loan.responses;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import pl.lodz.p.library.adapters.soap.dto.loan.LoanDTO;

@XmlRootElement(name = "GetLoanByIdResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetLoanByIdResponse {
    @XmlElement(required = true)
    private LoanDTO loan;
    public LoanDTO getLoanDTO() { return loan; }
    public void setLoanDTO(LoanDTO loanDTO) { this.loan = loanDTO; }
}
