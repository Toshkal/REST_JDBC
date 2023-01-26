package service;

import dao.AuthorDAO;
import entity.Author;
import utils.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AuthorService {
    private static final AuthorService INSTANCE = new AuthorService();
    private static final AuthorDAO authorDAO = AuthorDAO.getInstance();

    private AuthorService(){}

    public static AuthorService getInstance(){
        return INSTANCE;
    }

    public Author getByID(Long id) {
        try (Connection connection = ConnectionPool.get()) {
            return authorDAO.getByID(id, connection).orElse(null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Author> getAll() {
        try (Connection connection = ConnectionPool.get()) {
            return authorDAO.getAll(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Author save(Author author) {
        try (Connection connection = ConnectionPool.get()) {
            return authorDAO.save(author, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Author author) {
        try (Connection connection = ConnectionPool.get()) {
            authorDAO.update(author, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(Long id) {
        try (Connection connection = ConnectionPool.get()) {
            return authorDAO.delete(id, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
