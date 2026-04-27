package pl.lodz.p.user.adapters.rest.converters;

import pl.lodz.p.user.adapters.rest.dto.AdministratorDTO;
import pl.lodz.p.user.adapters.rest.dto.LibrarianDTO;
import pl.lodz.p.user.adapters.rest.dto.ReaderDTO;
import pl.lodz.p.user.domain.model.Administrator;
import pl.lodz.p.user.domain.model.Librarian;
import pl.lodz.p.user.domain.model.Reader;

public class UserConverter {
    private UserConverter() {
    }

    public static ReaderDTO toReaderDTO(Reader reader) {
        if (reader == null) return null;
        ReaderDTO dto = new ReaderDTO(
                reader.getLogin(),
                reader.getEmail(),
                reader.getFirstName(),
                reader.getLastName(),
                reader.getAge(),
                reader.isActive()
        );
        dto.setId(reader.getId());
        dto.setAccessLevel("reader");
        return dto;
    }

    public static LibrarianDTO toLibrarianDTO(Librarian librarian) {
        if (librarian == null) return null;
        LibrarianDTO dto = new LibrarianDTO(
                librarian.getLogin(),
                librarian.getEmail(),
                librarian.getFirstName(),
                librarian.getLastName(),
                librarian.getAge(),
                librarian.isActive()
        );
        dto.setId(librarian.getId());
        dto.setAccessLevel("librarian");
        return dto;
    }

    public static AdministratorDTO toAdministratorDTO(Administrator administrator) {
        if (administrator == null) return null;
        AdministratorDTO dto = new AdministratorDTO(
                administrator.getLogin(),
                administrator.getEmail(),
                administrator.getFirstName(),
                administrator.getLastName(),
                administrator.getAge(),
                administrator.isActive()
        );
        dto.setId(administrator.getId());
        dto.setAccessLevel("admin");
        return dto;
    }

    public static Reader fromReaderDTO(ReaderDTO dto) {
        if (dto == null) return null;
        Reader r = new Reader(
                dto.getLogin(),
                dto.getEmail(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getAge()
        );
        r.setId(dto.getId());
        r.setActive(dto.isActive());
        return r;
    }

    public static Librarian fromLibrarianDTO(LibrarianDTO dto) {
        if (dto == null) return null;
        Librarian l = new Librarian(
                dto.getLogin(),
                dto.getEmail(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getAge()
        );
        l.setId(dto.getId());
        l.setActive(dto.isActive());
        return l;
    }

    public static Administrator fromAdministratorDTO(AdministratorDTO dto) {
        if (dto == null) return null;
        Administrator a = new Administrator(
                dto.getLogin(),
                dto.getEmail(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getAge()
        );
        a.setId(dto.getId());
        a.setActive(dto.isActive());
        return a;
    }
}