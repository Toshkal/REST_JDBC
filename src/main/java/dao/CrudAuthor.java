package dao;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface CrudAuthor<K, E>{
    List<E> getAll(Connection connection);             // посмотреть всех
    Optional<E> getByID(K id, Connection connection);  // посмотреть по ID
    E save(E entity, Connection connection);            // сохранить
    void update(E entity, Connection connection);       // обновить запись
    boolean delete(K id, Connection connection);        // удалить запись
}
