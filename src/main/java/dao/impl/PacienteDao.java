package dao.impl;

import config.DBConnection;
import dao.IDao;
import model.Endereco;
import model.Paciente;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import service.impl.EnderecoService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PacienteDao implements IDao<Paciente> {
    private DBConnection dbConnection;
    static final Logger LOGGER = LogManager.getLogger(PacienteDao.class.getName());
    private static PacienteDao pacienteDao = null;
    private static final EnderecoService enderecoService = new EnderecoService();

    private PacienteDao(){}

    public static PacienteDao getInstance(){
        if(pacienteDao == null) pacienteDao = new PacienteDao();
        return pacienteDao;
    }

    @Override
    public Optional<Paciente> buscar(int idPaciente){
        LOGGER.debug("Consultando paciente na base de dados (idPaciente : " + idPaciente +')');
        Connection connection;
        Statement statement;
        String query = String.format("SELECT * FROM pacientes WHERE idPaciente = %d;", idPaciente);
        Paciente paciente = null;

        try{
            this.dbConnection = new DBConnection();
            connection = this.dbConnection.getConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                paciente = new Paciente(
                        resultSet.getInt("idPaciente"),
                        resultSet.getString("nome"),
                        resultSet.getString("sobrenome"),
                        resultSet.getString("rg"),
                        resultSet.getDate("dataCadastro")
                );
            }
            LOGGER.debug("Consulta realizada com sucesso!");
            try {
                paciente.setEndereco(enderecoService.buscarReferenteAoMorador(idPaciente, "Paciente").get());
            } catch (Exception e){
                LOGGER.error("Endereco nulo!");
            }
            statement.close();
            connection.close();
            LOGGER.debug("Conexao finalizada!");
        } catch (SQLException e){
            LOGGER.error("Falha na busca!", e);
        }
        return paciente != null ? Optional.of(paciente) : Optional.empty();
    }

    @Override
    public List<Paciente> buscarTodos(){
        LOGGER.debug("Consultando todos os pacientes na base de dados!");
        List<Paciente> pacientes = new ArrayList<>();
        Connection connection;
        Statement statement;
        String query = "SELECT * FROM pacientes;";


        try{
            this.dbConnection = new DBConnection();
            connection = this.dbConnection.getConnection();
            statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            pacientes = resultSetToList(result);
            LOGGER.debug("Consulta realizada com sucesso!");

            statement.close();
            connection.close();
            LOGGER.debug("Conexao finalizada!");
        } catch (SQLException e){
            LOGGER.error("Falha na busca!", e);
        }
        return pacientes;
    }

    @Override
    public Paciente salvar(Paciente paciente) {
        LOGGER.debug("Registrando novo paciente!");
        Connection connection;
        Statement statement;
        String query = String.format(
                "INSERT INTO pacientes (nome, sobrenome ,rg ,dataCadastro) VALUES ('%s', '%s', '%s', '%s')",
                paciente.getNome(),
                paciente.getSobrenome(),
                paciente.getRg(),
                new Date(new java.util.Date().getTime())
        );

        try{
            this.dbConnection = new DBConnection();
            connection = this.dbConnection.getConnection();
            statement = connection.createStatement();
            statement.execute(query, Statement.RETURN_GENERATED_KEYS);

            ResultSet keys = statement.getGeneratedKeys();
            while(keys.next()){
                paciente.setId(keys.getInt("idPaciente"));
            }
            int idPaciente = paciente.getId();
            LOGGER.debug("Paciente registrado com sucesso! - idPaciente: " + idPaciente);

            Endereco endereco = paciente.getEndereco();
            if(endereco != null) {
                endereco.setIdMorador(idPaciente);
                endereco.setClassMorador("Paciente");
                enderecoService.salvar(paciente.getEndereco());
            }
            statement.close();
            connection.close();
            LOGGER.debug("Conexao finalizada!");
        } catch (SQLException e){
            LOGGER.error("Falha na conexão!", e);
        }
        return paciente;
    }

    @Override
    public void excluir(Integer id) {
        LOGGER.debug("Excluindo paciente!");
        Connection connection;
        Statement statement;
        String query = String.format("DELETE FROM pacientes WHERE idPaciente = %s", id);
        try {
            this.dbConnection = new DBConnection();
            connection = this.dbConnection.getConnection();
            statement = connection.createStatement();
            statement.execute(query);
            LOGGER.debug("Paciente excluido!");
            statement.close();
            connection.close();
        } catch (SQLException throwables) {
            LOGGER.error("Falha ao excluir!");
        }
    }

    @Override
    public List<Paciente> resultSetToList(ResultSet rs) throws SQLException{
        List<Paciente> pacientes = new ArrayList<>();
        EnderecoService enderecoService = new EnderecoService();
        while(rs.next()){
            int idPaciente = rs.getInt("idPaciente");
            Paciente paciente = new Paciente(
                    idPaciente,
                    rs.getString("nome"),
                    rs.getString("sobrenome"),
                    rs.getString("rg"),
                    rs.getDate("dataCadastro")
            );
            try {
                Endereco endereco = EnderecoDao.getInstance().buscarReferenteAoMorador(idPaciente, "Paciente").get();
                paciente.setEndereco(endereco);
            } catch (Exception e){
                System.err.println(e.getMessage());
            }
            pacientes.add(paciente);
        }
        return pacientes;
    }
}
