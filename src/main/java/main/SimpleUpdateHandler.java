package main;

import com.google.api.services.sheets.v4.Sheets;
import main.botlib.core.beans.CallbackQuery;
import main.botlib.core.beans.ChosenInlineResult;
import main.botlib.core.beans.InlineQuery;
import main.botlib.core.beans.Message;
import main.botlib.core.TelegramBotApi;
import main.botlib.core.UpdateHandler;
import main.botlib.core.exceptions.NegativeResponseException;
import main.commands.Command;
import main.commands.DropoffUserCommand;
import main.commands.DummyCommand;
import main.commands.GoogleSheetCommand;
import main.commands.RegisterUserCommand;
import main.commands.StartCommand;
import main.commands.StatusCommand;

/*import org.slf4j.Logger;
import org.slf4j.LoggerFactory;*/

import java.io.IOException;

public class SimpleUpdateHandler implements UpdateHandler {

  /** TODO
   * - replace saturday/sunday everywhere with 'matchday'
   * - add logging
   * - ignore case for players' names
   * */

  // private static final Logger LOGGER = LoggerFactory.getLogger(SimpleUpdateHandler.class);

  private Sheets sheetsService;

  SimpleUpdateHandler() throws IOException {
    this.sheetsService = GoogleSheetsService.getSheetsService();
  }

  @Override
  public void onMessageReceived(TelegramBotApi telegramBotApi, int i, Message message) {
    try {
      Command command;
      String input = message.getText();

      switch (input) {
        case "/regme":
        case "/regme@vaupshasova_23_bot":
          command = new RegisterUserCommand(telegramBotApi, message, sheetsService);
          break;
        case "/dropoff":
        case "/dropoff@vaupshasova_23_bot":
          command = new DropoffUserCommand(telegramBotApi, message, sheetsService);
          break;
        case "/status":
        case "/status@vaupshasova_23_bot":
          command = new StatusCommand(telegramBotApi, message, sheetsService);
          break;
        case "/start":
        case "/start@vaupshasova_23_bot":
          command = new StartCommand(telegramBotApi, message);
          break;
        case "/sheet":
        case "/sheet@vaupshasova_23_bot":
          command = new GoogleSheetCommand(telegramBotApi, message);
          break;
        default:
          command = new DummyCommand();
      }

      command.execute();

    } catch (IOException | NegativeResponseException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onEditedMessageReceived(TelegramBotApi telegramBotApi, int i, Message message) {
  }

  @Override
  public void onInlineQueryReceived(TelegramBotApi telegramBotApi, int i, InlineQuery inlineQuery) {
  }

  @Override
  public void onChosenInlineResultReceived(TelegramBotApi telegramBotApi, int i, ChosenInlineResult chosenInlineResult) {
  }

  @Override
  public void onCallbackQueryReceived(TelegramBotApi telegramBotApi, int i, CallbackQuery callbackQuery) {
  }

  @Override
  public void onGetUpdatesFailure(Exception e) {
    e.printStackTrace(System.out);
  }
}