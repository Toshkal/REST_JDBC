package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Book {
    private Long id;
    private String binding;
    private String title;
    private Integer numberPages;
    private Author author;

    public Book() {
    }

    public Book(Long id, String binding, String title, Integer numberPages) {
        this.id = id;
        this.binding = binding;
        this.title = title;
        this.numberPages = numberPages;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", binding='" + binding + '\'' +
                ", title='" + title + '\'' +
                ", numberPages=" + numberPages +
                ", author=" + author +
                '}' + "\n";
    }
}
