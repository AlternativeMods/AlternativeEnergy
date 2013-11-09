package jkmau5.alternativeenergy.world.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import jkmau5.alternativeenergy.Config;
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

    public static Block blockPowerBox;
    public static Block blockPowerCable;
    public static Block blockLinkBox;

    public static void init(){
        blockPowerBox = new BlockPowerBox(Config.powerBox_blockId, Material.iron);
        blockPowerCable = new BlockPowerCable(Config.powerCable_blockId, Material.iron);
        blockLinkBox = new BlockLinkBox(Config.linkBox_blockId, Material.iron);

        GameRegistry.registerBlock(AltEngBlocks.blockPowerBox, ItemBlockPowerBox.class, "powerBox");
        GameRegistry.registerBlock(AltEngBlocks.blockPowerCable, "powerCable");
        GameRegistry.registerBlock(AltEngBlocks.blockLinkBox, "linkBox");
    }
}