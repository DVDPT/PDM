package pt.isel.adeetc.meic.pdm.common;

import org.joda.time.Period;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public final class DateHelper
{

    private DateHelper()
    {

    }

    public static Calendar subtract(Date d1, Date d2)
    {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(d1.getTime() - d2.getTime());
        return c1;
    }

    public static String stringifyDifference(Date d1, Date d2)
    {
        Logger.global.log(new LogRecord(Level.ALL,String.format("DATAs: %s - %s", d1.toString(), d2.toString())));
        Period sub = new Period(d2.getTime(),d1.getTime());


        if (sub.getYears() > 0)
            return String.format("%d %d %d", sub.getDays(), sub.getMonths(), sub.getYears());

        if (sub.getMonths() > 0)
            return String.format("over %d month", sub.getMonths());

        if(sub.getDays() > 0)
            return String.format("%d day ago",sub.getDays());

        if(sub.getHours() > 0)
            return  String.format("%dh",sub.getHours());
        if(sub.getMinutes() > 0)
            return String.format("%dm",sub.getMinutes());

        return String.format("%ds",sub.getSeconds());
    }

}
