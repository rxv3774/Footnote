package view;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import control.JsonParse;
import control.VerifyUsers;
import model.ServerRequest;

import java.io.*;
import java.util.HashMap;

public class SendVerification implements HttpHandler {

    private JsonParse parse;
    private VerifyUsers users;

    public SendVerification(JsonParse parse, VerifyUsers users){
        this.parse = parse;
        this.users = users;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        InputStream is = httpExchange.getRequestBody();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String response = "";

        String query = br.readLine();

        ServerRequest r = new ServerRequest(query, ServerRequest.RequestType.Register,parse);
        HashMap<String,String> parameters = r.parse();

        String username = parameters.get("username");
        String code = parameters.get("code").trim();

        String realCode = users.getCode(username);

        System.out.println(code +  " : " + realCode);
        if (!realCode.equals(null)) {
            if (users.getCode(username).equals(code)) {
                System.out.println("Valid verification for " + username);
                users.updateUser(username);
                response = "valid";
            }else{
                System.out.println("Invalid Verification Code for: " + username);
                response = "Invalid Verification Code";
            }
        }else{
            System.out.print("Code does not exist in database");
        }

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
