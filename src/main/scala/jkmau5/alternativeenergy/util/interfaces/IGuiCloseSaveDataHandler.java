package jkmau5.alternativeenergy.util.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * No description given
 *
 * @author jk-5
 */
public interface IGuiCloseSaveDataHandler {

    World getWorld();
    void writeGuiCloseData(DataOutput output) throws IOException;
    void readGuiCloseData(DataInput input, EntityPlayer player) throws IOException;
}
