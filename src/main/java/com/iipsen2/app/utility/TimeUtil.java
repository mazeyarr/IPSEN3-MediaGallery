package com.iipsen2.app.utility;

public class TimeUtil {
    public static final int MINUTE_IN_MILLIS = 60000;
    public static final int HOUR_IN_MILLIS = 3600000;
    public static final int SECOND_IN_MILLIS = 1000;

    public static long convertMinutesToMillis(int minutes) {
        return (long) (minutes * MINUTE_IN_MILLIS);
    }

    public static long convertHoursToMillis(int hours) {
        return (long) (hours * HOUR_IN_MILLIS);
    }

    public static long convertSecondsToMillis(int seconds) {
        return (long) (seconds * SECOND_IN_MILLIS);
    }
}
