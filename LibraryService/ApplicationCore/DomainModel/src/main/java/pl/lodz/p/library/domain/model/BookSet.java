package pl.lodz.p.library.domain.model;

import java.util.UUID;

public class BookSet {
    private UUID id;

    private String title;

    private String author;

    private int releaseYear;

    private int quantity;

    public BookSet(String title, String author, int releaseYear, int quantity) {
        this.title = title;
        this.author = author;
        this.releaseYear = releaseYear;
        this.quantity = quantity;
    }

    public BookSet() {
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

    @Override
    public String toString() {
        return "BookSet{" + "id='" + id + '\'' + ", title='" + title + '\'' + ", author='" + author + '\'' + ", releaseYear=" + releaseYear + ", quantity=" + quantity + '}';
    }
}