package service.impl;

import dao.impl.PacienteDao;
import model.Paciente;
import service.IService;

import java.util.List;
import java.util.Optional;

public class PacienteService implements IService<Paciente> {

    @Override
    public Optional<Paciente> buscar(Integer id)  {
            return  PacienteDao.getInstance().buscar(id);
    }

    @Override
    public List<Paciente> buscarTodos() {
            return PacienteDao.getInstance().buscarTodos();
    }

    @Override
    public Paciente salvar(Paciente paciente){
            return PacienteDao.getInstance().salvar(paciente);
    }

    @Override
    public void excluir(Integer id) {
        PacienteDao.getInstance().excluir(id);
    }
}
