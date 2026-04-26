package pl.lodz.p.library.adapters.rest.dto;

public class AdministratorDTO extends UserDTO {
    public AdministratorDTO(String login, String email, String firstName, String lastName, int age, boolean active) {
        super(login, email, firstName, lastName, age, active, "ADMINISTRATOR");
    }

    public AdministratorDTO() {
        super();
        setAccessLevel("ADMINISTRATOR");
    }
}