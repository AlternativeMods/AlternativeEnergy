package jkmau5.alternativeenergy.power;

/**
 * Author: Lordmau5
 * Date: 21.08.13
 * Time: 16:18
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public enum Ratios {
    EU(5),
    MJ(2);

    public final int conversion;

    private Ratios(int ratio) {
        conversion = ratio;
    }
}
