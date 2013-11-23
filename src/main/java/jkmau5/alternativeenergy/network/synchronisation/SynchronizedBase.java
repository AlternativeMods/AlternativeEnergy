package jkmau5.alternativeenergy.network.synchronisation;

import jkmau5.alternativeenergy.AlternativeEnergy;
import lombok.Setter;
import net.minecraft.world.World;

/**
 * No description given
 *
 * @author jk-5
 */
public abstract class SynchronizedBase implements ISynchronized {

    protected long lastChangeTime = 0;
    protected boolean dirty = false;
    @Setter protected boolean saveToNBT = true;

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void markClean() {
        dirty = false;
    }

    @Override
    public void markDirty() {
        dirty = true;
    }

    @Override
    public void resetChangeTimer(World world) {
        lastChangeTime = AlternativeEnergy.getProxy().getTicks(world);
    }

    @Override
    public int getTicksSinceChange(World world) {
        return (int)(AlternativeEnergy.getProxy().getTicks(world) - lastChangeTime);
    }
}
