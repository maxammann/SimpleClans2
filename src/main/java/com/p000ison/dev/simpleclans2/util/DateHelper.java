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
 *     Created: 02.09.12 18:33
 */


package com.p000ison.dev.simpleclans2.util;


import java.util.Date;

/**
 * Represents a DateHelper
 */
public class DateHelper {
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
}
