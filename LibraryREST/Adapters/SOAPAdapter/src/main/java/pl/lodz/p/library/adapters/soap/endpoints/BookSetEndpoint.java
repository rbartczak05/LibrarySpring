package pl.lodz.p.library.adapters.soap.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.lodz.p.library.adapters.soap.converters.BookSetSoapConverter;
import pl.lodz.p.library.adapters.soap.dto.bookset.requests.*;
import pl.lodz.p.library.adapters.soap.dto.bookset.responses.*;
import pl.lodz.p.library.domain.model.BookSet;
import pl.lodz.p.library.ports.inbound.BookSetUseCase;

import java.util.List;
import java.util.stream.Collectors;

@Endpoint
public class BookSetEndpoint {
    private static final String namespace = "http://pl.lodz.p.library.adapters.soap.dto.bookset/";

    private final BookSetUseCase bookSetUseCase;

    @Autowired
    public BookSetEndpoint(BookSetUseCase bookSetUseCase) {
        this.bookSetUseCase = bookSetUseCase;
    }

    @PayloadRoot(namespace = namespace, localPart = "GetAllBookSetsRequest")
    @ResponsePayload
    public GetAllBookSetsResponse getAllBookSetsResponse(@RequestPayload GetAllBookSetsRequest request) {
        List<BookSet> bookSets = bookSetUseCase.findAllBookSets();
        GetAllBookSetsResponse response = new GetAllBookSetsResponse();
        response.setBookSets(bookSets.stream().map(BookSetSoapConverter::toDTO).collect(Collectors.toList()));
        return response;
    }

    @PayloadRoot(namespace = namespace, localPart = "GetBookSetByIdRequest")
    @ResponsePayload
    public GetBookSetByIdResponse getBookSetByIdResponse(@RequestPayload GetBookSetByIdRequest request) {
        BookSet bookSet = bookSetUseCase.findBookSetById(request.getId());
        GetBookSetByIdResponse response = new GetBookSetByIdResponse();
        response.setBookSetDTO(BookSetSoapConverter.toDTO(bookSet));
        return response;
    }

    @PayloadRoot(namespace = namespace, localPart = "GetBookSetsByTitleRequest")
    @ResponsePayload
    public GetBookSetsByTitleResponse getBookSetsByTitle(@RequestPayload GetBookSetsByTitleRequest request) {
        GetBookSetsByTitleResponse response = new GetBookSetsByTitleResponse();
        response.setBookSets(bookSetUseCase.findBookSetsByTitle(request.getTitle()).stream()
                .map(BookSetSoapConverter::toDTO).collect(Collectors.toList()));
        return response;
    }

    @PayloadRoot(namespace = namespace, localPart = "AddBookSetRequest")
    @ResponsePayload
    public AddBookSetResponse addBookSet(@RequestPayload AddBookSetRequest request) {
        BookSet saved = bookSetUseCase.addBookSet(BookSetSoapConverter.toDomain(request.getBookSetDTO()));
        AddBookSetResponse response = new AddBookSetResponse();
        response.setBookSetDTO(BookSetSoapConverter.toDTO(saved));
        return response;
    }

    @PayloadRoot(namespace = namespace, localPart = "UpdateBookSetRequest")
    @ResponsePayload
    public UpdateBookSetResponse updateBookSet(@RequestPayload UpdateBookSetRequest request) {
        BookSet updated = bookSetUseCase.updateBookSet(
                request.getId(), BookSetSoapConverter.toDomain(request.getBookSetDTO()));
        UpdateBookSetResponse response = new UpdateBookSetResponse();
        response.setBookSetDTO(BookSetSoapConverter.toDTO(updated));
        return response;
    }

    @PayloadRoot(namespace = namespace, localPart = "DeleteBookSetRequest")
    @ResponsePayload
    public DeleteBookSetResponse deleteBookSet(@RequestPayload DeleteBookSetRequest request) {
        bookSetUseCase.deleteBookSet(request.getId());
        DeleteBookSetResponse response = new DeleteBookSetResponse();
        response.setDeleted(true);
        return response;
    }
}
