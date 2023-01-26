package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Getter
@Setter
@AllArgsConstructor
public class Author {
    private Long id;
    private String name;
    private String lastName;
    private List<Book> books;

    public Author() {
    }

    public Author(Long id, String name, String lastName) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.books = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(id, author.id) && Objects.equals(name, author.name) && Objects.equals(lastName, author.lastName) && Objects.equals(books, author.books);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lastName);
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", fname='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                '}' + "\n";
    }
}
