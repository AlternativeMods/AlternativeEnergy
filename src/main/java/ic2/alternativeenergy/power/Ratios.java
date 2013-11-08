package ic2.alternativeenergy.power;

import ic2.alternativeenergy.Config;

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
    public double conversion = 0;

    private Ratios(double ratio) {
        conversion = ratio;
    }
}