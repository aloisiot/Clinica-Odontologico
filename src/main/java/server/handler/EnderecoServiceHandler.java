package server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Endereco;
import org.apache.log4j.Logger;
import service.impl.EnderecoService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.NoSuchElementException;

public class EnderecoServiceHandler implements HttpHandler {
    private static final Logger LOGGER = Logger.getLogger(EnderecoServiceHandler.class);
    private final static EnderecoService enderecoService = new EnderecoService();

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
                    LOGGER.info(e.getMessage());
                    responseString = "{\"endereco\" : null}";
                    status =  404;
                } catch (Exception e){
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

    private static String get ( String idEndereco ) throws NoSuchElementException {
        String response;
        if(idEndereco.equalsIgnoreCase("all")) {
            response = Util.listToString(enderecoService.buscarTodos());
        } else {
            int id = Integer.valueOf(idEndereco);
            response = "[ " + enderecoService.buscar(id).get()+ " ]";
        }
        return response;
    }

    private static String delete ( String idEndereco){
        int id = Integer.valueOf(idEndereco);
        enderecoService.excluir(id);
        return "ok";
    }

    private static String post(InputStream bodyRequest) throws IOException {
        Map<String, String> bodyMap = Util.requestBodyReader(bodyRequest);
        System.out.println(bodyMap);
        String rua = bodyMap.get("rua");
        String bairro = bodyMap.get("bairro");
        String cidade = bodyMap.get("cidade");
        String classMorador = bodyMap.get("classMorador");
        Integer idMorador = Integer.valueOf(bodyMap.get("idMorador"));
        Integer numero = Integer.valueOf(bodyMap.get("numero"));
        Endereco endereco = null;
        if(rua != null && bairro != null && cidade != null &&
                classMorador!= null && idMorador != null && numero != null ){
            endereco = new Endereco( rua, bairro, cidade, classMorador, idMorador, numero);
        }
        return enderecoService.salvar(endereco).toString();
    }
}
