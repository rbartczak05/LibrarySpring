package pl.lodz.p.library.adapters.mongo.aggregates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.library.adapters.mongo.documents.BookSetDoc;
import pl.lodz.p.library.adapters.mongo.mappers.BookSetMapper;
import pl.lodz.p.library.adapters.mongo.repositories.BookSetRepository;
import pl.lodz.p.library.domain.model.BookSet;
import pl.lodz.p.library.ports.outbound.DeleteBookSetPort;
import pl.lodz.p.library.ports.outbound.GetBookSetPort;
import pl.lodz.p.library.ports.outbound.SaveBookSetPort;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BookSetRepositoryAdapter implements GetBookSetPort, SaveBookSetPort, DeleteBookSetPort {

    private final BookSetRepository repository;
    private final BookSetMapper mapper;

    @Autowired
    public BookSetRepositoryAdapter(BookSetRepository repository, BookSetMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<BookSet> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<BookSet> findById(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<BookSet> findBookSetsByTitle(String title) {
        return repository.findBookByTitle(title).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<BookSet> findBookSetsByAuthor(String author) {
        return repository.findBooksByAuthor(author).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<BookSet> findBookSetsByReleaseYear(int releaseYear) {
        return repository.findBooksByReleaseYear(releaseYear).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<BookSet> findBookSetsByQuantity(int quantity) {
        return repository.findBooksByQuantity(quantity).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<BookSet> findAllBookSetsByQuantity(int quantity) {
        return repository.findBooksByQuantity(quantity).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<BookSet> findByQuantityGreaterThan(int quantity) {
        return repository.findByQuantityGreaterThan(quantity).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<BookSet> findByQuantityLessThan(int quantity) {
        return repository.findAll().stream()
                .filter(doc -> doc.getQuantity() < quantity)
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookSet> findBookSetsByAvailable(boolean available) {
        if (available) {
            return findByQuantityGreaterThan(0);
        } else {
            return findBookSetsByQuantity(0);
        }
    }

    @Override
    public List<BookSet> findAllBookSets() {
        return findAll();
    }

    @Override
    public Optional<BookSet> addBookSet(BookSet bookSet) {
        BookSetDoc saved = repository.save(mapper.toDocument(bookSet));
        return Optional.ofNullable(mapper.toDomain(saved));
    }

    @Override
    public Optional<BookSet> updateBookSet(String id, BookSet bookSetUpdates) {
        return repository.findById(id).map(existing -> {
            existing.setQuantity(bookSetUpdates.getQuantity());
            return mapper.toDomain(repository.save(existing));
        });
    }

    @Override
    public BookSet save(BookSet bookSet) {
        return mapper.toDomain(repository.save(mapper.toDocument(bookSet)));
    }

    @Override
    public void deleteBookSet(String id) {
        repository.deleteById(id);
    }
}