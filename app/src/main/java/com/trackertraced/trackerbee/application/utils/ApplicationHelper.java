package com.trackertraced.trackerbee.application.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.format.DateFormat;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.TimeZone;

/**
 * Created by Mahmudur on 11-Mar-15.
 *
 * see http://www.coderanch.com/t/416639/java/java/Convert-Local-time-UTC-vice
 */
public final class ApplicationHelper {
    static LogHelper logHelper = new LogHelper(LogHelper.LogTags.KMR, ApplicationHelper.class.getSimpleName(), false);

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Method to Convert Server time to UTC time
     *
     * @param serverTime
     * @return String - Date time in UTC
     */
    public static String convertServerToUTC(String serverTime) throws Exception {
        String dateTimeInUTC = "";//Will hold the final converted date
        Date serverDateTime = null;
        String serverTimeZone = "MST";
        SimpleDateFormat formatter;
        SimpleDateFormat parser;

        //create a new Date object using the timezone of the Server
        parser = new SimpleDateFormat(DATE_TIME_FORMAT);
        parser.setTimeZone(TimeZone.getTimeZone(serverTimeZone));
        serverDateTime = parser.parse(serverTime);

        //Set output format prints "2007/10/25  18:35:07"
        formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
        formatter.setTimeZone(TimeZone.getTimeZone(serverTimeZone));

        logHelper.d("convertServerToUTC: The DateTime in the Server time zone " + formatter.format(serverDateTime));

        //Convert the date from the local timezone to UTC timezone
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        dateTimeInUTC = formatter.format(serverDateTime);
        logHelper.d("convertServerToUTC: The Date Time in the UTC time zone " + dateTimeInUTC);

        return dateTimeInUTC;
    }

    /**
     * Method to Convert UTC time to Server time
     *
     * @param utcTime
     * @return String - Date time in UTC
     */
    public static String convertUTCtoServerTime(String utcTime) throws Exception {

        String dateTimeInServerTimeZone = "";//Will hold the final converted date
        Date UTCDateTime = null;
        String serverTimeZone = "MST";
        SimpleDateFormat formatter;
        SimpleDateFormat parser;

        //create a new Date object using the UTC timezone
        parser = new SimpleDateFormat(DATE_TIME_FORMAT);
        parser.setTimeZone(TimeZone.getTimeZone("UTC"));
        UTCDateTime = parser.parse(utcTime);

        //Set output format - // prints "2007/10/25  18:35:07"
        formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
        parser.setTimeZone(TimeZone.getTimeZone("UTC"));
        logHelper.d("convertUTCtoServerTime: The Date in the UTC time zone(UTC) " + formatter.format(UTCDateTime));

        //Convert the UTC date to Server timezone
        formatter.setTimeZone(TimeZone.getTimeZone(serverTimeZone));
        dateTimeInServerTimeZone = formatter.format(UTCDateTime);
        logHelper.d("convertUTCtoServerTime: The Date in the Server Zone " + dateTimeInServerTimeZone);

        return dateTimeInServerTimeZone;
    }

    /**
     * Method to Convert Local time to UTC time
     *
     * @param localTime
     * @return String - Date time in UTC
     */
    public static String convertLocalTimeToUTCTime(String localTime) throws Exception {
        String dateTimeInUTC = "";//Will hold the final converted date
        Date localDateTime = null;
        TimeZone localTimeZone = TimeZone.getDefault();
        SimpleDateFormat formatter;
        SimpleDateFormat parser;

        //create a new Date object using the timezone of the Server
        parser = new SimpleDateFormat(DATE_TIME_FORMAT);
        parser.setTimeZone(localTimeZone);
        localDateTime = parser.parse(localTime);

        //Set output format prints "2007/10/25  18:35:07"
        formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
        formatter.setTimeZone(localTimeZone);

        logHelper.d("convertLocalTimeToUTCTime: The DateTime in the Local time zone " + formatter.format(localDateTime));

        //Convert the date from the local timezone to UTC timezone
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        dateTimeInUTC = formatter.format(localDateTime);
        logHelper.d("convertLocalTimeToUTCTime: The Date Time in the UTC time zone " + dateTimeInUTC);

        return dateTimeInUTC;
    }

    /**
     * Method to Convert UTC time to Local time
     *
     * @param utcTime
     * @return String - Date time in Local Time
     */
    public static String convertUTCtoLocalTime(String utcTime) throws Exception {

        String dateTimeInLocalTimeZone = "";//Will hold the final converted date
        Date UTCDateTime = null;
        TimeZone localTimeZone = TimeZone.getDefault();
        SimpleDateFormat formatter;
        SimpleDateFormat parser;

        //create a new Date object using the UTC timezone
        parser = new SimpleDateFormat(DATE_TIME_FORMAT);
        parser.setTimeZone(TimeZone.getTimeZone("UTC"));
        UTCDateTime = parser.parse(utcTime);

        //Set output format - // prints "2007/10/25  18:35:07"
        formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
        parser.setTimeZone(TimeZone.getTimeZone("UTC"));
        logHelper.d("convertUTCtoLocalTime: The Date in the UTC time zone(UTC) " + formatter.format(UTCDateTime));

        //Convert the UTC date to Server timezone
        formatter.setTimeZone(localTimeZone);
        dateTimeInLocalTimeZone = formatter.format(UTCDateTime);
        logHelper.d("convertUTCtoLocalTime: The Date in the Local Zone " + dateTimeInLocalTimeZone);

        return dateTimeInLocalTimeZone;
    }

