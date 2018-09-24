package main.commands;

import main.botlib.builders.ApiBuilder;
import main.botlib.core.TelegramBotApi;
import main.botlib.core.beans.Message;
import main.botlib.core.enums.ParseMode;
import main.botlib.core.exceptions.NegativeResponseException;

import java.io.IOException;

public abstract class Command {

  static final int DATES_ROW = 12;
  static final int COUNT_ROW = 13;
  public static final int BYN_PER_MAN_ROW = 13;
  static final int NAMES_START_ROW = 16;

  static final String TAB_NAME = "Main Page";
  static final String NAMES_RANGE = TAB_NAME + "!A" + NAMES_START_ROW + ":A";
  static final String DATES_RANGE = TAB_NAME + "!" + DATES_ROW + ":" + DATES_ROW;

  static final String SPREADSHEET_ID = "1I8i4v13wbr8l12Z7bIOhUVsw48vIwHJPwe8bu-KCIeI";
  static final String GOOGLE_SHEET_LINK = "https://goo.gl/Niz9CY";

  static final String INPUT_OPTION = "RAW";

  TelegramBotApi telegramBotApi;
  Message message;

  Command() {}

  Command(TelegramBotApi telegramBotApi, Message message) {
    this.telegramBotApi = telegramBotApi;
    this.message = message;
  }

  public abstract void execute() throws IOException, NegativeResponseException;

  void sendAnswerToChat(TelegramBotApi telegramBotApi, long chatId, String answer) throws IOException, NegativeResponseException {
    ApiBuilder.api(telegramBotApi)
        .sendMessage(answer)
        .toChatId(chatId).asSilentMessage()
        .parseMessageAs(ParseMode.MARKDOWN)
        .execute();
  }

  void sendAnswerAsReply(TelegramBotApi telegramBotApi, long chatId, String answer, int replyToMessageId) throws IOException, NegativeResponseException {
    ApiBuilder.api(telegramBotApi)
        .sendMessage(answer)
        .toChatId(chatId).asSilentMessage()
        .asReplyToMessage(replyToMessageId)
        .parseMessageAs(ParseMode.MARKDOWN)
        .execute();
  }
}
