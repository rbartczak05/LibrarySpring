package pl.lodz.p.library.adapters.mongo.mappers;

import org.springframework.stereotype.Component;
import pl.lodz.p.library.adapters.mongo.documents.AdministratorDoc;
import pl.lodz.p.library.domain.model.Administrator;

@Component
public class AdministratorMapper {

    public Administrator toDomain(AdministratorDoc doc) {
        if (doc == null) return null;
        Administrator admin = new Administrator(doc.getLogin(), doc.getPassword(), doc.getEmail(), doc.getAge());
        admin.setId(doc.getId());
        admin.setActive(doc.isActive());
        return admin;
    }

    public AdministratorDoc toDocument(Administrator admin) {
        if (admin == null) return null;
        AdministratorDoc doc = new AdministratorDoc(admin.getLogin(), admin.getPassword(), admin.getEmail(), admin.getAge());
        doc.setId(admin.getId());
        doc.setActive(admin.isActive());
        return doc;
    }
}