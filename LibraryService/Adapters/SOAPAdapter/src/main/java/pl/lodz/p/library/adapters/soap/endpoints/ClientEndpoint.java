package pl.lodz.p.library.adapters.soap.endpoints;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.lodz.p.library.adapters.soap.converters.ClientSoapConverter;
import pl.lodz.p.library.adapters.soap.dto.client.requests.GetAllClientsRequest;
import pl.lodz.p.library.adapters.soap.dto.client.requests.GetClientByIdRequest;
import pl.lodz.p.library.adapters.soap.dto.client.responses.GetAllClientsResponse;
import pl.lodz.p.library.adapters.soap.dto.client.responses.GetClientByIdResponse;
import pl.lodz.p.library.ports.inbound.ClientUseCase;

import java.util.stream.Collectors;

@Endpoint
public class ClientEndpoint {

    private static final String namespace = "http://pl.lodz.p.library.adapters.soap.dto.client/";

    private final ClientUseCase clientUseCase;

    public ClientEndpoint(ClientUseCase clientUseCase) {
        this.clientUseCase = clientUseCase;
    }

    @PayloadRoot(namespace = namespace, localPart = "GetAllClientsRequest")
    @ResponsePayload
    public GetAllClientsResponse getAllClients(@RequestPayload GetAllClientsRequest request) {
        GetAllClientsResponse response = new GetAllClientsResponse();
        response.setClients(clientUseCase.findAllClients().stream()
                .map(ClientSoapConverter::toDTO)
                .collect(Collectors.toList()));
        return response;
    }

    @PayloadRoot(namespace = namespace, localPart = "GetClientByIdRequest")
    @ResponsePayload
    public GetClientByIdResponse getClientById(@RequestPayload GetClientByIdRequest request) {
        GetClientByIdResponse response = new GetClientByIdResponse();
        response.setClient(ClientSoapConverter.toDTO(clientUseCase.findClientById(request.getId())));
        return response;
    }
}