package service.impl;

import dao.impl.DentistaDao;
import model.Dentista;
import service.IService;

import java.util.List;
import java.util.Optional;

public class DentistaService implements IService<Dentista> {
    @Override
    public Optional<Dentista> buscar(Integer matricula) {
        return DentistaDao.getInstance().buscar(matricula);
    }

    @Override
    public Dentista salvar(Dentista dentista) {
        return DentistaDao.getInstance().salvar(dentista);
    }

    @Override
    public List<Dentista> buscarTodos() {
        return DentistaDao.getInstance().buscarTodos();
    }

    @Override
    public void excluir(Integer matricula) {
        DentistaDao.getInstance().excluir(matricula);
    }
}
