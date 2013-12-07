package jkmau5.alternativeenergy.network.synchronisation;

import jkmau5.alternativeenergy.AltEngLog;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * No description given
 *
 * @author jk-5
 */
public enum SynchronisationHandlerType {

    TILEENTITY{

        @Override
        public ISynchronisationHandler findHandler(World world, DataInput input) throws IOException {
            int x = input.readInt();
            int y = input.readInt();
            int z = input.readInt();
            if (world != null) {
                if (world.blockExists(x, y, z)) {
                    TileEntity tile = world.getBlockTileEntity(x, y, z);
                    if (tile instanceof ISynchronisationHandler) {
                        return (ISynchronisationHandler) tile;
                    }
                }
            }
            AltEngLog.warning("Invalid handler info: can't find ISynchronisationHandler TileEntity @ (%d,%d,%d)", x, y, z);
            return null;
        }

        @Override
        public void writeHandlerInfo(ISynchronisationHandler handler, DataOutput output) throws IOException {
            try {
                TileEntity te = (TileEntity)handler;
                output.writeInt(te.xCoord);
                output.writeInt(te.yCoord);
                output.writeInt(te.zCoord);
            } catch (ClassCastException e) {
                throw new RuntimeException("Invalid usage of handler type", e);
            }
        }
    };

    public abstract ISynchronisationHandler findHandler(World world, DataInput input) throws IOException;

    public abstract void writeHandlerInfo(ISynchronisationHandler handler, DataOutput output) throws IOException;

    static final SynchronisationHandlerType[] TYPES = values();
}
