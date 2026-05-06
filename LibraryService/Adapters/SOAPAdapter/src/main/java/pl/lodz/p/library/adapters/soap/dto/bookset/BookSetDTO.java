package pl.lodz.p.library.adapters.soap.dto.bookset;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.UUID;

@XmlType(name = "bookSetDTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class BookSetDTO {
    @XmlElement(name = "id", required = true)
    private UUID id;

    @NotBlank(message = "Tytuł książki nie może być pusty.")
    @XmlElement(name = "title", required = true)
    private String title;

    @NotBlank(message = "Autor książki nie może być pusty.")
    @XmlElement(name = "author", required = true)
    private String author;

    @NotNull(message = "Rok wydania jest wymagany.")
    @Min(value = 0, message = "Rok wydania musi być liczbą nieujemną.")
    @XmlElement(name = "releaseYear", required = true)
    private int releaseYear;

    @NotNull(message = "Ilość sztuk jest wymagana.")
    @Min(value = 0, message = "Ilość sztuk na stanie musi być 0 lub większa.")
    @XmlElement(name = "quantity", required = true)
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
