package pl.lodz.p.user.adapters.soap.converters;

import pl.lodz.p.user.adapters.soap.dto.user.UserDTO;
import pl.lodz.p.user.domain.model.*;

public class UserSoapConverter {
    private UserSoapConverter() {}

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

    public static User fromDTO(UserDTO dto) {
        if (dto == null) return null;
        User user;
        String level = dto.getAccessLevel();

        if ("ADMINISTRATOR".equalsIgnoreCase(level)) {
            user = new Administrator(dto.getLogin(), dto.getEmail(), dto.getFirstName(), dto.getLastName(), dto.getAge());
        } else if ("LIBRARIAN".equalsIgnoreCase(level)) {
            user = new Librarian(dto.getLogin(), dto.getEmail(), dto.getFirstName(), dto.getLastName(), dto.getAge());
        } else {
            user = new Reader(dto.getLogin(), dto.getEmail(), dto.getFirstName(), dto.getLastName(), dto.getAge());
        }

        user.setId(dto.getId());
        user.setActive(dto.isActive());
        return user;
    }
}