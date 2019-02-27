package view;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import control.Comments;
import control.JsonParse;
import model.ServerRequest;

import java.io.*;
import java.util.HashMap;

public class SubmitComment implements HttpHandler {

    private Comments comments;
    private JsonParse parse;

    public SubmitComment(Comments comments, JsonParse parse){
        this.comments = comments;
        this.parse = parse;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        InputStream is = httpExchange.getRequestBody();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String response = "inserted";

        String query = br.readLine();

        System.out.println("Q:"+query);

        ServerRequest r = new ServerRequest(query, ServerRequest.RequestType.Register,parse);
        HashMap<String,String> parameters = r.parse();

        System.out.println("Hey" + parameters);

        String link = parameters.get("link");
        String username = parameters.get("username");
        String code = parameters.get("data");

        System.out.println(link);
        System.out.println(username);
        System.out.println(code);

        comments.insertQuery(link,username,code);

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }
}
