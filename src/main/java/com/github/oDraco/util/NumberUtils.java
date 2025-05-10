package com.github.oDraco.util;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// Fork from SrBlecaute
// https://github.com/SrBlecaute01
public class NumberUtils {

    private static final Pattern FORMAT_PATTERN = Pattern.compile("^\\d{1,3}([a-zA-Z]+|\\.\\d{1,2}[\\D]+$)");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("((^\\d{1,3})([.][\\d+]{1,2})?)(\\D+$)?");

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###.##");
    private static final List<String> FORMATS = Arrays.asList("", "K", "M", "B", "T", "Q", "QQ", "S", "SS", "OC", "N", "D", "UN", "DD", "TR", "QT", "QN");

    public static String format(double number) {
        int base = (int) Math.log10(number);
        int index = base / 3;

        if (index < 0) {
            index = 0;
        }

        number = (number / Math.pow(10, index * 3));

        String symbol = index < FORMATS.size() ? FORMATS.get(index) : "";
        return DECIMAL_FORMAT.format(number) + symbol;
    }

    public static boolean isFormatted(String string) {
        return FORMAT_PATTERN.matcher(string).matches();
    }

    public static double unformat(String string) {
        if (!isFormatted(string)) {
            throw new IllegalArgumentException("the value " + string + " is not in a valid format");
        }

        try {
            Matcher matcher = NUMBER_PATTERN.matcher(string);
            if (matcher.find()) {
                String value = matcher.group(1);
                String format = matcher.group(4);

                double number = Double.parseDouble(value);
                int index = FORMATS.contains(format) ? FORMATS.indexOf(format) : 0;

                return number * Math.pow(1000, index);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static String punctuate(Number number) {
        return DECIMAL_FORMAT.format(number);
    }

}