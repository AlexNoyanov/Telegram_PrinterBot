package com.alexn;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 *
 * PROXY: 64.90.51.244 : 29374
 */

public class Main {

    public static void main(String[] args) {
//        System.getProperties().put( "proxySet", "true" );
//        System.getProperties().put( "socksProxyHost", "176.9.119.170" );
//        System.getProperties().put( "socksProxyPort", "1080" );

        // Set up Http proxy
//        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
//
//        botOptions.setProxyHost("2.181.254.198");
//        botOptions.setProxyPort(8580);
        // Select proxy type: [HTTP|SOCKS4|SOCKS5] (default: NO_PROXY)
//        botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
//        System.getProperties().put( "proxySet", "true" );
//        System.getProperties().put( "socksProxyHost", "36.4.84.183" );
//        System.getProperties().put( "socksProxyPort", "30002" );
       // System.getProperties().put( "proxySet", "false" );                // Disable proxy

        ApiContextInitializer.init(); // Инициализируем апи
        TelegramBotsApi botapi = new TelegramBotsApi();

        TBot myBot = new TBot();
        myBot.init();                   // Initialize the message menu

        try {
            //TBot myBot = new TBot();
            //myBot.init();
            botapi.registerBot(myBot);
            //botapi.registerBot(new TBot());

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
