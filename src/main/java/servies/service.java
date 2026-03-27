package servies;
import java.sql.SQLException;
import java.util.List;
public interface service<T> {
    void add(T t) throws SQLException;
    void update(T t);
    void delete(int voyage);
    List<T> getAll();
}