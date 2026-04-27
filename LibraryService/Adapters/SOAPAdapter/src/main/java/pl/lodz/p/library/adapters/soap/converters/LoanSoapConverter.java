package pl.lodz.p.library.adapters.soap.converters;

import pl.lodz.p.library.adapters.soap.dto.loan.LoanDTO;
import pl.lodz.p.library.domain.model.Loan;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;

public class LoanSoapConverter {
    private LoanSoapConverter() {}

    public static LoanDTO toDTO(Loan loan) {
        if (loan == null) return null;
        return new LoanDTO(loan.getId(), loan.isActive(), toXml(loan.getStartTime()), toXml(loan.getEndTime()),
                toXml(loan.getReturnTime()), loan.getClientId(), loan.getBookSetId()
        );
    }

    public static Loan fromDTO(LoanDTO dto) {
        if (dto == null) return null;
        Loan loan = new Loan(dto.getClientId(), dto.getBookSetId(), fromXml(dto.getStartTime()));
        loan.setId(dto.getId());
        loan.setActive(dto.isActive());
        loan.setEndTime(fromXml(dto.getEndTime()));
        loan.setReturnTime(fromXml(dto.getReturnTime()));
        return loan;
    }

    private static XMLGregorianCalendar toXml(LocalDateTime ldt) {
        if (ldt == null) return null;
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(ldt.toString());
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private static LocalDateTime fromXml(XMLGregorianCalendar xgc) {
        if (xgc == null) return null;
        return xgc.toGregorianCalendar().toZonedDateTime().toLocalDateTime();
    }
}