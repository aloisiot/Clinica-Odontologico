package dao.impl;

import config.DBConnection;
import dao.IDao;
import model.Dentista;
import model.Endereco;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import service.impl.EnderecoService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DentistaDao implements IDao<Dentista> {
    private DBConnection dbConnection;
    static final Logger LOGGER = LogManager.getLogger(DentistaDao.class.getName());
    private static DentistaDao dentistaDao = null;
    private static final EnderecoService enderecoService = new EnderecoService();

    private DentistaDao() {}

    public static DentistaDao getInstance() {
        if(dentistaDao == null) dentistaDao = new DentistaDao();
        return dentistaDao;
    }

    @Override
    public Optional<Dentista> buscar(int matricula) {
        LOGGER.debug("Consultando dentista na base de dados (matricula : " + matricula +')');
        Connection connection;
        Statement statement;
        String query = String.format("SELECT * FROM dentistas WHERE matricula = %d;", matricula);
        Dentista dentista = null;

        try{
            this.dbConnection = new DBConnection();
            connection = this.dbConnection.getConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()){
                dentista = new Dentista(
                        resultSet.getInt("matricula"),
                        resultSet.getString("nome"),
                        resultSet.getString("sobrenome"),
                        resultSet.getDate("dataCadastro")
                );
            }
            LOGGER.debug("Consulta realizada com sucesso!");
            try {
                dentista.setEndereco(enderecoService.buscarReferenteAoMorador(matricula, "Dentista").get());
            } catch (Exception e){
                LOGGER.error("Endereco nulo!");
            }
            statement.close();
            connection.close();
            LOGGER.debug("Conexao finalizada!");
        }
        catch (SQLException e){
            LOGGER.error("Falha na busca!", e);
        }
        return dentista != null ? Optional.of(dentista) : Optional.empty();
    }

    @Override
    public Dentista salvar(Dentista dentista){
        LOGGER.debug("Registrando novo dentista!");
        Connection connection;
        Statement statement;
        String query = String.format(
                "INSERT INTO dentistas (nome, sobrenome ,dataCadastro) VALUES ('%s', '%s', '%s')",
                dentista.getNome(),
                dentista.getSobrenome(),
                new Date(new java.util.Date().getTime())
        );

        try{
            this.dbConnection = new DBConnection();
            connection = this.dbConnection.getConnection();
            statement = connection.createStatement();
            statement.execute(query, Statement.RETURN_GENERATED_KEYS);

            ResultSet keys = statement.getGeneratedKeys();
            while(keys.next()){
                dentista.setMatricula(keys.getInt("matricula"));
            }
            int matricula = dentista.getMatricula();
            LOGGER.debug("Dentista registrado com sucesso! - matricula: " + matricula);


            Endereco endereco = dentista.getEndereco();
            if(endereco != null){
                endereco.setIdMorador(matricula);
                endereco.setClassMorador("Dentista");
                enderecoService.salvar(dentista.getEndereco());
            }

            statement.close();
            connection.close();
            LOGGER.debug("Conexao finalizada!");
        } catch (SQLException e){
            System.err.println(e.getMessage());
            LOGGER.error("Falha na conexão!", e);
        }
        return dentista;
    }

    @Override
    public void excluir(Integer matricula) {
        LOGGER.debug("Excluindo dentista!");
        Connection connection;
        Statement statement;
        String query = String.format("DELETE FROM dentistas WHERE matricula = '%s'", matricula);
        try {
            this.dbConnection = new DBConnection();
            connection = this.dbConnection.getConnection();
            statement = connection.createStatement();
            statement.execute(query);
            LOGGER.debug("Dentista excluido!");
            statement.close();
            connection.close();;
        } catch (SQLException e) {
            LOGGER.error("Falha ao excluir!");
        }
    }

    @Override
    public List<Dentista> buscarTodos(){
        LOGGER.debug("Consultando todos os dentistas na base de dados!");
        List<Dentista> dentistas = new ArrayList<>();
        Connection connection;
        Statement statement;
        String query = "SELECT * FROM dentistas;";


        try{
            this.dbConnection = new DBConnection();
            connection = this.dbConnection.getConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            dentistas = resultSetToList(resultSet);
            LOGGER.debug("Consulta realizada com sucesso!");

            statement.close();
            connection.close();
            LOGGER.debug("Conexao finalizada!");
        } catch (SQLException e){
            LOGGER.error("Falha na busca!", e);
        }
        return dentistas;
    }

    @Override
    public List<Dentista> resultSetToList(ResultSet resultSet) throws SQLException {
        List<Dentista> dentistas = new ArrayList<>();
        while(resultSet.next()){
            int matricula = resultSet.getInt("matricula");
            Dentista dentista = new Dentista(
                    matricula,
                    resultSet.getString("nome"),
                    resultSet.getString("sobrenome"),
                    resultSet.getDate("dataCadastro")

            );
            try {
                Optional<Endereco> endereco = EnderecoDao.getInstance().buscarReferenteAoMorador(matricula, "Dentista");
                dentista.setEndereco(endereco.get());
            } catch (Exception e){
                System.err.println(e.getMessage());
            }
            dentistas.add(dentista);
        }
        return dentistas;
    }
}
