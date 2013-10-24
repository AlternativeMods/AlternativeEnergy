package core;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Author: Lordmau5
 * Date: 21.08.13
 * Time: 16:17
 * You are not allowed to change this code,
 * nor publish it without my permission.
 */
public class Config {
    public static double eu = 5;
    public static double mj = 2;

    public static int powerBox_blockId = 2000;
    public static int powerBox_capacity = 100000;
    public static int powerBox_capacity_multiplier = 5000;

    public static int capacityUpgrade_itemId = 5760;
    public static int outputSpeedUpgrade_itemId = 5761;

    public static boolean unbreakable = false;

    /**
     * Converting energy from EU to MJ and vice-versa.
     * The first param, "ratio", is the unit that is being used.
     * E.g.: EU -> MJ ==> Ratios.EU
     *       MJ -> EU ==> Ratios.MJ
     *
     * @param ratio
     * @param amount
     * @return
     */
    public static double convertInput(Ratios ratio, double input) {
        if(ratio == Ratios.EU) return (input / Ratios.EU.conversion);
        else                   return (input / Ratios.MJ.conversion);
    }

    public static String convertNumber(int number) {
        DecimalFormat df = new DecimalFormat("#,##0");
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ITALY));
        return df.format(new BigDecimal(number));
    }
}