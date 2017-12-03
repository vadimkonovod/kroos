package main;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Util {

  public static String getNextSaturday() {
    Calendar date = Calendar.getInstance();
    date.add(Calendar.DATE, 1);

    while (date.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
      date.add(Calendar.DATE, 1);
    }

    SimpleDateFormat format = new SimpleDateFormat("MMM-dd");
    return format.format(date.getTime());
  }

  public static String getNextSunday() {
    Calendar date = Calendar.getInstance();
    //date.add(Calendar.DATE, 1);

    while (date.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
      date.add(Calendar.DATE, 1);
    }

    SimpleDateFormat format = new SimpleDateFormat("MMM-dd");
    return format.format(date.getTime());
  }

  public static String columnIndexToSheetColumnName(int n) {
    char[] str = new char[50];  // To store result (Excel column name)
    int i = 0;  // To store current index in str which is result

    while (n>0)
    {
      // Find remainder
      int rem = n%26;

      // If remainder is 0, then a 'Z' must be there in output
      if (rem==0)
      {
        str[i++] = 'Z';
        n = (n/26)-1;
      }
      else // If remainder is non-zero
      {
        str[i++] = (char) ((rem-1) + 'A');
        n = n/26;
      }
    }
    str[i] = '\0';

    return new StringBuilder(new String(str)).reverse().toString().trim();
  }
}
