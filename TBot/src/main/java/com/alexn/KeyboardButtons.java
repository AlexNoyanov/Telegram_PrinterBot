package com.alexn;

/**
 *      ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *      For creating keyboard with buttons
 *
 *      March 30 2020
 *      By Alexander Noyanov
 *      ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *
 *
 */


import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class KeyboardButtons {


    private static ReplyKeyboardMarkup getSettingsKeyboard(String language) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        //replyKeyboardMarkup.setOneTimeKeyboad(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        //keyboardFirstRow.add(getLanguagesCommand(language));
        //keyboardFirstRow.add(getUnitsCommand(language));
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        //keyboardSecondRow.add(getAlertsCommand(language));
        //keyboardSecondRow.add(getBackCommand(language));
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }


    public static void SendKeyboard(Update update){
//        SendMessage message = new SendMessage();
//        message.enableMarkdown(true);
//        message.setReplyMarkup(getSettingsKeyboard(language));
//        message.setReplyToMessageId(message.getMessageId());
        //message.setChatId(message.getChatId().toString());
        //message.setText(getSettingsMessage(language));
    }

}
