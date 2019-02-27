package view;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import control.Comments;
import control.JsonParse;
import model.ServerRequest;

import java.io.*;
import java.util.HashMap;

public class GetComment implements HttpHandler {

    private Comments comments;
    private JsonParse parse;

    public GetComment(Comments comments, JsonParse parse){
        this.comments = comments;
        this.parse = parse;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        InputStream is = httpExchange.getRequestBody();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String response = "inserted";

        String query = br.readLine();

        ServerRequest r = new ServerRequest(query, ServerRequest.RequestType.Register,parse);
        HashMap<String,String> parameters = r.parse();

        String link = parameters.get("link");

        response = comments.getQueryFormat(link);

        if (response == null){
            response="invalid";
        }else{
            System.out.println("Response" + response);
        }

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }
}
