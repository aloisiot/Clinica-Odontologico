package server.handler;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Util {
    public static String listToString(List o){
        String pacientesStrg = o.get(0).toString();
        for (int i = 1 ; i < o.size(); i++)
            pacientesStrg = String.format("%s, %s", pacientesStrg, o.get(i).toString());
        return "[" + pacientesStrg + "]";
    }

    public static Map<String, String> requestBodyReader(InputStream inputStream)
            throws IOException {
        Map<String, String> requestBodyMap = new HashMap<>();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        int b;
        StringBuilder stringBuilder = new StringBuilder();
        while ((b = bufferedReader.read()) != -1) {
            stringBuilder.append((char) b);
        }

        String[] params = stringBuilder.toString().split("&");
        for (int i = params.length - 1; i >= 0; i--) {
            String[] param = params[i].split("=");
            requestBodyMap.put(param[0], param[1]);
        }

        bufferedReader.close();
        inputStreamReader.close();
        return requestBodyMap;
    }
}
