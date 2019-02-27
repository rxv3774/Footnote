package control;

import java.sql.*;

public class Comments {

    private JsonParse parse;

    public Comments(JsonParse parse){
        this.parse = parse;
    }

    public String getQueryFormat(String link){
        String format = "";

        //connect to database
        //get all entries
        //parse
        //send html

        Connection conn = null;
        ResultSet rs = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:src/" + "users.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");


            Statement stmt = null;
            String query =
                    "select * from Comments WHERE link='"+link+"'";
            try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery(query);
                int counter = 0;
                while (rs.next()) {
                    String username = rs.getString("username");
                    String comment = rs.getString("comment");
                    format += "<div class=\"comment\">" +
                            "<h1 class=\"user\">" + username + "</h1>" +
                            "<br><p class=\"data\">" + comment + "</p>" +
                            "</div>";
                    counter++;
                }
                if (counter==0){
                    return null;
                }
            } catch (SQLException e ) {

            } finally {
                if (stmt != null) { stmt.close(); }
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

        return format;
    }

    public void insertQuery(String link, String username, String comment){
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:src/" + "users.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

            //Remove the
            String sql = "INSERT INTO Comments(link,username,comment) VALUES('"+link+"','"+username+"','"+comment+"')";
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
