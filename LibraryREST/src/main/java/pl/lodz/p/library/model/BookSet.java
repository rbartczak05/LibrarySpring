package pl.lodz.p.library.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpStatus;
import pl.lodz.p.library.exception.BookSetHasInvalidFieldValueException;
import pl.lodz.p.library.exception.BookSetQuantityException;

@Document(collection = "booksets")
public class BookSet {
    @Id
    private String id;

    @NotBlank
    private final String title;

    @NotBlank
    private final String author;

    @NotNull
    @Min(0)
    private final int releaseYear;

    @NotNull
    @Min(0)
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
        if (title == null || title.isEmpty() || author == null || author.isEmpty() || releaseYear < 0) {
            throw new BookSetHasInvalidFieldValueException(HttpStatus.CONFLICT,
                    "Title, author or release year can't be null or empty");
        }
        this.title = title;
        this.author = author;
        this.releaseYear = releaseYear;
        if (quantity < 0) {
            throw new BookSetQuantityException(HttpStatus.CONFLICT, "Quantity must be greater than or equal to 0");
        }
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
        if (quantity >= 0) {
            this.quantity = quantity;
        } else {
            throw new BookSetQuantityException(HttpStatus.CONFLICT, "Quantity must be greater than or equal to 0");
        }
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
