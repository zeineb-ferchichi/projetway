package services;

import java.util.List;

public interface IService<T> {
    void add(T entity);
    boolean delete(int id); // ✅ Correction : prend un ID et non un objet
    boolean update(T entity);
    List<T> getAll();
    T getById(int id);
}
