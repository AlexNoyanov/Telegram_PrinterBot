package com.alexn;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import sun.awt.www.content.image.png;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

// For loading images:
import java.awt.Image;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GuardedObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TBot extends TelegramLongPollingBot {

    // For menu:    =============================
    private MenuManager menuManager = new MenuManager();

    public TBot() {

    }

    public void init() {                    // Adding all buttons to the manager:
        menuManager.setColumnsCount(2);

        // Menu design: 6 buttons on the page
        menuManager.addMenuItem("Turn ON power \uD83D\uDD0C ", "pwrON");

        menuManager.addMenuItem("Turn OFF power", "pwrOFF");
        menuManager.addMenuItem("Settings ⚙️", "settings");

        menuManager.addMenuItem("LED ON \uD83D\uDCA1 ", "ledON");
        menuManager.addMenuItem("LED OFF", "ledOFF");

        menuManager.addMenuItem("PHOTO️ \uD83D\uDCF7 ", "photo");
        //menuManager.addMenuItem("FULL STOP ⛔️", "fullStop");
        // Some emoji for buttons:
        /*
        Settings ⚙️ 🛠
        Power plug 🔌
        Light bulb  💡
        Stop        ⛔️
        Camera  📷  📸  📹 🎥 📽
        Show timelapse  🎞 🎬
        Random 🎲
         */
//        menuManager.addMenuItem("Action 1", "action 1");
//        menuManager.addMenuItem("Action 2", "action 2");
//        menuManager.addMenuItem("Action 3", "action 3");
//        menuManager.addMenuItem("Action 4", "action 4");
//        menuManager.addMenuItem("Action 5", "action 5");
//        menuManager.addMenuItem("Action 6", "action 6");
//        menuManager.addMenuItem("Action 7", "action 7");
//        menuManager.addMenuItem("Action 8", "action 8");
//        menuManager.addMenuItem("Action 9", "action 9");
//        menuManager.addMenuItem("Action 10", "action 10");
//        menuManager.addMenuItem("Action 11", "action 11");
//        menuManager.addMenuItem("Action 12", "action 12");
//        menuManager.addMenuItem("Action 13", "action 13");
//        menuManager.addMenuItem("Action 14", "action 14");
//        menuManager.addMenuItem("Action 15", "action 15");
//        menuManager.addMenuItem("Action 16", "action 16");
//        menuManager.addMenuItem("Action 17", "action 17");
//        menuManager.addMenuItem("Action 18", "action 18");
//        menuManager.addMenuItem("Action 19", "action 19");
//        menuManager.addMenuItem("Action 20", "action 20");

        menuManager.init();
    }

    private void replaceMessageWithText(long chatId, long messageId, String text) {
        EditMessageText newMessage = new EditMessageText()
                .setChatId(chatId)
                .setMessageId(Math.toIntExact(messageId))
                .setText(text);
        try {
            execute(newMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void replaceMessage(long chatId, long messageId, SendMessage message) {
        EditMessageText newMessage = new EditMessageText()
                .setChatId(chatId)
                .setMessageId(Math.toIntExact(messageId))
                .setText(message.getText())
                .setReplyMarkup((InlineKeyboardMarkup) message.getReplyMarkup());
        try {
            execute(newMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendPasswordMessage(Update upd) {      // To ask user for password send him the message:
        // Send the message to user:
        SendMessage messageText = new SendMessage() // Create a SendMessage object with mandatory fields
                .setChatId(upd.getMessage().getChatId())
                .setText("Please input the password:");
        try {
            execute(messageText); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendWrongPasswordMessage(Update upd) {      // To ask user for password send him the message:
        // Send the message to user:
        SendMessage messageText = new SendMessage() // Create a SendMessage object with mandatory fields
                .setChatId(upd.getMessage().getChatId())
                .setText("Your password is wrong :(");
        try {
            execute(messageText); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // ================

    // Message Security password checker:
    SecurityChecker myPassChecker = new SecurityChecker();

    @Override
    public String getBotUsername() {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get("/Users/anoyanov/Work/TBot/src/main/java/com/alexn/botInfo.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String botName = lines.get(0);
        return botName;
        // Return bot name
    }



    @Override
    public String getBotToken() {       // Get BOT TOKEN from the text file (Second in the file)
        //String textToken = "";
        String token ="";
        try {
            List<String> lines = Files.readAllLines(Paths.get("/Users/anoyanov/Work/TBot/src/main/java/com/alexn/botInfo.txt"));
//            for(String str : lines){
//                System.out.println(str);
//            }
            //System.out.println(lines.get(1));
            token = lines.get(1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return token;
    }

    boolean isGettingPassword = false;
    String passwrdType = "";

    // To send request to MySQL database use:
    public void sendMessageToDatabase(LocalDateTime  date, String userMessage,long chatID) {
        try {
            Class.forName("com.mysql.jdbc.Driver");     // To set up timezone: SET GLOBAL time_zone = '+3:00';
            List<String> lines = Files.readAllLines(Paths.get("/Users/anoyanov/Work/TBot/src/main/java/com/alexn/botInfo.txt"));
            String sqlPassword = lines.get(2);          // MySQL password (Third one in the text file)
            Connection con = DriverManager.getConnection(
                   
                    "jdbc:mysql://localhost/TelegramBot", "root", sqlPassword);
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

    public void sendToDatabaseMessage(LocalDateTime  date,long chatID, String FullName, String Fname, String Lname,String message){    // Send message to the database to the 'messages' table
        // MySQL request:
        // INSERT INTO messages(Date,ChatID,UserID,FirstName,LastName,Message) values('March 16 2020',199325184,1234,'Alexander','Noyanov', 'Test message inserted in MySQL database from terminal');
        try {
            List<String> lines = Files.readAllLines(Paths.get("/Users/anoyanov/Work/TBot/src/main/java/com/alexn/botInfo.txt"));
            String sqlPassword = lines.get(2);          // MySQL password (Third one in the text file)
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost/TelegramBot", "root", sqlPassword);
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


    // To take photo from URL:
    public void sendImageFromUrl(String url, Long chatId) {
        SendPhoto sendPhotoRequest = new SendPhoto();
        sendPhotoRequest.setChatId(chatId);
        sendPhotoRequest.setPhoto("http://samp-stats.ru/web/userbar-15377.png");
        try {
            execute(sendPhotoRequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void sendPhotoFromUrl(String url, long chatId){
        SendPhoto messagePhoto;
        File file = new File("/Users/anoyanov/Work/TelegramBot-Git/TBot/src/main/java/com/alexn/picture.jpg");
        FileInputStream fStream = null;
        try {
            fStream = new FileInputStream(file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        messagePhoto = new SendPhoto().setPhoto("SomeText", fStream);
        messagePhoto.setChatId(chatId);
        try {
            this.execute(messagePhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onUpdateReceived(Update update) {               // When user's message is received

        // Current date:
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime currentDateTime =  LocalDateTime.now();

        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {

            // Printing user's input to the text file:
            try {
                FileWriter writer = new FileWriter("/Users/anoyanov/Work/TBot/src/main/java/com/alexn/usersMessages.txt", true);        // Log file path
                writer.write("Chat ID:");
                long chatID = update.getMessage().getChatId();

                // values for newer table:
                long userID = update.getMessage().getFrom().getId();
                String userFname = update.getMessage().getFrom().getFirstName();
                String userLname = update.getMessage().getFrom().getLastName();
                String userFullname = update.getMessage().getFrom().getUserName();

                String usrMessage = update.getMessage().getText();
                writer.write(String.valueOf(chatID));
                writer.write("            Date:");
                writer.write(String.valueOf(currentDateTime));
                writer.write("                  Message:");
                writer.write(usrMessage);
                writer.write("\r\n");   // write new line
                //writer.write("Good Bye!");
                writer.close();

                // Add user message to the MySQL database:
                //sendMessageToDatabase(currentDateTime,usrMessage,chatID);                     // Old table
                sendToDatabaseMessage(currentDateTime, chatID, userFullname, userFname,userLname,usrMessage);

            } catch (IOException e) {
                e.printStackTrace();
            }

            // If message is unknown the reply message will be:
            String replyText = "I don't know this command.\nTry one of this commands:\n";

            if (isGettingPassword) {                                // If getting password :
                String usrPass = update.getMessage().getText();
                //System.out.print("User input password:");
                System.out.print("ChatID:");
                System.out.print(update.getMessage().getChatId());
                System.out.print("              User's input:");
                System.out.println(usrPass);
                usrPass.toLowerCase();

                myPassChecker.SetUserPassword(usrPass);

                if(myPassChecker.checkUserPassword(passwrdType)){   // If password is OK:

                    // Actions for each type of password:
                    if(Objects.equals(passwrdType,"photo")){        // Photo with password
                        replyText = "https://images.pexels.com/photos/3375903/pexels-photo-3375903.jpeg";
                       // replyText = "http://192.168.1.18/photo.html";
                    }

                    if(Objects.equals(passwrdType,"ledon")) {      // LED ON
                        try {
                            // To turn ON the LED need to go : <serverPath>/cgi-bin/ledON.py
                            // Before sending the picture to the user let's ask him for the password:

                            replyText = "";
                            // Now get user's password:
                            passwrdType = "ledon";
                            isGettingPassword = true;
                            final String USER_AGENT = "Mozilla/5.0";
                            String serverPath = "http://192.168.1.18/cgi-bin/ledON.py";
                            URL pwrOff = new URL(serverPath);
                            HttpURLConnection yc = (HttpURLConnection) pwrOff.openConnection();
                            yc.setRequestMethod("GET");
                            yc.setRequestProperty("User-Agent", USER_AGENT);
                            int responseCode = yc.getResponseCode();
                            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                                BufferedReader in = new BufferedReader(new InputStreamReader(
                                        yc.getInputStream()));
                                String inputLine;
                                StringBuffer response = new StringBuffer();

                                while ((inputLine = in.readLine()) != null) {
                                    response.append(inputLine);
                                }
                                in.close();
                                // print result
                                System.out.println(response.toString());
                            } else {
                                System.out.println("GET request not worked");
                            }
                            replyText = "command executed ok. Turned ON";
                        } catch (Exception e) {
                            replyText = "error:" + e.getLocalizedMessage();
                        }
                    }

                    if(Objects.equals(passwrdType,"ledoff")){
                        try {
                            // To turn OFF the power need to go : <serverPath>/cgi-bin/powerOFF.py
                            final String USER_AGENT = "Mozilla/5.0";
                            String serverPath = "http://192.168.1.18/cgi-bin/ledOFF.py";
                            URL pwrOff = new URL(serverPath);
                            HttpURLConnection yc = (HttpURLConnection) pwrOff.openConnection();
                            yc.setRequestMethod("GET");
                            yc.setRequestProperty("User-Agent", USER_AGENT);
                            int responseCode = yc.getResponseCode();
                            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                                BufferedReader in = new BufferedReader(new InputStreamReader(
                                        yc.getInputStream()));
                                String inputLine;
                                StringBuffer response = new StringBuffer();

                                while ((inputLine = in.readLine()) != null) {
                                    response.append(inputLine);
                                }
                                in.close();

                                // print result
                                System.out.println(response.toString());
                            } else {
                                System.out.println("GET request not worked");
                            }

                            replyText = "command executed ok. Turned OFF";
                        } catch (Exception e) {
                            replyText = "error:" + e.getLocalizedMessage();
                        }
                    }

                    if(Objects.equals(passwrdType,"poweron")){
                        try {
                            // To turn ON the power need to go : <serverPath>/cgi-bin/powerON.py
                            // Before turning on the power first ask the power password:
                            final String USER_AGENT = "Mozilla/5.0";
                            String serverPath = "http://192.168.1.18/cgi-bin/powerON.py";
                            URL pwrOff = new URL(serverPath);
                            HttpURLConnection yc = (HttpURLConnection) pwrOff.openConnection();
                            yc.setRequestMethod("GET");
                            yc.setRequestProperty("User-Agent", USER_AGENT);
                            int responseCode = yc.getResponseCode();
                            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                                BufferedReader in = new BufferedReader(new InputStreamReader(
                                        yc.getInputStream()));
                                String inputLine;
                                StringBuffer response = new StringBuffer();

                                while ((inputLine = in.readLine()) != null) {
                                    response.append(inputLine);
                                }
                                in.close();

                                // print result
                                System.out.println(response.toString());
                            } else {
                                System.out.println("GET request not worked");
                            }
                            replyText = "command executed ok. Printer power turned ON";
                        } catch (Exception e) {
                            replyText = "error:" + e.getLocalizedMessage();
                        }
                    }

                    if(Objects.equals(passwrdType,"poweroff")){
                                            try {
                        // To turn ON the power need to go : <serverPath>/cgi-bin/powerON.py

                        final String USER_AGENT = "Mozilla/5.0";
                        String serverPath = "http://192.168.1.18/cgi-bin/powerOFF.py";
                        URL pwrOff = new URL(serverPath);
                        HttpURLConnection yc = (HttpURLConnection) pwrOff.openConnection();
                        yc.setRequestMethod("GET");
                        yc.setRequestProperty("User-Agent", USER_AGENT);
                        int responseCode = yc.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK) { // success
                            BufferedReader in = new BufferedReader(new InputStreamReader(
                                    yc.getInputStream()));
                            String inputLine;
                            StringBuffer response = new StringBuffer();

                            while ((inputLine = in.readLine()) != null) {
                                response.append(inputLine);
                            }
                            in.close();

                            // print result
                            System.out.println(response.toString());
                        } else {
                            System.out.println("GET request not worked");
                        }
                        replyText = "command executed ok. Printer power turned OFF";
                    } catch (Exception e) {
                        replyText = "error:" + e.getLocalizedMessage();
                      }
                    }

                    SendMessage messageText = new SendMessage() // Create a SendMessage object with mandatory fields
                            .setChatId(update.getMessage().getChatId())
                            .setText(replyText);
                    try {
                        execute(messageText); // Call method to send the message
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }






                }else{
                    sendWrongPasswordMessage(update);
                }
                isGettingPassword = false;    // Exit "getpassword" mode
            } else {
                // List of all available commands:
                List<String> texCommandsList = Stream.of("led on", "led off", "power on", "power off","photo","/menu").collect(Collectors.toList());

                // List of special commands:
                List<String> specialCommands = Stream.of("/markup","getData").collect(Collectors.toList());

                String commandListStr = "";
                for (String s : texCommandsList) {
                    commandListStr = commandListStr + s + "\n";
                }
                replyText += commandListStr;
                // Text commands code: ==========================================================

                // Check if Bot get good text command:
                //if(texCommandsList.contains(update.getMessage().getText())) {

                String usrCommand = update.getMessage().getText().toLowerCase();        // All user's command converting to lowercase

                // To get photo from the camera:
                if ( Objects.equals(usrCommand,"photo") ) {
                    replyText = " ";
//                    try {
//                        // Before sending the picture to the user let's ask him for the password:
//                        // Sending message to the user here:
//                        sendImageFromUrl("http://192.168.1.18/Photos/picture.jpg", update.getMessage().getChatId());
//
//                    } catch (Exception e) {
//
//                    }
                    // URL: "/Users/anoyanov/Work/TelegramBot-Git/TBot/src/main/java/com/alexn/picture.jpg"
                    sendPhotoFromUrl("/Users/anoyanov/Work/TelegramBot-Git/TBot/src/main/java/com/alexn/picture.jpg", update.getMessage().getChatId());
                }

                if (update.getMessage().getText().toLowerCase().equals("led off")) {
                   try {
                       sendPasswordMessage(update);
                       replyText = "";
                       // Now get user's password:
                       passwrdType = "ledoff";
                       isGettingPassword = true;
                   }catch (Exception e){
                       replyText = "error:" + e.getLocalizedMessage();
                   }
                }

                if (update.getMessage().getText().toLowerCase().equals("led on")) {
                    try {
                        // To turn ON the LED need to go : <serverPath>/cgi-bin/ledON.py
                        // Before sending the picture to the user let's ask him for the password:
                        //int numOfTry = 3;
                        sendPasswordMessage(update);
                        replyText = "";
                        // Now get user's password:
                        passwrdType = "ledon";
                        isGettingPassword = true;

                       // replyText = "command executed ok. Turned ON";
                    } catch (Exception e) {
                        replyText = "error:" + e.getLocalizedMessage();
                    }
                }

                if (update.getMessage().getText().toLowerCase().equals("power on")) {
                    sendPasswordMessage(update);
                    replyText = "";
                    // Now get user's password:
                    passwrdType = "poweron";

                    isGettingPassword = true;
                }

                if (update.getMessage().getText().toLowerCase().equals("power off")) {
                    sendPasswordMessage(update);
                    replyText = "";
                    // Now get user's password:
                    passwrdType = "poweroff";
                    isGettingPassword = true;
                }

                // If the command is menu:
                if (update.getMessage().getText().equals("/menu")) {
                    replyText = ""; // Don't need the reply text
                }

                // Send the message to user:
                SendMessage messageText = new SendMessage() // Create a SendMessage object with mandatory fields
                        .setChatId(update.getMessage().getChatId())
                        .setText(replyText);
                try {
                    execute(messageText); // Call method to send the message
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

                // Testing new Keyboard buttons instead of message with menu:
                if(update.getMessage().getText().equals("/keyboardMenu")){
                    KeyboardButtons myKeyboard = new KeyboardButtons();
                    myKeyboard.SendKeyboard(update);
                }

                // ---- Secret commands here: ----
                // photoupdate - Upload new photo
                if(update.getMessage().getText().equals("photoupdate")){       // Load new image from the URL
                    BufferedImage image =null;
                    replyText = "Uploaded new photo from the URL";
                    try{
                        URL url = new URL("http://192.168.1.18/Photos/cam2-00.jpg");
                                // read the url
                                image = ImageIO.read(url);

                        //for png
                        ImageIO.write(image, "png",new File("/Users/anoyanov/Pictures/PrinterPhotos/picture00.png"));

                        // for jpg
                        ImageIO.write(image, "jpg",new File("/Users/anoyanov/Work/TelegramBot-Git/TBot/src/main/java/com/alexn/picture00.jpg"));

                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }

                if(update.getMessage().getText().equals("photo2")){
                        BufferedImage image = null;
                    URL url = null;
                    try {
                        url = new URL("https://pixlr.com/photo/photo-shop-200108-pw.jpg");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    try {
                        image = ImageIO.read(url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        ImageIO.write(image, "jpg", new File("/Users/anoyanov/Pictures/PrinterPhotos/out.jpg"));
                        ImageIO.write(image, "gif", new File("/Users/anoyanov/Pictures/PrinterPhotos/out.gif"));
                        ImageIO.write(image, "png", new File("/Users/anoyanov/Pictures/PrinterPhotos/out.png"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                        System.out.println("Done");
                }


                // MENU:    ================================================================
                if (update.getMessage().getText().equals("/menu")) {

                    long chatId = update.getMessage().getChatId();
                    replyText = "Menu:";
                    // lets render the menu
                    InlineKeyboardBuilder builder = menuManager.createMenuForPage(0, true);
                    builder.setChatId(chatId).setText("Choose action:");
                    SendMessage message = builder.build();

                    try {
                        // Send the message
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }
            } else if (update.hasCallbackQuery()) {     // If user answered and pressed some button in menu:
                // Set variables
                long chatId = update.getCallbackQuery().getMessage().getChatId();           // ID of user's chat
                String callData = update.getCallbackQuery().getData();                      // Data
                long messageId = update.getCallbackQuery().getMessage().getMessageId();     // ID of the user's message

                // here will be menu buttons callbacks:
                // To turn on the LED illumination of the printer:

            System.out.println(callData);


            if(callData.equals("ledON")){
               // System.out.println("=== Calling LED ON ===");
                sendMessageToDatabase(currentDateTime, "menu: Led ON",chatId);  // Add user's action to database
                //sendPasswordMessage(update);
                isGettingPassword = true;
                passwrdType = "ledon";

            } else if(callData.equals("ledOFF")){
               // System.out.println("=== Calling LED OFF ===");
                sendMessageToDatabase(currentDateTime, "menu: Led OFF",chatId);  // Add user's action to database
                //sendPasswordMessage(update);
                isGettingPassword = true;
                passwrdType = "ledoff";
            } else if(callData.equals("pwrON")){
                sendMessageToDatabase(currentDateTime, "menu: Power ON",chatId);  // Add user's action to database
                //sendPasswordMessage(update);
                isGettingPassword = true;
                passwrdType = "poweron";
            }else if(callData.equals("pwrOFF")) {
                sendMessageToDatabase(currentDateTime, "menu: POWER OFF",chatId);  // Add user's action to database
                //sendPasswordMessage(update);
                isGettingPassword = true;
                passwrdType = "poweroff";
            }else if(callData.equals("photo")){
                sendMessageToDatabase(currentDateTime, "menu: Photo",chatId);  // Add user's action to database
                // https://wtmqerubko.localtunnel.me//img/promocao/20180212-20180217/10.jpg
                // http://192.168.1.18/picture.jpg
                SendPhoto messagePhoto;
                File file = new File("/Users/anoyanov/Work/TelegramBot-Git/TBot/src/main/java/com/alexn/picture00.jpg");
                FileInputStream fStream = null;
                try {
                    fStream = new FileInputStream(file);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                messagePhoto = new SendPhoto().setPhoto("SomeText", fStream);
                messagePhoto.setChatId(chatId);
                try {
                    this.execute(messagePhoto);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

                if (callData.equals(MenuManager.CANCEL_ACTION)) {                                  // If cancel button pressed by user
                    sendMessageToDatabase(currentDateTime, "menu: Cancelled",chatId);  // Add user's action to database
                    replaceMessageWithText(chatId, messageId, "Cancelled.");                  // Send him the message with text

                } else if (callData.startsWith(MenuManager.PREV_ACTION) || callData.startsWith(MenuManager.NEXT_ACTION)) {      // If next or prev button pressed

                    String pageNum = "0";

                    if (callData.startsWith(MenuManager.PREV_ACTION)) {
                        pageNum = callData.replace(MenuManager.PREV_ACTION + ":", "");
                    } else {
                        pageNum = callData.replace(MenuManager.NEXT_ACTION + ":", "");
                    }
                    InlineKeyboardBuilder builder = menuManager.createMenuForPage(Integer.parseInt(pageNum), true);
                    builder.setChatId(chatId).setText("Choose action:");
                    SendMessage message = builder.build();

                    replaceMessage(chatId, messageId, message);
                }
            }
    }

}
