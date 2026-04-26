package pl.lodz.p.library.adapters.soap.converters;

import pl.lodz.p.library.adapters.soap.dto.user.UserDTO;
import pl.lodz.p.library.domain.model.Administrator;
import pl.lodz.p.library.domain.model.Librarian;
import pl.lodz.p.library.domain.model.Reader;
import pl.lodz.p.library.domain.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserSoapConverter {
    private UserSoapConverter() {
    }

    public static UserDTO toDTO(User user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setLogin(user.getLogin());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setAge(user.getAge());
        dto.setActive(user.isActive());

        if (user instanceof Administrator) dto.setAccessLevel("ADMINISTRATOR");
        else if (user instanceof Librarian) dto.setAccessLevel("LIBRARIAN");
        else dto.setAccessLevel("READER");

        return dto;
    }

    public static List<UserDTO> toUserDTOList(List<User> users) {
        return users.stream().map(UserSoapConverter::toDTO).collect(Collectors.toList());
    }

    public static User fromDTO(UserDTO dto) {
        if (dto == null) return null;
        User user;
        if ("ADMINISTRATOR".equalsIgnoreCase(dto.getAccessLevel())) {
            user = new Administrator(dto.getLogin(), dto.getEmail(), dto.getFirstName(), dto.getLastName(), dto.getAge());
        } else if ("LIBRARIAN".equalsIgnoreCase(dto.getAccessLevel())) {
            user = new Librarian(dto.getLogin(), dto.getEmail(), dto.getFirstName(), dto.getLastName(), dto.getAge());
        } else {
            user = new Reader(dto.getLogin(), dto.getEmail(), dto.getFirstName(), dto.getLastName(), dto.getAge());
        }
        user.setId(dto.getId());
        user.setActive(dto.isActive());
        return user;
    }
}
