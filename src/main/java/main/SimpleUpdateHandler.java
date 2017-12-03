package main;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import main.bot.core.beans.CallbackQuery;
import main.bot.core.beans.ChosenInlineResult;
import main.bot.core.beans.InlineQuery;
import main.bot.core.beans.Message;
import main.bot.builders.ApiBuilder;
import main.bot.core.TelegramBotApi;
import main.bot.core.UpdateHandler;
import main.bot.core.beans.User;
import main.bot.core.enums.ParseMode;
import main.bot.core.exceptions.NegativeResponseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleUpdateHandler implements UpdateHandler {

  /** TODO
   * - replace saturday/sunday everywhere with 'matchday'
   * */

  public static final String SPREADSHEET_ID = "";
  public static final String GOOGLE_SHEET_LINK = "";

  public static final int DATES_ROW = 12;
  public static final int COUNT_ROW = 13;
  public static final int BYN_PER_MAN_ROW = 13;
  public static final int NAMES_START_ROW = 16;

  public static final String TAB_NAME = "Main Page";
  public static final String NAMES_RANGE = TAB_NAME + "!A" + NAMES_START_ROW + ":A";
  public static final String DATES_RANGE = TAB_NAME + "!" + DATES_ROW + ":" + DATES_ROW;

  public static final String INPUT_OPTION = "RAW";

  private Sheets sheetsService;

  SimpleUpdateHandler() throws IOException {
    this.sheetsService = GoogleSheetsService.getSheetsService();
  }

  @Override
  public void onMessageReceived(TelegramBotApi telegramBotApi, int i, Message message) {
    try {
      String input = message.getText();
      if ("/regme".equals(input) || "/regme@vaupshasova_23_bot".equals(input)) {
        User user = message.getFrom();
        String answer = registerUser(user);
        sendAnswerAsReply(telegramBotApi, message.getChat().getId(), answer, message.getMessageId());
      } else if ("/dropoff".equals(input) || "/dropoff@vaupshasova_23_bot".equals(input)) {
        User user = message.getFrom();
        String answer = unregisterUser(user);
        sendAnswerAsReply(telegramBotApi, message.getChat().getId(), answer, message.getMessageId());
      } else if ("/status".equals(input) || "/status@vaupshasova_23_bot".equals(input)) {
        String status = getStatus();
        sendAnswerToChat(telegramBotApi, message.getChat().getId(), status);
        if (status.contains("Count:* 10")) {
          sendAnswerToChat(telegramBotApi, message.getChat().getId(), "My congratulations, comrades! We are 10 people!");
        }
      } else if ("/start".equals(input) || "/start@vaupshasova_23_bot".equals(input)) {
        sendAnswerToChat(telegramBotApi, message.getChat().getId(), getStartMessage());
      } else if ("/sheet".equals(input) || "/sheet@vaupshasova_23_bot".equals(input)) {
        sendAnswerToChat(telegramBotApi, message.getChat().getId(), GOOGLE_SHEET_LINK);
      }
    } catch(IOException | NegativeResponseException e) {
      e.printStackTrace();
    }
  }

  private String registerUser(User user) throws IOException {
    String firstName = user.getFirstName();
    String lastName = user.getLastName();
    if (firstName == null || lastName == null) {
      return "Specify you name and surname.";
    }
    String fullName = user.getFirstName() + " " + user.getLastName();

    ValueRange responseNames = sheetsService.spreadsheets().values()
        .get(SPREADSHEET_ID, NAMES_RANGE)
        .execute();

    List<List<Object>> names = responseNames.getValues();

    if (names == null || names.size() == 0) {
      return  "Players' names not found in the range " + NAMES_RANGE + ".";
    } 

    int nameIndex = -1;
    for (int i = 0; i < names.size(); i++) {
      List row = names.get(i);
      if (fullName.equals(row.get(0))) {
        nameIndex = i;
        break;
      }
    }

    if (nameIndex == -1) {
      return  "Player with name " + fullName + " not found in the Google Sheet.";
    }

    ValueRange responseSaturdays = sheetsService.spreadsheets().values()
        .get(SPREADSHEET_ID, DATES_RANGE)
        .execute();

    List<List<Object>> saturdays = responseSaturdays.getValues();
    if (saturdays == null || saturdays.size() == 0) {
      return  "Sundays not found in the row " + DATES_ROW + ".";
    }

    String nextSaturday = Util.getNextSunday();
    int nexSaturdayIndex = -1;
    for (int i = 0; i < saturdays.get(0).size(); i++) {
      if (nextSaturday.equals(saturdays.get(0).get(i))) {
        nexSaturdayIndex = i;
        break;
      }
    }

    if (nexSaturdayIndex == -1) {
      return  "Next Sunday (" + nextSaturday + ") not found in the row " + DATES_ROW + ".";
    }

    String saturdayCode = Util.columnIndexToSheetColumnName(++nexSaturdayIndex);

    ValueRange bodyWithYesAnswer = new ValueRange().setValues(Arrays.asList(Arrays.asList("yes")));

    sheetsService.spreadsheets().values()
        .update(SPREADSHEET_ID, TAB_NAME + "!" + saturdayCode + (nameIndex + NAMES_START_ROW), bodyWithYesAnswer)
        .setValueInputOption(INPUT_OPTION)
        .execute();

    return fullName + " successfully registered for a Sunday game.";
  }

  private String unregisterUser(User user) throws IOException {
    String firstName = user.getFirstName();
    String lastName = user.getLastName();
    if (firstName == null || lastName == null) {
      return "Specify you name and surname.";
    }
    String fullName = user.getFirstName() + " " + user.getLastName();

    ValueRange responseNames = sheetsService.spreadsheets().values()
        .get(SPREADSHEET_ID, NAMES_RANGE)
        .execute();

    List<List<Object>> names = responseNames.getValues();

    if (names == null || names.size() == 0) {
      return  "Players' names not found in the range " + NAMES_RANGE + ".";
    }

    int nameIndex = -1;
    for (int i = 0; i < names.size(); i++) {
      List row = names.get(i);
      if (fullName.equals(row.get(0))) {
        nameIndex = i;
        break;
      }
    }

    if (nameIndex == -1) {
      return "Player with name " + fullName + " not found in the Google Sheet.";
    }

    ValueRange responseSaturdays = sheetsService.spreadsheets().values()
        .get(SPREADSHEET_ID, DATES_RANGE)
        .execute();

    List<List<Object>> saturdays = responseSaturdays.getValues();
    if (saturdays == null || saturdays.size() == 0) {
      return  "Sundays not found in the row " + DATES_ROW + ".";
    }

    String nextSaturday = Util.getNextSunday();
    int nexSaturdayIndex = -1;
    for (int i = 0; i < saturdays.get(0).size(); i++) {
      if (nextSaturday.equals(saturdays.get(0).get(i))) {
        nexSaturdayIndex = i;
        break;
      }
    }

    if (nexSaturdayIndex == -1) {
      return  "Next Sunday (" + nextSaturday + ") not found in the row " + DATES_ROW + ".";
    }

    String saturdayCode = Util.columnIndexToSheetColumnName(++nexSaturdayIndex);

    ValueRange bodyWithYesAnswer = new ValueRange().setValues(Arrays.asList(Arrays.asList("no")));

    sheetsService.spreadsheets().values()
        .update(SPREADSHEET_ID, TAB_NAME + "!" + saturdayCode + (nameIndex + NAMES_START_ROW), bodyWithYesAnswer)
        .setValueInputOption(INPUT_OPTION)
        .execute();

    return fullName + " kinul pacanov successfully.";
  }

  private String getStatus() throws IOException {
    ValueRange responseNames = sheetsService.spreadsheets().values()
        .get(SPREADSHEET_ID, NAMES_RANGE)
        .execute();

    List<List<Object>> names = responseNames.getValues();

    if (names == null || names.size() == 0) {
      return  "Players' names not found in the range " + NAMES_RANGE + ".";
    }

    List<String> namesList = new ArrayList<>();
    for (List l : names) {
      namesList.add((String) l.get(0));
    }

    ValueRange responseSaturdays = sheetsService.spreadsheets().values()
        .get(SPREADSHEET_ID, DATES_RANGE)
        .execute();

    List<List<Object>> saturdays = responseSaturdays.getValues();
    if (saturdays == null || saturdays.size() == 0) {
      return  "Sundays not found in the row " + DATES_ROW + ".";
    }

    String nextSaturday = Util.getNextSunday();
    int nexSaturdayIndex = -1;
    for (int i = 0; i < saturdays.get(0).size(); i++) {
      if (nextSaturday.equals(saturdays.get(0).get(i))) {
        nexSaturdayIndex = i;
        break;
      }
    }

    if (nexSaturdayIndex == -1) {
      return  "Next Sunday (" + nextSaturday + ") not found in the row " + DATES_ROW + ".";
    }

    String saturdayCode = Util.columnIndexToSheetColumnName(++nexSaturdayIndex);

    String statisticsRange = TAB_NAME + "!" + saturdayCode + COUNT_ROW + ":" + saturdayCode;

    ValueRange responseStatistics = sheetsService.spreadsheets().values()
        .get(SPREADSHEET_ID, statisticsRange)
        .execute();

    List<List<Object>> statistics = responseStatistics.getValues();

    if (statistics == null || statistics.size() == 0) {
      return  "Attendance column not found in the range " + statisticsRange + ".";
    }

    int count = 0;
    try {
      count = Integer.parseInt((String) statistics.remove(0).get(0));
    } catch (IndexOutOfBoundsException ignore) {
    }

    String bynPerMan = "N/A";
    try {
      bynPerMan = (String) statistics.remove(0).get(0);
    } catch (IndexOutOfBoundsException ignore) {
    }
    statistics.remove(0);

    List<Boolean> attendanceList = new ArrayList<>();
    for (List l : statistics) {
      if (l.size() == 1 && "yes".equalsIgnoreCase((String) l.get(0))) {
          attendanceList.add(true);
        } else {
          attendanceList.add(false);
        }
    }

    StringBuilder attendees = new StringBuilder();
    for (int i = 0; i < attendanceList.size(); i++) {
      if (attendanceList.get(i)) {
        attendees.append(cutName(namesList.get(i))).append(", ");
      }
    }

    return "*Date/Time:* Sunday " + nextSaturday.replace('-', ' ') +  ", 19:30\n" +
           "*Count:* " + count + "\n" +
           "*BYN per Man:* " + bynPerMan + "\n" +
           "*Squad:* " + attendees.toString().replaceAll(", $", "");
  }

  private String cutName(String fullname) {
    String[] parts = fullname.split("\\s+");
    String name = parts[0].substring(0, 1).toUpperCase() + ".";
    String surname = parts[1];
    return name + " " + surname;
  }

  private String getStartMessage() {
    return "List of commands:\n" +
           "*/regme* - register for a Sunday game\n" +
           "*/dropoff* - kinut' pacanov\n" +
           "*/status* - get status of the upcoming game\n";
           //"*/map* - get google map link";
  }

  private void sendAnswerToChat(TelegramBotApi telegramBotApi, long chatId, String answer) throws IOException, NegativeResponseException {
    ApiBuilder.api(telegramBotApi)
        .sendMessage(answer)
        .toChatId(chatId).asSilentMessage()
        .parseMessageAs(ParseMode.MARKDOWN)
        .execute();
  }

  private void sendAnswerAsReply(TelegramBotApi telegramBotApi, long chatId, String answer, int replyToMessageId) throws IOException, NegativeResponseException {
    ApiBuilder.api(telegramBotApi)
        .sendMessage(answer)
        .toChatId(chatId).asSilentMessage()
        .asReplyToMessage(replyToMessageId)
        .parseMessageAs(ParseMode.MARKDOWN)
        .execute();
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
      e.printStackTrace();
  }
}