package core;

/**
 * Author: Lordmau5
 * Date: 21.08.13
 * Time: 16:18
 * You are not allowed to change this code,
 * nor publish it without my permission.
 */
public enum Ratios {
    EU(Config.eu),
    MJ(Config.mj);
    public double conversion = 0;

    private Ratios(double ratio) {
        conversion = ratio;
    }
}