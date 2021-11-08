package server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Paciente;
import org.apache.log4j.Logger;
import service.impl.PacienteService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.NoSuchElementException;

public class PacienteServiceHandler  implements HttpHandler {
    private static final Logger LOGGER = Logger.getLogger(PacienteServiceHandler.class);
    private final static PacienteService pacienteService = new PacienteService();

    public void handle(HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders().set("Content-Type", "application/json");
        String requestMethod = httpExchange.getRequestMethod();
        InputStream bodyRequest = httpExchange.getRequestBody();
        String responseString = null;
        int status = 200;

        String uri = httpExchange.getRequestURI().toString();
        String params = null;
        if(uri.contains("?")) params = uri.split("\\?")[1];

        switch (requestMethod) {
            case "GET":
                try{
                    responseString = get(params);
                    LOGGER.info("Requisicao http GET: Sucesso!");
                }catch (NoSuchElementException e){
                    LOGGER.info(e.getMessage());
                    responseString = "{\"paciente\" : null}";
                    status =  404;
                }catch (Exception e){
                    status = 400;
                    responseString = "Falha!";
                    LOGGER.error("Requisicao http GET: Falha!", e);
                }
                break;

            case "DELETE":
                try {
                    responseString = delete(params);
                    LOGGER.error("Requisicao http DELETE: Sucesso!");
                } catch (Exception e){
                    status = 400;
                    responseString = "Falha!";
                    LOGGER.error("Requisicao http DELETE: Falha!", e);
                }
                break;

            case "POST":
                try{
                    responseString = post(bodyRequest);
                    LOGGER.info("Requisicao POST: Sucesso!");
                }catch (Exception e){
                    status = 400;
                    responseString = "Falha!";
                    LOGGER.error("Requisicao POST: Falha!");
                }
        }

        byte[] response = responseString.getBytes(StandardCharsets.UTF_8);
        httpExchange.sendResponseHeaders(status, response.length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response);
        os.close();

    }


    private static String get ( String idPaciente ) throws NoSuchElementException {
        String response;
        if(idPaciente.equalsIgnoreCase("all")) {
            response = Util.listToString(pacienteService.buscarTodos());
        } else {
            int id = Integer.valueOf(idPaciente);
            response = "[ " + pacienteService.buscar(id).get() + " ]";
        }
        return response;
    }

    private static String delete ( String idPaciente){
        int id = Integer.valueOf(idPaciente);
        pacienteService.excluir(id);
        return "ok";
    }

    private static String post(InputStream bodyRequest) throws IOException {
        Map<String, String> bodyMap = Util.requestBodyReader(bodyRequest);
        String nome = bodyMap.get("nome");
        String sobrenome = bodyMap.get("sobrenome");
        String rg  =bodyMap.get("rg");
        Paciente paciente = null;
        if(nome != null && sobrenome != null && rg != null){
            paciente = new Paciente( nome, sobrenome, rg );
        }
        return pacienteService.salvar(paciente).toString();
    }
}
