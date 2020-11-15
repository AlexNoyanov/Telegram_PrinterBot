package com.alexn;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;


/**
 *      ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *      DatabaseWriter to send all data from Bot to the database
 *
 *      Branch: DatabaseWriter
 *
 *      ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *
 *
 */

public class DatabaseWriter {
    public String sqlPassword;

    String infoPath = "/Users/anoyanov/Work/TBot/src/main/java/com/alexn/botInfo.txt";

    public DatabaseWriter(){    // Getting database password in this class
        try {
            List<String> lines = Files.readAllLines(Paths.get(infoPath));
            sqlPassword = lines.get(2);          // MySQL password (Third one in the text file)
        }catch(Exception e){
            System.out.println("ERROR! Can't get MySQL password from the file!");
        }
    }


    public void sendToDatabaseMessage(LocalDateTime date, long chatID, String FullName, String Fname, String Lname, String message){    // Send message to the database to the 'messages' table
        // MySQL request:
        // INSERT INTO messages(Date,ChatID,UserID,FirstName,LastName,Message) values('March 16 2020',199325184,1234,'Alexander','Noyanov', 'Test message inserted in MySQL database from terminal');
        try {
            List<String> lines = Files.readAllLines(Paths.get(infoPath));
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

    // To create table for user:
    public void createTable(){

    }


    // To send request to MySQL database use:
    public void sendMessageToDatabase(LocalDateTime  date, String userMessage,long chatID) {
        try {
            Class.forName("com.mysql.jdbc.Driver");     // To set up timezone: SET GLOBAL time_zone = '+3:00';
            List<String> lines = Files.readAllLines(Paths.get(infoPath));
            String sqlPassword = lines.get(2);          // MySQL password (Third one in the text file)
            Connection con = DriverManager.getConnection(

                    "jdbc:mysql://localhost/TelegramBot ?useUnicode=true&serverTimezone=UTC", "root", sqlPassword);
            // jdbc:mysql://localhost/db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Moscow
            Statement stmt = con.createStatement();
            String request = "INSERT INTO userMessages(Date,ChatID,Message) values('";
            request = request.concat(String.valueOf(date));
            request = request.concat("', ");
            request = request.concat( String.valueOf(chatID));
            request = request.concat(",'");
            request = request.concat(userMessage);
            request = request.concat("');");

            System.out.println("MySQL REQUEST:");
            System.out.println(request);

            int rs = stmt.executeUpdate(request);

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    // Insert User's data into UserData table:
    public void insertUserData(Update messageUpdate){
        long chatId = messageUpdate.getMessage().getChatId();
        String fullName = messageUpdate.getMessage().getForwardSenderName();
        String userMessage = messageUpdate.getMessage().getText();

        // INSERT INTO UsersData

        System.out.print("--- User ");
        System.out.print(fullName);

        System.out.print(" In chat ");
        System.out.print(chatId);

        System.out.print("                  Send text:  ");
        System.out.print(userMessage);




    }


}
