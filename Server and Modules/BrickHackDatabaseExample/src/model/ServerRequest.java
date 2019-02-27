package model;

import control.JsonParse;
import java.util.HashMap;

public class ServerRequest{

    private String query;
    private RequestType type;
    private JsonParse parse;

    private HashMap<String,String> parameters;

    public enum RequestType{
        Register,
        Login,
        Data,
        Verify
    }

    public ServerRequest(String query, ServerRequest.RequestType type, JsonParse parse){
        this.query = query;
        this.type = type;
        this.parse = parse;
    }

    public HashMap<String,String> parse(){
        parameters = parse.parseJSON(query);
        return parameters;
    }

    public RequestType getType(){
        return type;
    }
}
