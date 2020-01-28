package com.iipsen2.app.utility;

public class TimeUtil {
    public static long minutesToMillis(int minutes) {
        return (long) (minutes * 60000);
    }

    public static long hoursToMillis(int hours) {
        return (long) (hours * 3600000);
    }

    public static long secondsToMillis(int seconds) {
        return (long) (seconds * 1000);
    }
}
