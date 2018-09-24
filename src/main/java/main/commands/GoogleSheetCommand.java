package main.commands;

import main.botlib.core.TelegramBotApi;
import main.botlib.core.beans.Message;
import main.botlib.core.exceptions.NegativeResponseException;

import java.io.IOException;

public class GoogleSheetCommand extends Command {

  public GoogleSheetCommand(TelegramBotApi telegramBotApi, Message message) {
    super(telegramBotApi, message);
  }

  @Override
  public void execute() throws IOException, NegativeResponseException {
    sendAnswerToChat(telegramBotApi, message.getChat().getId(), GOOGLE_SHEET_LINK);
  }
}


