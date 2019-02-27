package view;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import control.JsonParse;
import control.VerifyUsers;
import model.ServerRequest;

import java.io.*;
import java.util.HashMap;

public class RegistrationRequest implements HttpHandler {

    private JsonParse parse;
    private VerifyUsers users;

    public RegistrationRequest(JsonParse parse, VerifyUsers users){
        this.parse = parse;
        this.users = users;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        InputStream is = httpExchange.getRequestBody();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String query = br.readLine();

        ServerRequest r = new ServerRequest(query, ServerRequest.RequestType.Register,parse);
        HashMap<String,String> parameters = r.parse();

        String email = parameters.get("email");
        String username = parameters.get("username");
        String hash = parameters.get("hash");

        System.out.println("Zander");

        users.newUser(email,username,hash);

        //send a verification code input div.
        String response = "\t<div class=\"comment\">\n" +
                "\t\t<h2>Comment data from server</h2>\n" +
                "\t\t<p><a href=''>This is the comment</a> body where the user will type their message.</p>\n" +
                "\t</div>";
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
