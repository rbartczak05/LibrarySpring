package pl.lodz.p.library.adapters.mongo.mappers;

import org.springframework.stereotype.Component;
import pl.lodz.p.library.adapters.mongo.documents.LoanDoc;
import pl.lodz.p.library.domain.model.Loan;

@Component
public class LoanMapper {

    public Loan toDomain(LoanDoc doc) {
        if (doc == null) return null;

        Loan loan = new Loan(doc.getClientId(), doc.getBookSetId(), doc.getStartTime());
        loan.setId(doc.getId());
        loan.setEndTime(doc.getEndTime());
        loan.setActive(doc.isActive());
        loan.setReturnTime(doc.getReturnTime());

        return loan;
    }

    public LoanDoc toDocument(Loan loan) {
        if (loan == null) return null;

        LoanDoc doc = new LoanDoc(loan.getClientId(), loan.getBookSetId(), loan.getStartTime());
        doc.setId(loan.getId());
        doc.setEndTime(loan.getEndTime());
        doc.setActive(loan.isActive());
        doc.setReturnTime(loan.getReturnTime());

        return doc;
    }
}