package main.commands;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import main.Util;
import main.botlib.core.TelegramBotApi;
import main.botlib.core.beans.Message;
import main.botlib.core.beans.User;
import main.botlib.core.exceptions.NegativeResponseException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RegisterUserCommand extends Command {

  private Sheets sheetsService;

  public RegisterUserCommand(TelegramBotApi telegramBotApi, Message message, Sheets sheetsService) {
    super(telegramBotApi, message);
    this.sheetsService = sheetsService;
  }

  @Override
  public void execute() throws IOException, NegativeResponseException {
    User user = message.getFrom();
    String answer = registerUser(user);
    sendAnswerAsReply(telegramBotApi, message.getChat().getId(), answer, message.getMessageId());
  }

  private String registerUser(User user) throws IOException {
    String firstName = user.getFirstName();
    String lastName = user.getLastName();
    if (firstName == null || lastName == null) {
      return "Specify your name and surname.";
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
      if (fullName.equalsIgnoreCase(String.valueOf(row.get(0)))) {
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
}
