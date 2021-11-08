package dao;

import model.Endereco;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface IDao<T> {
    Optional<T> buscar(int id);
    T salvar(T t) throws SQLException;
    void excluir(Integer id);
    List<T> buscarTodos() throws SQLException;
    List<T>resultSetToList(ResultSet resultSet) throws SQLException;

}
