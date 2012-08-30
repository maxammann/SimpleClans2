/*
 * Copyright (C) 2012 p000ison
 *
 * This work is licensed under the Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License. To view a copy of
 * this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send
 * a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco,
 * California, 94105, USA.
 */

package com.p000ison.dev.simpleclans2.util;


import java.util.Date;

/**
 * Represents a DateHelper
 */
public class DateHelper {


    public static double differenceInMonths(Date date1, Date date2)
    {
        return differenceInYears(date1, date2) * 12;
    }


    public static double differenceInYears(Date date1, Date date2)
    {
        double days = differenceInDays(date1, date2);
        return days / 365.2425;
    }

    public static double differenceInDays(Date date1, Date date2)
    {
        return differenceInHours(date1, date2) / 24.0;
    }


    public static double differenceInHours(Date date1, Date date2)
    {
        return differenceInMinutes(date1, date2) / 60.0;
    }


    public static double differenceInMinutes(Date date1, Date date2)
    {
        return differenceInSeconds(date1, date2) / 60.0;
    }

    public static double differenceInSeconds(Date date1, Date date2)
    {
        return differenceInMilliseconds(date1, date2) / 1000.0;
    }

    private static double differenceInMilliseconds(Date date1, Date date2)
    {
        return differenceInMilliseconds(date1.getTime(), date2.getTime());
    }

    public static double differenceInMonths(long date1, long date2)
    {
        return differenceInMonths(date1, date2);
    }


    public static double differenceInYears(long date1, long date2)
    {
        return differenceInYears(date1, date2);
    }


    public static double differenceInDays(long date1, long date2)
    {
        return differenceInDays(date1, date2);
    }

    public static double differenceInHours(long date1, long date2)
    {
        return differenceInHours(date1, date2);
    }

    public static double differenceInMinutes(long date1, long date2)
    {
        return differenceInMinutes(date1, date2);
    }

    public static double differenceInSeconds(long date1, long date2)
    {
        return differenceInSeconds(date1, date2);
    }

    private static double differenceInMilliseconds(long date1, long date2)
    {
        return Math.abs(date1 - date2);
    }
}
