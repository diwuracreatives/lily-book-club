package com.lilybookclub.util;

import java.time.DayOfWeek;
import java.util.Random;

public class ClubUtil {

    private static  final Random RANDOM = new Random();
    private static int generateFourDigitNumber(){
         return 1000 + RANDOM.nextInt(9000);
    }

    public static String generateClubCode() {
        return "CL" + generateFourDigitNumber();
    }

    public static String getReadingDay(int day) {
        return DayOfWeek.of(day).name();
    }
    public static DayOfWeek generateReadingDay() {
        return DayOfWeek.of(RANDOM.nextInt(7));
    }

}
