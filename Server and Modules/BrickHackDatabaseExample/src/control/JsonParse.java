package control;

import java.util.HashMap;

public class JsonParse {

    public HashMap<String, String> parseJSON(String json){

        String[] data = json.trim().split("&");
        HashMap<String, String> parameters = new HashMap<>();

        for (int i = 0; i < data.length; i++){
            String[] d = data[i].trim().split("=");
            parameters.put(d[0],d[1]);
        }

        return parameters;
    }
}
