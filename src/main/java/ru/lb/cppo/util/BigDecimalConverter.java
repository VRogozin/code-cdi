package ru.lb.cppo.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class BigDecimalConverter {

    public BigDecimalConverter() {
        // Empty converter
    }

    public BigDecimal stringToDecimal(String cleanCost) throws ParseException {
        Locale.setDefault(new Locale("ru", "RU"));
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        df.setParseBigDecimal(true);
        return (BigDecimal) df.parse(cleanCost);
    }

}
