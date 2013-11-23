package jkmau5.alternativeenergy;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Config {

    public static int powerBox_capacity = 100000;
    public static int powerBox_capacity_multiplier = 5000;

    public static int upgrade_ItemId = 5760;

    public static boolean powerBoxExplosionResistant = false;

    public static String convertNumber(int number) {
        DecimalFormat df = new DecimalFormat("#,##0");
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ITALY));
        return df.format(new BigDecimal(number));
    }
}
