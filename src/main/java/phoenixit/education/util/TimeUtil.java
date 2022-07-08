package phoenixit.education.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TimeUtil {

    private static final DateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    public static Date DateFromStr(String dateStr) throws ParseException {
        return isoFormat.parse(dateStr);
    }

}
