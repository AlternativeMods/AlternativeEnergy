package jkmau5.alternativeenergy.world.tileentity;

import cpw.mods.fml.common.registry.GameRegistry;

/**
 * No description given
 *
 * @author jk-5
 */
public class AltEngTileEntities {

    public static void init() {
        //Change the names and the tileEntities will disappear from the world, making all the people angry!
        //Don't change them!
        GameRegistry.registerTileEntity(TileEntityPowerBox.class, "AltEng.PowerBox");
        GameRegistry.registerTileEntity(TileEntityPowerCable.class, "AltEng.PowerCable");
        GameRegistry.registerTileEntity(TileEntityLinkBox.class, "AltEng.LinkBox");
        GameRegistry.registerTileEntity(TileEntityConveyor.class, "AltEng.Conveyor");
    }
}
