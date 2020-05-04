package com.alexn;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;


/*

        This class is for communicating with MySQL database
        Created by Alexander Noyanov, April 2020

 */

public class DatabaseWriter {
    public String sqlPassword;

    public DatabaseWriter(){    // Getting database password in this class
        try {
            List<String> lines = Files.readAllLines(Paths.get("/Users/anoyanov/Work/TBot/src/main/java/com/alexn/botInfo.txt"));
            sqlPassword = lines.get(2);          // MySQL password (Third one in the text file)
        }catch(Exception e){
            System.out.println("ERROR! Can't get MySQL password from the file!");
        }
    }

// To send to the new database:
    public void sendToDatabaseMessage(LocalDateTime date, long chatID, String FullName, String Fname, String Lname, String message){    // Send message to the database to the 'messages' table
        // MySQL request Table <messages>:
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

    // Older method for old table


}