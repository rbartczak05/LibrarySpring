package pl.lodz.p.library.adapters.mongo.mappers;

import pl.lodz.p.library.adapters.mongo.documents.UserDoc;
import pl.lodz.p.library.domain.model.User;

public interface UserMapper {
    public User toDomain(UserDoc doc);

    public UserDoc toDocument(User user);
}
