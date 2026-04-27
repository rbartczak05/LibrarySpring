package pl.lodz.p.library.adapters.soap.endpoints;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.lodz.p.library.adapters.soap.converters.LoanSoapConverter;
import pl.lodz.p.library.adapters.soap.dto.loan.requests.*;
import pl.lodz.p.library.adapters.soap.dto.loan.responses.*;
import pl.lodz.p.library.domain.model.Loan;
import pl.lodz.p.library.ports.inbound.LoanUseCase;

import java.util.List;
import java.util.stream.Collectors;

@Endpoint
public class LoanEndpoint {

    private static final String namespace = "http://pl.lodz.p.library.adapters.soap.dto.loan/";

    private final LoanUseCase loanUseCase;

    public LoanEndpoint(LoanUseCase loanUseCase) {
        this.loanUseCase = loanUseCase;
    }

    @PayloadRoot(namespace = namespace, localPart = "GetAllLoansRequest")
    @ResponsePayload
    public GetAllLoansResponse getAllLoans(@RequestPayload GetAllLoansRequest request) {
        GetAllLoansResponse response = new GetAllLoansResponse();
        response.setLoans(loanUseCase.findAllLoans().stream()
                .map(LoanSoapConverter::toDTO).collect(Collectors.toList()));
        return response;
    }

    @PayloadRoot(namespace = namespace, localPart = "GetLoanByIdRequest")
    @ResponsePayload
    public GetLoanByIdResponse getLoanById(@RequestPayload GetLoanByIdRequest request) {
        GetLoanByIdResponse response = new GetLoanByIdResponse();
        response.setLoanDTO(LoanSoapConverter.toDTO(loanUseCase.findLoanById(request.getId())));
        return response;
    }

    @PayloadRoot(namespace = namespace, localPart = "GetLoansByClientRequest")
    @ResponsePayload
    public GetLoansByClientResponse getLoansByClient(@RequestPayload GetLoansByClientRequest request) {
        List<Loan> loans = loanUseCase.findByClientId(request.getClientId());
        GetLoansByClientResponse response = new GetLoansByClientResponse();
        response.setLoanDTO(loans.stream().map(LoanSoapConverter::toDTO).collect(Collectors.toList()));
        return response;
    }

    @PayloadRoot(namespace = namespace, localPart = "CreateLoanRequest")
    @ResponsePayload
    public CreateLoanResponse createLoan(@RequestPayload CreateLoanRequest request) {
        Loan loan = loanUseCase.createLoan(request.getClientId(), request.getBookSetId());
        CreateLoanResponse response = new CreateLoanResponse();
        response.setLoanDTO(LoanSoapConverter.toDTO(loan));
        return response;
    }

    @PayloadRoot(namespace = namespace, localPart = "EndLoanRequest")
    @ResponsePayload
    public EndLoanResponse endLoan(@RequestPayload EndLoanRequest request) {
        Loan loan = loanUseCase.endLoan(request.getId());
        EndLoanResponse response = new EndLoanResponse();
        response.setLoanDTO(LoanSoapConverter.toDTO(loan));
        return response;
    }

    @PayloadRoot(namespace = namespace, localPart = "DeleteLoanRequest")
    @ResponsePayload
    public DeleteLoanResponse deleteLoan(@RequestPayload DeleteLoanRequest request) {
        loanUseCase.deleteLoan(request.getId());
        DeleteLoanResponse response = new DeleteLoanResponse();
        response.setDeleted(true);
        return response;
    }
}