package pl.lodz.p.library.ports.library.web.dto;

public class LibrarianDTO extends UserDTO {

    public LibrarianDTO() {
    }

    public LibrarianDTO(String id, String login, String email, int age, boolean active, String type) {
        super(id, login, email, age, active, type);
    }
}
