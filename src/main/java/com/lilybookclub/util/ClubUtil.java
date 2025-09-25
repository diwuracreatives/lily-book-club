package com.lilybookclub.util;

import com.lilybookclub.enums.DayOfTheWeek;

import java.util.Random;

public class ClubUtil {

    private static final Random RANDOM = new Random();

    public static DayOfTheWeek generateReadingDay() {
        DayOfTheWeek[] days = DayOfTheWeek.values();
        return days[RANDOM.nextInt(days.length)];
    }
}

