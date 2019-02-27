package view;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import control.JsonParse;
import model.ServerRequest;

import java.io.*;
import java.sql.*;
import java.util.HashMap;

public class LoginRequest implements HttpHandler {

    private JsonParse parse;

    public LoginRequest(JsonParse parse){
        this.parse = parse;
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
        String hash = parameters.get("hash");

        Connection conn = null;
        ResultSet rs = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:src/" + "users.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

            //Remove the
            String sql = "SELECT username FROM Verified WHERE username='" + username + "' AND hash='" +hash+ "' AND verified='1'";
            PreparedStatement ps = null;

            try {
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                conn.close();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }//END OF CONNECTION


        try {
            if (rs.isBeforeFirst()){
                response = "valid";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }
}
