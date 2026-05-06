package pl.lodz.p.library.adapters.rest.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;

import java.util.UUID;

public class BookSetDTO {

    @Id
    @Nullable
    private UUID id;

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

    public BookSetDTO(UUID id, String title, String author, int releaseYear, int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.releaseYear = releaseYear;
        this.quantity = quantity;
    }

    public BookSetDTO() {

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
