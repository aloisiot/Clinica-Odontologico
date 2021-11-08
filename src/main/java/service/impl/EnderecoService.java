package service.impl;

import dao.impl.EnderecoDao;
import model.Endereco;
import service.IService;

import java.util.List;
import java.util.Optional;

public class EnderecoService implements IService<Endereco> {
    @Override
    public Optional<Endereco> buscar(Integer id)  {
        return EnderecoDao.getInstance().buscar(id);
    }

    @Override
    public List<Endereco> buscarTodos() {
        return EnderecoDao.getInstance().buscarTodos();
    }

    @Override
    public Endereco salvar(Endereco endereco){
        return EnderecoDao.getInstance().salvar(endereco);
    }

    @Override
    public void excluir(Integer id) {
        EnderecoDao.getInstance().excluir(id);
    }

    public Optional<Endereco> buscarReferenteAoMorador(int idMorador, String classMorador){
        return EnderecoDao.getInstance().buscarReferenteAoMorador(idMorador, classMorador);
    }
}
