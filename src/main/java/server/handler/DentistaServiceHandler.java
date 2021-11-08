package server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Dentista;
import org.apache.log4j.Logger;
import service.impl.DentistaService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.NoSuchElementException;

public class DentistaServiceHandler implements HttpHandler {
    private static final Logger LOGGER = Logger.getLogger(DentistaServiceHandler.class);
    private final static DentistaService dentistaService = new DentistaService();

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
                } catch (NoSuchElementException e){
                    LOGGER.debug(e.getMessage());
                    responseString = "{{\"dentista\" : null}";
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

    private static String get ( String matricula )  throws NoSuchElementException {
        String response;
        if(matricula.equalsIgnoreCase("all")) {
            response = Util.listToString(dentistaService.buscarTodos());
        } else {
            int id = Integer.valueOf(matricula);
            response = "[ " + dentistaService.buscar(id).get() + " ]";
        }
        return response;
    }

    private static String delete ( String marticula){
        int id = Integer.valueOf(marticula);
        dentistaService.excluir(id);
        return "ok";
    }

    private static String post (InputStream bodyRequest) throws IOException {
        Map<String, String> bodyMap = Util.requestBodyReader(bodyRequest);
        Dentista dentista = null;
        String nome = bodyMap.get("nome");
        String sobrenome = bodyMap.get("sobrenome");
        if(nome != null && sobrenome != null) {
            dentista = new Dentista(nome, sobrenome);
        }
        return dentistaService.salvar(dentista).toString();
    }
}
