package pl.lodz.p.user.adapters.mongo.mappers;

import org.springframework.stereotype.Component;
import pl.lodz.p.user.adapters.mongo.documents.AdministratorDoc;
import pl.lodz.p.user.domain.model.Administrator;

@Component
public class AdministratorMapper {
    public Administrator toDomain(AdministratorDoc administratorDoc) {
        if (administratorDoc == null) {
            return null;
        }
        Administrator administrator = new Administrator(
                administratorDoc.getLogin(),
                administratorDoc.getPassword(),
                administratorDoc.getEmail(),
                administratorDoc.getFirstName(),
                administratorDoc.getLastName(),
                administratorDoc.getAge()
        );
        administrator.setId(administratorDoc.getId());
        administrator.setActive(administratorDoc.isActive());
        return administrator;
    }

    public AdministratorDoc toDocument(Administrator administrator) {
        if (administrator == null) {
            return null;
        }
        AdministratorDoc administratorDoc = new AdministratorDoc(
                administrator.getLogin(),
                administrator.getPassword(),
                administrator.getEmail(),
                administrator.getFirstName(),
                administrator.getLastName(),
                administrator.getAge(),
                administrator.isActive()
        );
        administratorDoc.setId(administrator.getId());
        return administratorDoc;
    }
}