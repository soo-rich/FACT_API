package com.soosmart.facts.utils;

import com.ibm.icu.text.RuleBasedNumberFormat;
import com.ibm.icu.util.ULocale;


public class NumberToWords {


    public static String convertNumberToWords(Object number) {
        ULocale locale = new ULocale("fr_FR");
        RuleBasedNumberFormat formatter = new RuleBasedNumberFormat(locale, RuleBasedNumberFormat.SPELLOUT);

        if (number instanceof Integer || number instanceof Float|| number instanceof Double) {
          return formatter.format(number);
        } else {
            throw new IllegalArgumentException("Le paramètre doit être un Integer ou un Float ou un Double");
        }
    }


}
