package com.alexn;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

public class DatabaseWriter {
    public String databasePassword;

    public DatabaseWriter(){    // Getting database password in this class

    }


    public void sendToDatabaseMessage(LocalDateTime date, long chatID, String FullName, String Fname, String Lname, String message){    // Send message to the database to the 'messages' table
        // MySQL request:
        // INSERT INTO messages(Date,ChatID,UserID,FirstName,LastName,Message) values('March 16 2020',199325184,1234,'Alexander','Noyanov', 'Test message inserted in MySQL database from terminal');
        try {
            List<String> lines = Files.readAllLines(Paths.get("/Users/anoyanov/Work/TBot/src/main/java/com/alexn/botInfo.txt"));
            String sqlPassword = lines.get(2);          // MySQL password (Third one in the text file)
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost/TelegramBot ?useUnicode=true&serverTimezone=UTC", "root", sqlPassword);
            Statement stmt = con.createStatement();
            String request = "INSERT INTO messages(Date,ChatID,FullName,FirstName,LastName,Message) values('";
            request = request.concat(String.valueOf(date));
            request = request.concat("', ");
            request = request.concat( String.valueOf(chatID));
            request = request.concat(", '");
            request = request.concat( FullName);
            request = request.concat("','");
            request = request.concat(Fname);
            request = request.concat("','");
            request = request.concat(Lname);
            request = request.concat("','");
            request = request.concat(message);
            request = request.concat("');");

            System.out.println("MySQL REQUEST:");
            System.out.println(request);

            int rs = stmt.executeUpdate(request);

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }


}
