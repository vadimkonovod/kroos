package main.commands;

import main.botlib.core.TelegramBotApi;
import main.botlib.core.beans.Message;
import main.botlib.core.exceptions.NegativeResponseException;

import java.io.IOException;

public class StartCommand extends Command {

  public StartCommand(TelegramBotApi telegramBotApi, Message message) {
    super(telegramBotApi, message);
  }

  @Override
  public void execute() throws IOException, NegativeResponseException {
    sendAnswerToChat(telegramBotApi, message.getChat().getId(), getStartMessage());
  }

  private String getStartMessage() {
    return "List of commands:\n" +
        "*/regme* - register for a Sunday game\n" +
        "*/dropoff* - kinut' pacanov\n" +
        "*/status* - get status of the upcoming game\n";
      //"*/map* - get google map link";
  }
}
