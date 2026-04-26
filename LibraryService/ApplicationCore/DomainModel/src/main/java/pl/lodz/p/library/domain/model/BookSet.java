package pl.lodz.p.library.domain.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BookSet {

    private String id;

    @NotBlank(message = "Tytuł książki nie może być pusty.")
    private String title;

    @NotBlank(message = "Autor książki nie może być pusty.")
    private String author;

    @NotNull(message = "Rok wydania jest wymagany.")
    @Min(value = 0, message = "Rok wydania musi być liczbą nieujemną.")
    private int releaseYear;

    @NotNull(message = "Ilość sztuk jest wymagana.")
    @Min(value = 0, message = "Ilość sztuk na stanie musi być 0 lub większa.")
    private int quantity;

    /**
     * BookSet jako książka wraz z ilością jej sztuk na stanie
     *
     * @param title       - tytuł
     * @param author      - autor
     * @param releaseYear - rok wydania
     * @param quantity    - pozostała ilość sztuk
     */
    public BookSet(String title, String author, int releaseYear, int quantity) {
        this.title = title;
        this.author = author;
        this.releaseYear = releaseYear;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getReleaseYear() {
        return releaseYear;
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
        return "BookSet{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", releaseYear=" + releaseYear +
                ", quantity=" + quantity +
                '}';
    }
}
