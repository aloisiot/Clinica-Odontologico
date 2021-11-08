package server;

import org.apache.log4j.Logger;
import server.handler.DentistaServiceHandler;
import server.handler.EnderecoServiceHandler;
import server.handler.PacienteServiceHandler;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class Server {

    private static final Logger LOGGER = Logger.getLogger(Server.class);

    public static void main(String[] args) throws Exception {
        LOGGER.debug("Iniciando servidor http!");
        int port = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/pacientes", new PacienteServiceHandler());
        server.createContext("/api/enderecos", new EnderecoServiceHandler());
        server.createContext("/api/dentistas", new DentistaServiceHandler());
        server.setExecutor(null);
        server.start();
        LOGGER.debug("Servidor rodando na porta " + port + "!");
    }
}