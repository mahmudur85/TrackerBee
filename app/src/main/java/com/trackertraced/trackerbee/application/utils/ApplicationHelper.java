package com.trackertraced.trackerbee.application.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Mahmudur on 11-Mar-15.
 *
 * see http://www.coderanch.com/t/416639/java/java/Convert-Local-time-UTC-vice
 */
public final class ApplicationHelper {
    static LogHelper logHelper = new LogHelper(LogHelper.LogTags.KMR, ApplicationHelper.class.getSimpleName(), true);

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
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        String currentDateTime = sdf.format(new Date());
        logHelper.d("getCurrentTime: " + currentDateTime);
        return currentDateTime;
    }
}
