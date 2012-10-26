/*
 * This file is part of SimpleClans2 (2012).
 *
 *     SimpleClans2 is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     SimpleClans2 is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with SimpleClans2.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     Last modified: 10.10.12 21:57
 */


package com.p000ison.dev.simpleclans2.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * Represents a DateHelper
 */
public final class DateHelper {
    public static final long YEAR = 31556926000L;
    public static final long DAY = 86400000L;
    public static final long HOUR = 3600000L;
    public static final long MINUTE = 60000L;
    public static final long SECOND = 1000L;

    private DateHelper()
    {
    }

    public static double differenceInMonths(Date date1, Date date2)
    {
        return differenceInYears(date1, date2) * 12D;
    }

    public static double differenceInYears(Date date1, Date date2)
    {
        double days = differenceInDays(date1, date2);
        return days / 365.2425D;
    }

    public static double differenceInDays(Date date1, Date date2)
    {
        return differenceInHours(date1, date2) / 24.0D;
    }


    public static double differenceInHours(Date date1, Date date2)
    {
        return differenceInMinutes(date1, date2) / 60.0D;
    }


    public static double differenceInMinutes(Date date1, Date date2)
    {
        return differenceInSeconds(date1, date2) / 60.0D;
    }

    public static double differenceInSeconds(Date date1, Date date2)
    {
        return differenceInMilliseconds(date1, date2) / 1000.0D;
    }

    private static double differenceInMilliseconds(Date date1, Date date2)
    {
        return differenceInMilliseconds(date1.getTime(), date2.getTime());
    }

    public static double differenceInMonths(long date1, long date2)
    {
        return differenceInYears(date1, date2) * 12D;
    }

    public static double differenceInYears(long date1, long date2)
    {
        double days = differenceInDays(date1, date2);
        return days / 365.2425D;
    }

    public static double differenceInDays(long date1, long date2)
    {
        return differenceInHours(date1, date2) / 24.0D;
    }

    public static double differenceInHours(long date1, long date2)
    {
        return differenceInMinutes(date1, date2) / 60.0D;
    }

    public static double differenceInMinutes(long date1, long date2)
    {
        return differenceInSeconds(date1, date2) / 60.0D;
    }

    public static double differenceInSeconds(long date1, long date2)
    {
        return differenceInMilliseconds(date1, date2) / 1000.0D;
    }

    public static long differenceInMilliseconds(long date1, long date2)
    {
        return Math.abs(date1 - date2);
    }

    private static final char YEAR_CHAR = 'y', DAY_CHAR = 'd', HOUR_CHAR = 'h', MINUTE_CHAR = 'm', SECOND_CHAR = 's';

    /**
     * This parses a String which holds a time and returns the duration in ms.
     * <p/>
     * <p><strong>Example:</strong> 2d5h2m52s</p>
     *
     * @param time The string to parse
     * @return The duration in ms
     */
    public static long parseTime(String time)
    {
        char[] chars = time.toCharArray();

        long duration = 0;

        long current = 0;
        int run = 1;
        long multi = 1;

        //iterate backwards
        for (int i = chars.length - 1; i >= 0; i--) {
            char character = chars[i];
            int currentNumber = getDigit(character);

            if (currentNumber != -1) {
                //is number
                //set the value of the current iteration
                current += currentNumber * run;
                //add the current value to the whole
                duration += current * multi;
                //we set our run '* 10' because: 112 = 1 * 100 + 1 * 10 + 2 * 1
                run *= 10;
            } else {
                //is character
                //lets find our multiplicator
                switch (character) {
                    case YEAR_CHAR:
                        multi = YEAR;
                        break;
                    case DAY_CHAR:
                        multi = DAY;
                        break;
                    case HOUR_CHAR:
                        multi = HOUR;
                        break;
                    case MINUTE_CHAR:
                        multi = MINUTE;
                        break;
                    case SECOND_CHAR:
                        multi = SECOND;
                        break;
                    case ' ':
                    case '_':
                        continue;
                    default:
                        throw new IllegalArgumentException(String.format("Invalid character found! %s", character));
                }
                //everytime we get a new character which signs the multiplicator we reset our values
                current = 0;
                run = 1;
            }
        }

        return duration;
    }

    /**
     * Returns the number of a character. '9' will return the integer 9, '2' will return the integer 2
     *
     * @param character The character to parse
     * @return The integer which matches, or -1 if the parse fails
     */
    public static int getDigit(int character)
    {
        final int MINIMAL_DIGIT = 48, MAXIMAL_DIGIT = 57;

        if (character >= MINIMAL_DIGIT && character <= MAXIMAL_DIGIT) {
            return character - MINIMAL_DIGIT;
        }

        return -1;
    }

//    public static void main(String[] args) {
//
//        long start = System.currentTimeMillis();
//
//        for (int i = 0; i < 10000000; i++) {
//              parseTime("5d2h20m2s");
//        }
//
//        long finish = System.currentTimeMillis();
//        System.out.printf("Check took %s!", finish - start);
//
//         start = System.currentTimeMillis();
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("d'd'k'h'm'm's's'");
//        for (int i = 0; i < 10000000; i++) {
//            Date date = null;
//            try {
//                date = dateFormat.parse("5d2h20m2s");
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            date.getTime();
//        }
//         finish = System.currentTimeMillis();
//        System.out.printf("Check 2 took %s!", finish - start);
//    }


    public static void main(String[] args)
    {
        String input;
        System.out.print("Wann? ");

        try {
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            input = bufferRead.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        System.out.println(new Date(System.currentTimeMillis() + parseTime(input)));
    }
}
