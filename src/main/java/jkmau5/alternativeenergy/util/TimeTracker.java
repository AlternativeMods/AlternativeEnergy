package jkmau5.alternativeenergy.util;

import net.minecraft.world.World;

/**
 * No description given
 *
 * @author jk-5
 */
public class TimeTracker {

    private long lastMark = Long.MIN_VALUE;
    private long duration = -1;

    /**
     * Return true if a given delay has passed since last time marked was called
     * successfully.
     */
    public boolean markTimeIfPassed(World world, long delay) {
        if (world == null) {
            return false;
        }

        long currentTime = world.getTotalWorldTime();

        if (currentTime < lastMark) {
            lastMark = currentTime;
            return false;
        } else if (lastMark + delay <= currentTime) {
            duration = currentTime - lastMark;
            lastMark = currentTime;
            return true;
        } else {
            return false;
        }

    }

    public long durationOfLastDelay() {
        return duration > 0 ? duration : 0;
    }

    public void markTime(World world) {
        lastMark = world.getTotalWorldTime();
    }
}
