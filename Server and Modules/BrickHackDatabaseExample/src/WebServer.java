import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.sun.net.httpserver.HttpServer;
import control.Comments;
import control.JsonParse;
import control.VerifyUsers;
import org.json.simple.JSONObject;
import view.*;


public class WebServer {

    private static VerifyUsers users;
    private static JsonParse parse;
    private static Comments comments;
    private static JSONObject jsonMachine; //Send JSON objects back to the server

    public WebServer() throws Exception{
        parse = new JsonParse();
        users = new VerifyUsers();
        comments = new Comments(parse);

        removeUnverified();

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/Register/", new RegistrationRequest(parse,users));
        server.createContext("/Validate/", new SendVerification(parse,users));
        server.createContext("/Login/", new LoginRequest(parse));
        server.createContext("/SubmitComment/", new SubmitComment(comments,parse));
        server.createContext("/GetComment/", new GetComment(comments,parse));
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    public static void main(String[] args) throws Exception {
        new WebServer();
    }

    public void removeUnverified(){

        users.removeAllUnverified();

        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:src/" + "users.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

            //Remove the
            String sql = "DELETE FROM Verified WHERE verified='0'";
            PreparedStatement ps = null;

            try {
                ps = conn.prepareStatement(sql);
                ps.executeUpdate();

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
    }
}
