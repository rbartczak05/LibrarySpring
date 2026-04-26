package pl.lodz.p.library.adapters.rest.converters;

import pl.lodz.p.library.adapters.rest.dto.AdministratorDTO;
import pl.lodz.p.library.adapters.rest.dto.LibrarianDTO;
import pl.lodz.p.library.adapters.rest.dto.ReaderDTO;
import pl.lodz.p.library.domain.model.Reader;

public class UserConverter {
    private UserConverter() {
    }

    public static ReaderDTO toReaderDTO(Reader reader) {
        if (reader == null) return null;
        return new ReaderDTO(reader.getId(), reader.getLogin(), reader.getEmail(), reader.getAge(), reader.isActive(), "reader", reader.getCurrentLoansCount());
    }

    public static LibrarianDTO toLibrarianDTO(Librarian librarian) {
        if (librarian == null) return null;
        return new LibrarianDTO(librarian.getId(), librarian.getLogin(), librarian.getEmail(), librarian.getAge(), librarian.isActive(), "librarian");
    }

    public static AdministratorDTO toAdministratorDTO(Administrator administrator) {
        if (administrator == null) return null;
        return new AdministratorDTO(administrator.getId(), administrator.getLogin(), administrator.getEmail(), administrator.getAge(), administrator.isActive(), "admin");
    }

    public static Reader fromReaderDTO(ReaderDTO dto) {
        if (dto == null) return null;
        Reader r = new Reader(dto.getLogin(), dto.getEmail(), dto.getAge());
        r.setId(dto.getId());
        r.setActive(dto.isActive());
        r.setCurrentLoansCount(dto.getCurrentLoansCount());
        return r;
    }

    public static Librarian fromLibrarianDTO(LibrarianDTO dto) {
        if (dto == null) return null;
        Librarian l = new Librarian(dto.getLogin(), dto.getEmail(), dto.getAge());
        l.setId(dto.getId());
        l.setActive(dto.isActive());
        return l;
    }

    public static Administrator fromAdministratorDTO(AdministratorDTO dto) {
        if (dto == null) return null;
        Administrator a = new Administrator(dto.getLogin(), dto.getEmail(), dto.getAge());
        a.setId(dto.getId());
        a.setActive(dto.isActive());
        return a;
    }
}