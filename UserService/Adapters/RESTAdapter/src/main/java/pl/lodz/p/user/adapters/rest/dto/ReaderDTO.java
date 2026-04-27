package pl.lodz.p.user.adapters.rest.dto;

public class ReaderDTO extends UserDTO {
    public ReaderDTO(String login, String email, String firstName, String lastName, int age, boolean active) {
        super(login, email, firstName, lastName, age, active, "READER");
    }

    public ReaderDTO() {
        super();
        setAccessLevel("READER");
    }
}