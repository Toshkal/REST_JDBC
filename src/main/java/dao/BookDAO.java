package dao;

import entity.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDAO implements CrudAuthor<Long, Book> {
    private static final BookDAO INSTANCE = new BookDAO();

    private static final String getAll = """
            SELECT book.id
            binding,
            title,
            numberPages
            FROM book
            """;

    private static final String getByID = getAll + """
            WHERE id = ?
            """;

    private static final String save = """
            INSERT INTO  book(binding, title, numberPages) 
            VALUES (?,?,?)
            """;

    private static final String update = """
            UPDATE book
            SET binding = ?,
                title = ?,
                numberPages = ?
            WHERE id = ?
            """;

    private static final String delete = """
            DELETE FROM book
            WHERE id = ?
            """;

    private BookDAO() {
    }

    public static BookDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Book> getAll(Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(getAll)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Book> books = new ArrayList<>();

            while (resultSet.next()) {
                books.add(buildBook(resultSet));
            }
            return books;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Book> getByID(Long id, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(getByID)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            Book book = null;
            if (resultSet.next()) {
                book = buildBook(resultSet);
            }
            return Optional.ofNullable(book);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Book save(Book entity, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(save, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getBinding());
            preparedStatement.setString(2, entity.getTitle());
            preparedStatement.setString(3, String.valueOf(entity.getNumberPages()));

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getLong("id"));
            }
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Book entity, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(update)) {
            preparedStatement.setString(1, entity.getBinding());
            preparedStatement.setString(2, entity.getTitle());
            preparedStatement.setString(3, String.valueOf(entity.getNumberPages()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Long id, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(delete)) {
            preparedStatement.setLong(1, id);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected Book buildBook(ResultSet resultSet) throws SQLException {
        return new Book(resultSet.getLong(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getInt(4));
    }
}
