package service;

import dao.BookDAO;
import entity.Book;
import utils.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BookService {
    private static final BookService INSTANCE = new BookService();
    private static final BookDAO bookDAO = BookDAO.getInstance();

    private BookService() {
    }

    public static BookService getInstance() {
        return INSTANCE;
    }

    public Book getByID(Long id) {
        try (Connection connection = ConnectionPool.get()) {
            return bookDAO.getByID(id, connection).orElse(null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Book> getAll() {
        try (Connection connection = ConnectionPool.get()) {
            return bookDAO.getAll(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Book save(Book book) {
        try (Connection connection = ConnectionPool.get()) {
            return bookDAO.save(book, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Book film) {
        try (Connection connection = ConnectionPool.get()) {
            bookDAO.update(film, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(Long id){
        try (Connection connection = ConnectionPool.get()) {
            return bookDAO.delete(id, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
