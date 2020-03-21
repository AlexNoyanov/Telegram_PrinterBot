package com.alexn;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

// This class is requred for security reasons
// It must protect the chat functions

// Created by Alexander Noyanov
// Monday March 9 2020

public class SecurityChecker {

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // All passwords are the strings
    // To check if password is right the method requered
    // But copy-pasting the same function for each field is stupid
    // So, the passwords must be pair <Key> <Value>
    // This is Map: <name of the password> <password value>
    // To check the password method need two strings: User's password and password type
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // This is not the best way to save the password:
    // The passwords for functions:
//    String powerPas = "7425";        // The password for power ON and OFF
//    String ledPass = "1830";         // LED password

    // Saving all passwords using Map:
    Map<String,String> passwords = new HashMap<String,String>();
    String userPassword;
    boolean isCorrect;

    public SecurityChecker(){               // Constructor for initializing passwords Map
        passwords.put("poweron", "1554");     // Putting all passwords to the Map
        passwords.put("poweroff", "1554");
        passwords.put("ledoff", "4321");
        passwords.put("ledon","4321");
        passwords.put("photo", "4321");
        userPassword = "";
        isCorrect = false;
    }

    public void SetUserPassword(String inputedUserPassword){    // To set the user password
        userPassword = inputedUserPassword;
    }

    // Now to check if password of this type is right we use only one simple method:
    public boolean checkPasswordInput(String passType, String userPass){
        String pass = (String) passwords.get(passType);             // Finding the password for this type
        return Objects.equals(userPass,pass);
//        if(userPass == pass){         NOT CORRECT!
//            return true;
//        }else{
//            return false;
//        }
    }

    // This method can find if the user password from class is equal to the password
    public boolean checkUserPassword(String passType) {
        String passForType = (String) passwords.get(passType);             // Finding the password for this type
        return Objects.equals(userPassword,passForType);                    // Checking user's password
      // return isCorrect;
    }

    // To reset password asking:
    public void resetChecker(){
        isCorrect = false;
    }

//    // To ask user for password:
//    public void askForPassword(Update update){
//        // Send the message to user:
//        SendMessage messageText = new SendMessage() // Create a SendMessage object with mandatory fields
//                .setChatId(update.getMessage().getChatId())
//                .setText("Please inout password:");
//        try {
//            execute(messageText); // Call method to send the message
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }


//    public boolean isPowerrPass(String userPass ) {   // To check the password:
//        if (userPass == powerPas) {
//              return true;
//            } else {
//              return false;
//            }
//        }
//
//    public boolean isLedPass(String userPass ) {   // To check the password:
//        if (userPass == ledPass) {
//            return true;
//        } else {
//            return false;
//        }
//    }


    }

