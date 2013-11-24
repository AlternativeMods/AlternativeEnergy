package jkmau5.alternativeenergy;

import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.tools.IToolWrench;
import buildcraft.api.transport.IPipeTile;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import ic2.api.energy.tile.IEnergyConductor;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.item.Items;
import ic2.api.tile.IEnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import javax.swing.*;

/**
 * No description given
 *
 * @author jk-5
 */
public class AltEngCompat {

    public static boolean hasBC = Loader.isModLoaded("BuildCraft|Transport");
    public static boolean hasIC2 = Loader.isModLoaded("IC2");
    public static boolean hasCC = Loader.isModLoaded("ComputerCraft");

    public static void checkCompat(){
        hasBC = Loader.isModLoaded("BuildCraft|Transport");
        hasIC2 = Loader.isModLoaded("IC2");
        hasCC = Loader.isModLoaded("ComputerCraft");
        if(!hasIC2 && !hasBC){
            if (FMLCommonHandler.instance().getSide().isClient()) {
                JOptionPane optionPane = new JOptionPane();
                optionPane.setMessage("AlternativeEnergy is useless without one of the corresponding mods (BuildCraft or IndustrialCraft2).\n" + "Install atleast one of them, and the mod will have features!");
                optionPane.setMessageType(JOptionPane.WARNING_MESSAGE);
                JDialog dialog = optionPane.createDialog("Compatability warning!");
                dialog.setAlwaysOnTop(true);
                dialog.setVisible(true);
            }
        }
    }

    public static boolean isWrench(ItemStack stack){
        if(stack.getItem() instanceof IToolWrench) return true;
        if(stack.getItem() == Items.getItem("wrench").getItem() || stack.getItem() == Items.getItem("electricWrench").getItem()) return true;
        return false;
    }

    private static String[] invalidTileClassNames = {"TileEntityTeleporter", "TileCapacitorBank", "TileConduitBundle", "PipeTile"};
    private static String[] validTileClassNames = {"TileEntityCompactSolar"};
    private static String[] validTileSuperNames = {"TileEntityCompactSolar"};

    public static boolean isInvalidPowerTile(TileEntity tile) {
        Class<? extends TileEntity> cl = tile.getClass();
        for(String cClass : invalidTileClassNames){
            if(cl.getSimpleName().equalsIgnoreCase(cClass)){
                return true;
            }
        }
        return false;
    }

    public static boolean checkForModTile(TileEntity tile) {
        if(AltEngCompat.hasBC){
            if(tile instanceof IPipeTile) return false;
            if(tile instanceof IPowerReceptor) return true;
        }
        if(AltEngCompat.hasIC2) {
            if(tile instanceof IEnergyConductor) return false;
            if(tile instanceof IEnergySink || tile instanceof IEnergyStorage || tile instanceof IEnergySource) return true;
        }
        return false;
    }

    public static boolean isValidPowerTile(TileEntity tile) {
        if(isInvalidPowerTile(tile)) return false;

        Class<? extends TileEntity> cl = tile.getClass();
        for(String cClass : validTileClassNames) {
            if(cl.getSimpleName().equalsIgnoreCase(cClass)){
                return true;
            }
        }
        Class<?> superClass = cl.getSuperclass();
        if(superClass != null){
            for(String sClass : validTileSuperNames) {
                if(superClass.getSimpleName().equalsIgnoreCase(sClass)){
                    return true;
                }
            }
        }
        return checkForModTile(tile);
    }

    public static boolean isICTile(TileEntity tile) {
        if(!hasIC2)
            return false;

        if(tile instanceof IEnergySink || tile instanceof IEnergyStorage || tile instanceof IEnergySource)
            return true;

        return false;
    }
}
