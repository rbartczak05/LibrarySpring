package pl.lodz.p.library.adapters.rest.dto;

public class AdministratorDTO extends UserDTO {

    public AdministratorDTO() {
    }

    public AdministratorDTO(String id, String login, String email, int age, boolean active, String type) {
        super(id, login, email, age, active, type);
    }
}
