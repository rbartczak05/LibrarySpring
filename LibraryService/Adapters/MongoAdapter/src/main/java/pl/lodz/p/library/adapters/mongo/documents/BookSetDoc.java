package pl.lodz.p.library.adapters.mongo.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "booksets")
public class BookSetDoc {

    @Id
    private UUID id;
    private String title;
    private String author;
    private int releaseYear;
    private int quantity;

    public BookSetDoc(String title, String author, int releaseYear, int quantity) {
        this.title = title;
        this.author = author;
        this.releaseYear = releaseYear;
        this.quantity = quantity;
    }

    public BookSetDoc() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isAvailable() {
        return quantity > 0;
    }
}