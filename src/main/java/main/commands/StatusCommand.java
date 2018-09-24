package main.commands;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import main.Util;
import main.botlib.core.TelegramBotApi;
import main.botlib.core.beans.Message;
import main.botlib.core.exceptions.NegativeResponseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StatusCommand extends Command {

  private Sheets sheetsService;

  public StatusCommand(TelegramBotApi telegramBotApi, Message message, Sheets sheetsService) {
    super(telegramBotApi, message);
    this.sheetsService = sheetsService;
  }

  @Override
  public void execute() throws IOException, NegativeResponseException {
    String status = getStatus();
    sendAnswerToChat(telegramBotApi, message.getChat().getId(), status);
    if (status.contains("Count:* 10")) {
      sendAnswerToChat(telegramBotApi, message.getChat().getId(), "My congratulations, comrades! We are 10 people!");
    }
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
}
