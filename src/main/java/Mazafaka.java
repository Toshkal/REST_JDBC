import dao.AuthorDAO;
import entity.Author;
import utils.ConnectionPool;

import java.util.Optional;

public class Mazafaka {
    public static void main(String[] args) {
        AuthorDAO authorDAO = AuthorDAO.getInstance();
        Optional<Author> byID = authorDAO.getByID(1L, ConnectionPool.get());
        System.out.println(byID.get());
    }
}
