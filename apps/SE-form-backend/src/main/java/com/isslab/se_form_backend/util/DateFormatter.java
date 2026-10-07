package com.isslab.se_form_backend.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateFormatter {
    public static Date getDateInYyyyMmDdFormat(Date inputDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDateStr = dateFormat.format(inputDate);
        return dateFormat.parse(formattedDateStr);
    }
}
