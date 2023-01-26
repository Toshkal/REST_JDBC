package dao;

import entity.Author;
import entity.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDAO implements CrudAuthor<Long, Author> {
    private static final AuthorDAO INSTANCE = new AuthorDAO();
    // переменная для запроса на просмотр таблицы авторов
    private static final String getAll = """       
            SELECT a.id, 
            a.fname, 
            a.lastname 
            FROM autor as a
            """;
    // переменная для запроса на просмотр автора по ID
    private static final String getByID = getAll + """
            WHERE id = ?
            """;
    //
    private static final String save = """
            INSERT INTO autor (fname, lastname)
            VALUES (?,?)
            """;
    private static final String update = """
            UPDATE autor 
            SET fname = ?, 
            lastname = ? 
            WHERE id = ?
            """;
    private static final String delete = """
            DELETE FROM autor 
            WHERE id = ?
            """;
    private static final String getBookALLByAuthor = """
            SELECT b.id, b.binding, b.title, b.numberPages 
            FROM book as b 
            LEFT JOIN autor as a on a.id = b.autor_id 
            WHERE a.id = ?
            """;

    private AuthorDAO(){
    }
    public static AuthorDAO getInstance(){
        return INSTANCE;
    }

    @Override
    public List<Author> getAll(Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(getAll)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Author> author = new ArrayList<>();

            while (resultSet.next()) {
                author.add(buildAuthor(resultSet));
            }
            return author;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Author> getByID(Long id, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(getByID)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            Author author = null;
            if (resultSet.next()) {
                author = buildAuthor(resultSet);
                author.setBooks(getBookALLByAuthorId(id, connection));
            }
            return Optional.ofNullable(author);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Author save(Author entity, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(save, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getLastName());

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
    public void update(Author entity, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(update)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setLong(3, entity.getId());

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
    public List<Book> getBookALLByAuthorId(Long id, Connection connection){
        try (PreparedStatement preparedStatement = connection.prepareStatement(getBookALLByAuthor)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Book> book = new ArrayList<>();
            while (resultSet.next()){
                book.add(BookDAO.getInstance().buildBook(resultSet));
            }
            return book;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected Author buildAuthor(ResultSet resultSet) throws SQLException {
        return new Author(
                resultSet.getLong("id"),
                resultSet.getString("fname"),
                resultSet.getString("lastname")
        );
    }
}
