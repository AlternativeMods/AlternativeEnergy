package jkmau5.alternativeenergy.util;

import net.minecraftforge.common.ForgeDirection;

import java.util.BitSet;

/**
 * No description given
 *
 * @author jk-5
 */
public class CableConnectionMatrix {

    private BitSet connections = new BitSet(ForgeDirection.values().length);
    private boolean shouldUpdate = false;

    public boolean isConnected(ForgeDirection direction) {
        return this.connections.get(direction.ordinal());
    }

    public boolean setConnected(ForgeDirection direction, boolean connected) {
        if (this.connections.get(direction.ordinal()) != connected) {
            this.connections.set(direction.ordinal(), connected);
            this.shouldUpdate = true;
        }
        return this.connections.get(direction.ordinal());
    }

    public void setShouldUpdate(boolean shouldUpdate) {
        this.shouldUpdate = shouldUpdate;
    }

    public boolean shouldUpdate() {
        return this.shouldUpdate;
    }

    public void onUpdated() {
        this.shouldUpdate = false;
    }
}
