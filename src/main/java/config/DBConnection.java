package config;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private final String URL ;
    private final String USER;
    private final String PASSWORD;

    private final Logger LOGGER = Logger.getLogger(DBConnection.class);

    public DBConnection(){
        this.URL = "jdbc:h2:~/clinica;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'src/main/resources/create.sql'";
        this.USER = "sa";
        this.PASSWORD = "";
    }

    public Connection getConnection(){
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(this.URL, this.USER, this.PASSWORD);
            LOGGER.debug("Conexao bem sucedida!");
        }
        catch (SQLException e){
            e.printStackTrace();
            LOGGER.debug("Conexao Mal sucedida!", e);
        }
        return connection;
    }
}
