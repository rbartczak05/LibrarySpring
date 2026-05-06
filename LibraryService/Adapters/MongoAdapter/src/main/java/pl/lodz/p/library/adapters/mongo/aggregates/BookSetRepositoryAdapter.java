package pl.lodz.p.library.adapters.mongo.aggregates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.library.adapters.mongo.documents.BookSetDoc;
import pl.lodz.p.library.adapters.mongo.mappers.BookSetMapper;
import pl.lodz.p.library.adapters.mongo.repositories.BookSetRepository;
import pl.lodz.p.library.domain.model.BookSet;
import pl.lodz.p.library.ports.outbound.BookSetPort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class BookSetRepositoryAdapter implements BookSetPort {

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
    public Optional<BookSet> findById(UUID id) {
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
        return repository.findByQuantityLessThan(quantity).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<BookSet> findBookSetsByAvailable(boolean available) {
        if (available) {
            return repository.findByQuantityGreaterThan(0).stream().map(mapper::toDomain).collect(Collectors.toList());
        } else {
            return repository.findBooksByQuantity(0).stream().map(mapper::toDomain).collect(Collectors.toList());
        }
    }

    @Override
    public List<BookSet> findAllBookSets() {
        return repository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<BookSet> addBookSet(BookSet bookSet) {
        if (bookSet.getId() == null) {
            bookSet.setId(UUID.randomUUID());
        }
        BookSetDoc doc = mapper.toDocument(bookSet);
        return Optional.ofNullable(mapper.toDomain(repository.save(doc)));
    }

    @Override
    public BookSet save(BookSet bookSet) {
        if (bookSet.getId() == null) {
            bookSet.setId(UUID.randomUUID());
        }
        BookSetDoc doc = mapper.toDocument(bookSet);
        return mapper.toDomain(repository.save(doc));
    }

    @Override
    public Optional<BookSet> updateBookSet(UUID id, BookSet bookSetUpdates) {
        return repository.findById(id).map(existing -> {
            existing.setTitle(bookSetUpdates.getTitle());
            existing.setAuthor(bookSetUpdates.getAuthor());
            existing.setReleaseYear(bookSetUpdates.getReleaseYear());
            existing.setQuantity(bookSetUpdates.getQuantity());
            return mapper.toDomain(repository.save(existing));
        });
    }

    @Override
    public void deleteBookSet(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}