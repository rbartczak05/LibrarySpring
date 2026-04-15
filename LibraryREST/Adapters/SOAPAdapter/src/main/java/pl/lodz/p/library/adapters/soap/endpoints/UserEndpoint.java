package pl.lodz.p.library.adapters.soap.endpoints;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.lodz.p.library.adapters.soap.converters.UserSoapConverter;
import pl.lodz.p.library.adapters.soap.dto.user.requests.*;
import pl.lodz.p.library.adapters.soap.dto.user.responses.*;
import pl.lodz.p.library.domain.model.User;
import pl.lodz.p.library.ports.inbound.UserUseCase;

import java.util.List;
import java.util.stream.Collectors;

@Endpoint
public class UserEndpoint {

    private static final String namespace = "http://pl.lodz.p.library.adapters.soap.dto.user/";

    private final UserUseCase userUseCase;

    public UserEndpoint(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @PayloadRoot(namespace = namespace, localPart = "GetAllUsersRequest")
    @ResponsePayload
    public GetAllUsersResponse getAllUsers(@RequestPayload GetAllUsersRequest request) {
        List<User> users = userUseCase.findAllUsers();
        GetAllUsersResponse response = new GetAllUsersResponse();
        response.setUsers(users.stream().map(UserSoapConverter::toDTO).collect(Collectors.toList()));
        return response;
    }

    @PayloadRoot(namespace = namespace, localPart = "GetUserByIdRequest")
    @ResponsePayload
    public GetUserByIdResponse getUserById(@RequestPayload GetUserByIdRequest request) {
        GetUserByIdResponse response = new GetUserByIdResponse();
        response.setUserDTO(UserSoapConverter.toDTO(userUseCase.findUserById(request.getId())));
        return response;
    }

    @PayloadRoot(namespace = namespace, localPart = "GetUserByLoginRequest")
    @ResponsePayload
    public GetUserByLoginResponse getUserByLogin(@RequestPayload GetUserByLoginRequest request) {
        GetUserByLoginResponse response = new GetUserByLoginResponse();
        response.setUserDTO(UserSoapConverter.toDTO(userUseCase.findUserByLogin(request.getLogin())));
        return response;
    }

    @PayloadRoot(namespace = namespace, localPart = "GetUserByEmailRequest")
    @ResponsePayload
    public GetUserByEmailResponse getUserByEmail(@RequestPayload GetUserByEmailRequest request) {
        GetUserByEmailResponse response = new GetUserByEmailResponse();
        response.setUserDTO(UserSoapConverter.toDTO(userUseCase.findUserByEmail(request.getEmail())));
        return response;
    }

    @PayloadRoot(namespace = namespace, localPart = "AddUserRequest")
    @ResponsePayload
    public AddUserResponse addUser(@RequestPayload AddUserRequest request) {
        User toSave = UserSoapConverter.fromDTO(request.getUserDTO());
        toSave.setPassword(request.getPassword());
        AddUserResponse response = new AddUserResponse();
        response.setUser(UserSoapConverter.toDTO(userUseCase.addUser(toSave)));
        return response;
    }

    @PayloadRoot(namespace = namespace, localPart = "ActivateUserRequest")
    @ResponsePayload
    public ActivateUserResponse activateUser(@RequestPayload ActivateUserRequest request) {
        ActivateUserResponse response = new ActivateUserResponse();
        response.setUser(UserSoapConverter.toDTO(userUseCase.activateUser(request.getId())));
        return response;
    }

    @PayloadRoot(namespace = namespace, localPart = "DeactivateUserRequest")
    @ResponsePayload
    public DeactivateUserResponse deactivateUser(@RequestPayload DeactivateUserRequest request) {
        DeactivateUserResponse response = new DeactivateUserResponse();
        response.setUser(UserSoapConverter.toDTO(userUseCase.deactivateUser(request.getId())));
        return response;
    }

    @PayloadRoot(namespace = namespace, localPart = "DeleteUserRequest")
    @ResponsePayload
    public DeleteUserResponse deleteUser(@RequestPayload DeleteUserRequest request) {
        userUseCase.deleteUser(request.getId());
        DeleteUserResponse response = new DeleteUserResponse();
        response.setDeleted(true);
        return response;
    }
}
