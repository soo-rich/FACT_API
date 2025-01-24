package com.soosmart.facts.utils;

import org.springframework.stereotype.Service;

@Service
public class NumberToWords {

    private static final String[] units = {"", "un", "deux", "trois", "quatre", "cinq", "six", "sept", "huit", "neuf"};
    private static final String[] teens = {"dix", "onze", "douze", "treize", "quatorze", "quinze", "seize", "dix-sept", "dix-huit", "dix-neuf"};
    private static final String[] tens = {"", "dix", "vingt", "trente", "quarante", "cinquante", "soixante", "soixante-dix", "quatre-vingt", "quatre-vingt-dix"};

    public String convertNumberToWords(Object number) {
        if (number instanceof Integer) {
            return convertIntegerToWords((Integer) number);
        } else if (number instanceof Float) {
            return convertFloatToWords((Float) number);
        } else {
            throw new IllegalArgumentException("Le paramètre doit être un Integer ou un Float");
        }
    }

    private  String convertIntegerToWords(int number) {
        if (number == 0) {
            return "zéro";
        }
        return convert(number);
    }

    private  String convertFloatToWords(float number) {
        int integerPart = (int) number;
        int decimalPart = (int) ((number - integerPart) * 100);
        String words = convertIntegerToWords(integerPart);
        if (decimalPart > 0) {
            words += " virgule " + convertIntegerToWords(decimalPart);
        }
        return words;
    }

    private static String convert(int number) {
        if (number < 10) {
            return units[number];
        } else if (number < 20) {
            return teens[number - 10];
        } else if (number < 100) {
            return tens[number / 10] + ((number % 10 != 0) ? "-" + units[number % 10] : "");
        } else if (number < 1000) {
            return units[number / 100] + " cent" + ((number % 100 != 0) ? " " + convert(number % 100) : "");
        } else if (number < 1000000) {
            return convert(number / 1000) + " mille" + ((number % 1000 != 0) ? " " + convert(number % 1000) : "");
        } else {
            return convert(number / 1000000) + " million" + ((number % 1000000 != 0) ? " " + convert(number % 1000000) : "");
        }
    }
}
