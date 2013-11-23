package jkmau5.alternativeenergy.power;

import jkmau5.alternativeenergy.Config;

/**
 * Author: Lordmau5
 * Date: 21.08.13
 * Time: 16:18
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public enum Ratios {
    EU(Config.eu),
    MJ(Config.mj);

    public final int conversion;

    private Ratios(int ratio) {
        conversion = ratio;
    }
}
