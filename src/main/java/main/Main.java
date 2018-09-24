package main;

import main.botlib.core.JTelegramBot;

import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    JTelegramBot bot = new JTelegramBot(
      "WFR Football Bot",
      "",
      new SimpleUpdateHandler());
    bot.start();
  }
}
