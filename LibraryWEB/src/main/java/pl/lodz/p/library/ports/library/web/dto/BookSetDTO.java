package pl.lodz.p.library.ports.library.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.http.HttpStatus;
import pl.lodz.p.library.ports.library.web.exceptions.BookSetQuantityException;

public class BookSetDTO {

    @Id
    private String id;
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @Min(0)
    private int releaseYear;
    @Min(0)
    private int quantity;

    public BookSetDTO(String id, String title, String author, int releaseYear, int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.releaseYear = releaseYear;
        this.quantity = quantity;
    }

    public BookSetDTO() {

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
        if (quantity >= 0) {
            this.quantity = quantity;
        } else {
            throw new BookSetQuantityException(HttpStatus.CONFLICT, "Quantity must be greater than or equal to 0");
        }
    }

    public boolean isAvailable() {
        return quantity > 0;
    }
}
