package pl.lodz.p.user.adapters.rest.dto;

public class LibrarianDTO extends UserDTO {
    public LibrarianDTO(String login, String email, String firstName, String lastName, int age, boolean active) {
        super(login, email, firstName, lastName, age, active, "LIBRARIAN");
    }

    public LibrarianDTO() {
        super();
        setAccessLevel("LIBRARIAN");
    }
}