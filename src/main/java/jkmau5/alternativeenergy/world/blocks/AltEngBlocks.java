package jkmau5.alternativeenergy.world.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import jkmau5.alternativeenergy.world.item.ItemBlockPowerStorage;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * Author: Lordmau5
 * Date: 21.08.13
 * Time: 16:55
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class AltEngBlocks {

    public static Block blockPowerCable;
    public static Block blockPowerStorage;
    public static Block blockConveyor;

    public static void init() {
        blockPowerCable = new BlockPowerCable(Material.iron);
        blockPowerStorage = new BlockPowerStorage();
        blockConveyor = new BlockConveyor();

        GameRegistry.registerBlock(AltEngBlocks.blockPowerStorage, ItemBlockPowerStorage.class, "powerStorage");
        GameRegistry.registerBlock(AltEngBlocks.blockPowerCable, "powerCable");
        GameRegistry.registerBlock(AltEngBlocks.blockConveyor, "conveyor");
    }
}