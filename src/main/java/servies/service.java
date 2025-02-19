package servies;
import java.util.List;
public interface service<T> {
    void add(T t);
    void update(T t);
    void delete(int voyage);
    List<T> getAll();
}