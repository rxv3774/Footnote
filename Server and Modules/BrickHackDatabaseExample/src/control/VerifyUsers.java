package control;

import model.EmailSend;
import model.TimeoutVerification;

import java.sql.*;
import java.util.HashMap;
import java.util.Random;

public class VerifyUsers {

    //when generate new code, add to hashmap with user.

    //<code,useremail>
    HashMap<String, String> useremailAndCode = new HashMap<>();

    public void newUser(String email, String username, String hash) {


       //check to see if the data exists in the database
        // create a new entry in the database.
        //create new TimeoutVerification
        ResultSet r = null;
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:src/" + "users.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

            String sql = "SELECT username FROM Verified WHERE username=" + "'" + username + "'";
            PreparedStatement ps = null;

            try {
                ps = conn.prepareStatement(sql);
                r = ps.executeQuery();

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
            if (r.next() == false){
                try {
                    // db parameters
                    String url = "jdbc:sqlite:src/" + "users.db";
                    // create a connection to the database
                    conn = DriverManager.getConnection(url);

                    System.out.println("Connection to SQLite has been established.");

                    String sql = "INSERT INTO Verified(useremail,username,hash,verified) VALUES('" + email + "','" + username  + "','" + hash + "','" + "0')";
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

                //Create TimeoutVerification
                TimeoutVerification tv = new TimeoutVerification(username,useremailAndCode);
                new Thread(tv).start();
                String code = generateNewCode();
                useremailAndCode.put(username,code);
                EmailSend es = new EmailSend();
                es.run(email,code);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isVerified(String email, String code){
        if (useremailAndCode.get(email).equals(code)){
            return true;
        }
        return false;
    }

    public void removeAllUnverified(){
        for (String key : useremailAndCode.keySet()){
            useremailAndCode.remove(key);
        }
    }

    public String getCode(String email){
        return useremailAndCode.get(email);
    }

    public void updateUser(String username){
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:src/" + "users.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

            String sql = "UPDATE Verified SET verified='1' WHERE username='" + username + "'";
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


    protected String generateNewCode() {
        String availableChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder string = new StringBuilder();
        Random random = new Random();

        while (string.length() < 16) {
            int index = (int)(random.nextFloat() * availableChars.length());
            string.append(availableChars.charAt(index));
        }
        return string.toString();
    }
}
