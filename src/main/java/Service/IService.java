package Service;

import java.util.List;

public interface IService<T> {
    void insert  (T T);

    void update (T T);


    void deleteById(int id);

    List<T> getAll();
    T getById(int id);

}