package com.lilybookclub.util;

import com.lilybookclub.enums.DayOfTheWeek;

import java.time.LocalDateTime;
import java.util.Random;

public class AppUtil {

    private static final Random RANDOM = new Random();

    public static DayOfTheWeek generateReadingDay() {
        DayOfTheWeek[] days = DayOfTheWeek.values();
        return days[RANDOM.nextInt(days.length)];
    }

    public static String getTimeGreeting() {
        int hour = LocalDateTime.now().getHour();
        return hour < 12 ? "Good Morning" : hour < 18 ? "Good Afternoon" : "Good Evening";
    }
}

