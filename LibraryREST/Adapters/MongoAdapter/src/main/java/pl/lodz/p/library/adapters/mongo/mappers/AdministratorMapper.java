package pl.lodz.p.library.adapters.mongo.mappers;

import org.springframework.stereotype.Component;
import pl.lodz.p.library.adapters.mongo.documents.AdministratorDoc;
import pl.lodz.p.library.domain.model.Administrator;

@Component
public class AdministratorMapper {
    public Administrator toDomain(AdministratorDoc doc) {
        if (doc == null) return null;
        Administrator administrator = new Administrator(doc.getLogin(), doc.getPassword(), doc.getEmail(), doc.getAge());
        administrator.setId(doc.getId());
        administrator.setActive(doc.isActive());
        return administrator;
    }

    public AdministratorDoc toDocument(Administrator administrator) {
        if (administrator == null) return null;
        AdministratorDoc doc = new AdministratorDoc(administrator.getLogin(), administrator.getPassword(), administrator.getEmail(), administrator.getAge());
        doc.setId(administrator.getId());
        doc.setActive(administrator.isActive());
        return doc;
    }
}
