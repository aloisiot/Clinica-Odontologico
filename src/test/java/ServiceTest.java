import config.DBConnection;
import model.Dentista;
import model.Endereco;
import model.Paciente;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import service.impl.DentistaService;
import service.impl.EnderecoService;
import service.impl.PacienteService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class ServiceTest {
    private static final PacienteService pacienteService = new PacienteService();
    private static final EnderecoService enderecoService = new EnderecoService();
    private static final DentistaService dentistaService = new DentistaService();


    @BeforeClass
    public static void salvarPacienteComEndereco(){
        System.out.println("\n - Salvar pacientes e enderecos na base de dados -");

        Paciente paciente = new Paciente("Juan", "Silva", "32753526");
        Endereco endereco = new Endereco( "rua beira rio", "Bairro 1", "Sao Paulo", 44);
        paciente.setEndereco(endereco);
        pacienteService.salvar(paciente);

        Paciente paciente1 = new Paciente("Marcia", "Soares", "32753896");
        Endereco endereco1 = new Endereco("sao lucas", "centro", "Sao Paulo", 798);
        paciente.setEndereco(endereco);
        pacienteService.salvar(paciente);

        Paciente paciente2 = new Paciente("Vera", "Gomes", "84753526");
        Endereco endereco2 = new Endereco("San Calara", "bela vista", "Sao Paulo", 35);
        paciente.setEndereco(endereco);
        pacienteService.salvar(paciente);

        Dentista dentista = new Dentista("Juan", "Silva");
        Endereco endereco3 = new Endereco( "rua beira rio", "Bairro 1", "Sao Paulo", 44);
        dentista.setEndereco(endereco);
        dentistaService.salvar(dentista);

        Dentista dentista1 = new Dentista("Marcia", "Soares");
        Endereco endereco4 = new Endereco("sao lucas", "centro", "Sao Paulo", 798);
        dentista.setEndereco(endereco);
        dentistaService.salvar(dentista);

        Dentista dentista2 = new Dentista("Vera", "Gomes");
        Endereco endereco5 = new Endereco("San Calara", "bela vista", "Sao Paulo", 35);
        dentista.setEndereco(endereco);
        dentistaService.salvar(dentista);


        System.out.println(" - FIM TESTE - \n");
    }

    private static int lastIndex(String table, String idColumn){
        int r = 0;
        try{
            DBConnection dbConnection = new DBConnection();
            Connection connection = dbConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    String.format("SELECT MAX(%s) FROM %s;",idColumn , table)
            );
            while (resultSet.next()) {
                r = resultSet.getInt(1);
            }
            statement.close();
            connection.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return r;
    }

    @Test
    public void buscarPaciente(){
        System.out.println("\n - INICIANDO TESTE - \nDESCRICAO: Busca por paciente na base de dados");
        int lastIndex = lastIndex("pacientes", "idPaciente");
        Assert.assertNotNull(pacienteService.buscar(lastIndex));
        System.out.println(" - FIM TESTE - \n");
    }

    @Test
    public void buscarTodosPacientes() {
        System.out.println("\n - INICIANDO TESTE - \nDESCRICAO: Busca por todos os pacientes na base de dados");
        Assert.assertTrue(pacienteService.buscarTodos().size() > 0);
        System.out.println(" - FIM TESTE - \n");
    }

    @Test
    public void excluirPaciente(){
        System.out.println("\n - INICIANDO TESTE - \nDESCRICAO: Excluir paciente da base de dados");
        int lastIndex = lastIndex("pacientes", "idPaciente");
        pacienteService.excluir(lastIndex);
        Assert.assertTrue(pacienteService.buscar(lastIndex).isEmpty());
        System.out.println(" - FIM TESTE - \n");
    }

    @Test
    public void buscarEndereco() {
        System.out.println(" - INICIANDO TESTE - \nDESCRICAO: Busca por endereco na base de dados");
        int lastIndex = lastIndex("enderecos", "idEndereco");
        Assert.assertNotNull(enderecoService.buscar(lastIndex));
        System.out.println(" - FIM TESTE - \n");
    }

    @Test
    public void buscarTodosEnderecos(){
        System.out.println("\n - INICIANDO TESTE - \nDESCRICAO: Busca por todos os endereços na base de dados");
        Assert.assertTrue(enderecoService.buscarTodos().size() > 0);
        System.out.println(" - FIM TESTE - \n");
    }

    @Test
    public void excluirEndereco(){
        System.out.println("\n - INICIANDO TESTE - \nDESCRICAO: Excluir paciente da base de dados");
        int lastIndex = lastIndex("enderecos", "idEndereco");
        enderecoService.excluir(lastIndex);
        Assert.assertTrue(enderecoService.buscar(lastIndex).isEmpty());
        System.out.println(" - FIM TESTE - \n");
    }

    @Test
    public void buscarEnderecoReferenteAoMorador(){
        System.out.println("\n - INICIANDO TESTE - \nDESCRICAO: Busca por endereco referente a determinado morador");
        int lastIndexOfPaciente = lastIndex("pacientes", "idPaciente");
        Optional<Endereco> endereco = enderecoService.buscarReferenteAoMorador(lastIndexOfPaciente, "Paciente");
        Assert.assertNotNull(endereco);
        System.out.println(" - FIM TESTE - \n");
    }

    @Test
    public void buscarDestista(){
        System.out.println(" - INICIANDO TESTE - \nDESCRICAO: Busca por dentista na base de dados");
        int lastIndex = lastIndex("dentistas", "matricula");
        Assert.assertNotNull(dentistaService.buscar(lastIndex));
        System.out.println(" - FIM TESTE - \n");
    }

    @Test
    public void excluirDestista(){
        System.out.println("\n - INICIANDO TESTE - \nDESCRICAO: Excluir dentista da base de dados");
        int lastIndex = lastIndex("dentistas", "matricula");
        dentistaService.excluir(lastIndex);
        Assert.assertTrue(dentistaService.buscar(lastIndex).isEmpty());
        System.out.println(" - FIM TESTE - \n");
    }

    @Test
    public void buscarTodosDentistas(){
        System.out.println("\n - INICIANDO TESTE - \nDESCRICAO: Busca por todos os dentistas na base de dados");
        Assert.assertTrue(dentistaService.buscarTodos().size() > 0);
        System.out.println(" - FIM TESTE - \n");
    }
}
