package com.alexn;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TBot extends TelegramLongPollingBot
{
    public static String BOT_TOKEN = "YOUR_BOT_TOKEN";
    public static String BOT_USERNAME = "BOT_NAME";


    @Override
    public String getBotUsername() {
        return "BOT_NAME";
        //возвращаем юзера
    }

    @Override
    public String getBotToken() {
        return "YOUR_BOT_TOKEN";
        //Токен бота
    }

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                    .setChatId(update.getMessage().getChatId())
                    .setText(update.getMessage().getText());
            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

}
