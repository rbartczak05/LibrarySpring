package pl.lodz.p.library.adapters.soap.converters;

import pl.lodz.p.library.adapters.soap.dto.user.UserDTO;
import pl.lodz.p.library.domain.model.Administrator;
import pl.lodz.p.library.domain.model.Librarian;
import pl.lodz.p.library.domain.model.Reader;
import pl.lodz.p.library.domain.model.User;

public class UserSoapConverter {
    private UserSoapConverter() {}

    public static UserDTO toDTO(User user) {
        if (user == null) return null;

        String type;
        int currentLoansCount = 0;

        switch (user) {
            case Reader reader -> {
                type = "reader";
                currentLoansCount = reader.getCurrentLoansCount();
            }
            case Librarian librarian -> type = "librarian";
            case Administrator administrator -> type = "admin";
            default -> type = "unknown";
        }

        return new UserDTO(user.getId(), user.getLogin(), user.getEmail(), user.getAge(), user.isActive(),
                type, currentLoansCount
        );
    }

    public static User fromDTO(UserDTO dto) {
        if (dto == null) return null;

        String type = dto.getType();

        switch (type) {
            case "reader" -> {
                Reader r = new Reader(dto.getLogin(), dto.getEmail(), dto.getAge());
                r.setId(dto.getId());
                r.setActive(dto.isActive());
                r.setCurrentLoansCount(dto.getCurrentLoansCount());
                return r;
            }
            case "librarian" -> {
                Librarian l = new Librarian(dto.getLogin(), dto.getEmail(), dto.getAge());
                l.setId(dto.getId());
                l.setActive(dto.isActive());
                return l;
            }
            case "administator" -> {
                Administrator a = new Administrator(dto.getLogin(), dto.getEmail(), dto.getAge());
                a.setId(dto.getId());
                a.setActive(dto.isActive());
                return a;
            }
            default -> {
                return null;
            }
        }
    }
}
