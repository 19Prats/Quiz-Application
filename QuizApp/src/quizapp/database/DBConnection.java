package quizapp.database;

import java.sql.*;

public class DBConnection {
    private static final String url = "jdbc:mysql://localhost:3306/quiz_db";
    private static final String user = "root";
    private static final String password = "Pratik";
    public static Connection connection = getConnection();
    public static Connection getConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Loaded jdbc Driver");
            connection = DriverManager.getConnection(url,user,password);
            if(connection != null){
                System.out.println("CONNECTED");
                return connection;
            }else{
                System.out.println("CANNOT CONNECT");
                return null;
            }          
        } catch (Exception e) {
            System.out.println("EXCEPTION IN DATABASE CONNECTION");
            System.out.println(e);
            return null;
        }
    }
    public static void main(String[] args) {
        connection = getConnection();
        if(connection != null)
            System.out.println("CONNECTION SUCESSFULL!!");
        else
            System.out.println("CONNECTION UNSUCESSFULL!!");
    }
}

