package service;

import java.util.List;
import java.util.Optional;

public interface IService<T> {
    Optional<T> buscar(Integer id);
    T salvar(T t);
    List<T> buscarTodos();
    void excluir(Integer id);
}
