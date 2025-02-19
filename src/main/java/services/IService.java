package services;

import java.util.List;

public interface IService<T> {
    void add(T t);
    void update(T t);  // ✅ Méthode obligatoire
    void delete(T t);
    T getById(int id);
    List<T> getAll();
}