    /**
     * Method to Convert Local time to Server time
     *
     * @param localTime
     * @return String - Date time in Server Time
     */
    public static String convertLocalTimeToServerTime(String localTime) throws Exception {
        String utcTime = convertLocalTimeToUTCTime(localTime);
        String serverTime = convertUTCtoServerTime(utcTime);
        return serverTime;
    }

    /**
     * Method to Convert Server time to Local time
     *
     * @param serverTime
     * @return String - Date time in Server Time
     */
    public static String convertServerTimeToLocalTime(String serverTime) throws Exception {
        String utcTime = convertServerToUTC(serverTime);
        String localTime = convertUTCtoLocalTime(utcTime);
        return localTime;
    }

    /**
     * Method to get Current Date Time
     *
     * @return currentDateTime - String
     */
    public static String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        String currentDateTime = sdf.format(new Date());
        logHelper.d("getCurrentDateTime: " + currentDateTime);
        return currentDateTime;
    }

    /**
     * Method to get Date from DateTime String
     */
    public static String getDateOnly(String dateTime) {
        String date[] = dateTime.split(" ");
        return date[0];
    }

    /**
     * Method to get Yesterday Date
     */
    public static String getYesterdayDateString() {
        Date yesterday = new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24));
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        return getDateOnly(sdf.format(yesterday));
    }

    public static boolean isEmptyOrNull(String value) {
        if ((null == value) || ("" == value) || (value.length() == 0) || value.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Method to Convert Drawable to Bitmap Image
     *
     * @param drawable
     * @return Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable)
            return ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public static boolean isPhoneNumber(String s) {
        if (s.length() > 0 && s.length() <= 15) {
            return true;
        }
        return false;
    }

    /**
     * Method to Convert Bitmap Image to Drawable
     *
     * @param resources
     * @param bitmap
     * @return Drawable
     */
    public static Drawable bitmapToDrawable(Resources resources, Bitmap bitmap) {
        BitmapDrawable bitmapDrawable = new BitmapDrawable(resources, bitmap);
        return bitmapDrawable.getCurrent();
    }

    /**
     * Method to Convert Timestamp to 00HR:00MIN:00SEC Format
     *
     * @param timestamp
     * @return String
     */
    public static String getDurationTimeStampFormat(long timestamp) {
        String timeFormat = "";
        int hr = (int) Math.floor(timestamp / 3600);
        int min = (int) Math.floor((timestamp % 3600) / 60);
        int ss = (int) Math.floor((timestamp % 3600) % 60);
        String preTimeH = "0";
        String preTimeM = "0";
        String preTimeS = "0";
        String hrStr = ":";
        String minStr = ":";
        String ssStr = "";
        if (hr >= 10)
            preTimeH = "";
        if (min >= 10)
            preTimeM = "";
        if (ss >= 10)
            preTimeS = "";
        timeFormat = preTimeM + min + minStr + preTimeS + ss + ssStr;
        if (hr > 0)
            timeFormat = preTimeH + hr + hrStr + preTimeM + min + minStr + preTimeS + ss + ssStr;
        return timeFormat;
    }

    /**
     * Method to Convert Timestamp to DateTime String
     *
     * @param timestamp
     * @return String
     */
    public static String getTimeStampString(long timestamp) {
        Date dateObj = new Date();
        dateObj.setTime(timestamp * 1000);
        return DateFormat.format(DATE_TIME_FORMAT, dateObj).toString();
    }

    /**
     * Method to Find Day Difference with Today
     *
     * @param timestamp
     * @return Integer Day difference
     */
    public static int getDayDifferenceWithToday(long timestamp) {
        logHelper.d("Current => " + System.nanoTime());
        Date fetchedDate = new Date();
        fetchedDate.setTime(timestamp * 1000);
        logHelper.d("Fetched Date => " + fetchedDate);
        Date currentDate = new Date();
        currentDate.setTime(System.currentTimeMillis());
        logHelper.d("Current Date => " + currentDate);
        long diffInMillis = currentDate.getTime() - fetchedDate.getTime();
        logHelper.d("Difference In Milliseconds => " + diffInMillis);
        logHelper.d("Day Difference => " + TimeUnit.MILLISECONDS.toDays(diffInMillis));
        return (int) TimeUnit.MILLISECONDS.toDays(diffInMillis);
    }

    /**
     * Method to Convert Java Timestamp to UNIX Timestamp
     *
     * @param timestamp
     * @return Integer Day difference
     */
    public static long getJavaToUnixTimestamp(long timestamp) {
        return timestamp / 1000L;
    }

    /**
     * Method to Convert DateTime String to UnixTimeStamp
     *
     * @param localTime - DataTime String
     * @return Long
     */
    public static long getDateTimeToUnixTimestamp(String localTime) throws ParseException {
        TimeZone localTimeZone = TimeZone.getDefault();
        SimpleDateFormat parser = new SimpleDateFormat(DATE_TIME_FORMAT);
        parser.setTimeZone(localTimeZone);
        return getJavaToUnixTimestamp(parser.parse(localTime).getTime());
    }
}
