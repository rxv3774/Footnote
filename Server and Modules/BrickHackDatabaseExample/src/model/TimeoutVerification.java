package model;

import java.sql.*;
import java.util.HashMap;

//The purpose of this class is too remove the verification entry in the database
//if the user hasn't registered on the site through the email.
public class TimeoutVerification implements Runnable {

    private final float TIMEOUT = 1000 * 60 * 3;
    private float time;

    private String username;
    private int codeEntered; // this is the code entered by the user.

    HashMap<String,String> useremailAndCode;

    public TimeoutVerification(String username, HashMap<String,String> useremailAndCode) {
        this.username = username;
        this.useremailAndCode = useremailAndCode;
    }

    @Override
    public void run() {
        float startTime = System.currentTimeMillis();
        float endTime = startTime;
        while (endTime - startTime < TIMEOUT) {
            endTime = System.currentTimeMillis();
        }

        if (!isVerified()){
            removeEntry();
            useremailAndCode.remove(username);
            System.out.println(username + " removed from verification process");
        }
    }


    private boolean isVerified() {
        Connection conn = null;
        ResultSet rs = null;
        boolean isVerified = true;
        try {
            // db parameters
            String url = "jdbc:sqlite:src/" + "users.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

            //Remove the
            String sql = "SELECT verified FROM Verified WHERE username='" + username + "' AND verified='1'";
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
            if (!rs.isBeforeFirst()) {
                removeEntry();
                isVerified = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isVerified;
    }


    private void removeEntry(){
        useremailAndCode.remove(username);
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:src/" + "users.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

            //Remove the
            String sql = "DELETE FROM Verified WHERE username='"+username+"'";
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
