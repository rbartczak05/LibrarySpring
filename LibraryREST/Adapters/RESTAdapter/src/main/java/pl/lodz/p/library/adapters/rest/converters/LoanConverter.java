package pl.lodz.p.library.adapters.rest.converters;

import pl.lodz.p.library.dto.LoanDTO;
import pl.lodz.p.library.model.Loan;

public class LoanConverter {
    private LoanConverter() {
    }

    public static LoanDTO toDTO(Loan loan) {
        if (loan == null) {
            return null;
        }
        LoanDTO dto = new LoanDTO();
        dto.setId(loan.getId());
        dto.setActive(loan.isActive());
        dto.setStartTime(loan.getStartTime());
        dto.setEndTime(loan.getEndTime());
        dto.setReturnTime(loan.getReturnTime());
        dto.setBookSetId(loan.getBookSetId());
        dto.setReaderId(loan.getReaderId());
        return dto;
    }

    public static Loan fromDTO(LoanDTO dto) {
        if (dto == null) {
            return null;
        }
        Loan loan = new Loan(dto.getReaderId(), dto.getBookSetId(), dto.getStartTime());
        loan.setActive(dto.isActive());
        loan.setReturnTime(dto.getReturnTime());
        return loan;
    }
}
