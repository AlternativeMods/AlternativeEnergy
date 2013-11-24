package jkmau5.alternativeenergy.util.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Implement this interface on your TileEntity or Entity when you want to sync GUI data.
 *
 * @author jk-5
 */
public interface IGuiCloseSaveDataHandler {

    /**
     * @return The {@link World} that the object that belongs to this {@link IGuiCloseSaveDataHandler} is in
     */
    World getWorld();

    /**
     * This will write all the gui data to an stream and is send to the server.
     *
     * @param output The stream you can write the data to
     * @throws IOException When the DataOutput throws an {@link IOException}
     */
    void writeGuiCloseData(DataOutput output) throws IOException;

    /**
     * Read the gui data, received from the client, in here and do something with it
     *
     * @param input The data received from the client
     * @param player The player that sent this data to us
     * @throws IOException When the DataOutput throws an {@link IOException}
     */
    void readGuiCloseData(DataInput input, EntityPlayer player) throws IOException;
}
