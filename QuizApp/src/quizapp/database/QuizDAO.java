package quizapp.database;

import java.sql.*;

public class QuizDAO {
    private static final Connection connection = DBConnection.connection;
    
    public static ResultSet getData(){
        if(connection!=null){
            try {
                String query = "select * from quiz;";
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(query);
                System.out.println("RESULT STORED IN RESULTSET");
                return result;
            } catch (Exception e) {
                System.out.println("EXCEPTION IN getData()");
                e.printStackTrace();
            }
        }
        System.out.println("CONNECTION UNSUCCESSFULL");
        return null;
    }
    
    public static void main(String[] args) {
        ResultSet result = getData();
    }
}


