package com.omkar.expensetracker.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Component
public class AppUtil {
    public String getMonthYear(){
        YearMonth currentYearMonth = YearMonth.now();
        // Format the year and month
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-yyyy");
        String formattedDate = currentYearMonth.format(formatter).toUpperCase();
        return formattedDate;
    }

    public String getDateMonthYear(){
        LocalDate currentDate = LocalDate.now();
        // Format the date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        String formattedDate = currentDate.format(formatter).toUpperCase();
        return formattedDate;
    }

}
