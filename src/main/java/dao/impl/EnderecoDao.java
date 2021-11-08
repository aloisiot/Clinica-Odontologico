package dao.impl;

import config.DBConnection;
import dao.IDao;
import model.Endereco;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnderecoDao implements IDao<Endereco> {
    private static DBConnection dbConnection;
    static final Logger LOGGER = LogManager.getLogger(EnderecoDao.class.getName());
    private static EnderecoDao enderecoDao;

    private EnderecoDao(){}

    public static EnderecoDao getInstance(){
        if(enderecoDao != null){
            return enderecoDao;
        }
        return new EnderecoDao();
    }
    @Override
    public Optional<Endereco> buscar(int idEndereco){
        LOGGER.debug("Consultando endereco na base de dados (idEndereco : " + idEndereco +')');
        Connection connection = null;
        Statement statement = null;
        String query = String.format("SELECT * FROM enderecos WHERE idEndereco = %d;", idEndereco);
        Endereco endereco = null;

        try {
            this.dbConnection = new DBConnection();
            connection = this.dbConnection.getConnection();
            statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            while(result.next()){
                endereco = new Endereco(
                        result.getInt("idEndereco"),
                        result.getInt("idMorador"),
                        result.getString("rua"),
                        result.getString("bairro"),
                        result.getString("cidade"),
                        result.getString("classMorador"),
                        result.getInt("numero")
                );
            }
            LOGGER.info("Consulta realizada com sucesso!");

            statement.close();
            connection.close();
            LOGGER.debug("Conexao finalizada!");
        }
        catch (SQLException e){
            LOGGER.error("Falha na busca!", e);
        }
        return endereco != null ? Optional.of(endereco) : Optional.empty();
    }

    public Optional<Endereco> buscarReferenteAoMorador(int idMorador, String classMorador){
        LOGGER.debug("Consultando endereco referente determinado morador (idMorador : " + idMorador +')');
        Endereco endereco = null;
        Connection connection= null;
        Statement statement = null;
        String query = String.format("SELECT * FROM enderecos WHERE idMorador = %d AND classMorador = '%s' LIMIT 1;",
                idMorador,
                classMorador
        );

        try {
            this.dbConnection = new DBConnection();
            connection = this.dbConnection.getConnection();
            statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            while(result.next()){
                endereco = new Endereco(
                        result.getInt("idEndereco"),
                        result.getInt("idMorador"),
                        result.getString("rua"),
                        result.getString("bairro"),
                        result.getString("cidade"),
                        result.getString("classMorador"),
                        result.getInt("numero")
                );
            }
            LOGGER.info("Consulta realizada com sucesso!");

            statement.close();
            connection.close();
            LOGGER.debug("Conexao finalizada!");
        }
        catch (SQLException e){
            LOGGER.error("Falha na conexao!", e);
        }
        return endereco != null ? Optional.of(endereco) : Optional.empty();
    }

    @Override
    public List<Endereco> buscarTodos(){
        LOGGER.debug("Consultando todos os enderecos na base de dados!");
        List<Endereco> enderecos = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        String query = "SELECT * FROM enderecos;";

        try {
            this.dbConnection = new DBConnection();
            connection = this.dbConnection.getConnection();
            statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            enderecos = resultSetToList(result);


            statement.close();
            connection.close();
            LOGGER.debug("Conexao finalizada!");
        }catch (SQLException e){
            LOGGER.error("Falha na busca!", e);
        }

        return enderecos;
    }

    @Override
    public Endereco salvar(Endereco endereco){
        LOGGER.debug("Registrando novo endereco!");
        Connection connection = null;
        Statement statement = null;
        String sql = String.format(
                "INSERT INTO enderecos (idMorador, classMorador,rua, bairro, cidade, numero) VALUES (%s, '%s', '%s', '%s', '%s', %s);",
                endereco.getIdMorador(),
                endereco.getClassMorador(),
                endereco.getRua(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getNumero()
        );

        try{
            this.dbConnection = new DBConnection();
            connection = this.dbConnection.getConnection();
            statement = connection.createStatement();
            statement.execute(sql, Statement.RETURN_GENERATED_KEYS);

            ResultSet keys = statement.getGeneratedKeys();
            while(keys.next()){
                endereco.setIdEndereco(keys.getInt("idEndereco"));
            }
            LOGGER.debug("Endereco registrado com sucesso! - idEndereco: " + endereco.getIdEndereco());

            statement.close();
            connection.close();
            LOGGER.debug("Conexao finalizada!");
        }
        catch (SQLException e){
            e.printStackTrace();
            LOGGER.error("Falha na conexão!", e);
        }

        return endereco;
    }

    public void excluir(Integer id) {
        LOGGER.debug("Excluindo endereco!");
        Connection connection;
        Statement statement;
        String query = String.format("DELETE FROM enderecos WHERE idEndereco = '%s'", id);
        try {
            this.dbConnection = new DBConnection();
            connection = this.dbConnection.getConnection();
            statement = connection.createStatement();
            statement.execute(query);
            LOGGER.debug("Endereco excluido!");
            statement.close();
            connection.close();;
        } catch (SQLException e) {
            LOGGER.error("Falha ao excluir");
        }
    }

    public List<Endereco> resultSetToList(ResultSet rs) throws SQLException{
        List<Endereco> enderecos = new ArrayList<>();
        while(rs.next()){
            Endereco endereco = new Endereco(
                    rs.getInt("idEndereco"),
                    rs.getInt("idMorador"),
                    rs.getString("rua"),
                    rs.getString("bairro"),
                    rs.getString("cidade"),
                    rs.getString("classMorador"),
                    rs.getInt("numero")

            );
            enderecos.add(endereco);
        }
        return enderecos;
    }

}
