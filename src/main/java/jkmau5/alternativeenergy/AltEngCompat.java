package jkmau5.alternativeenergy;

import buildcraft.api.tools.IToolWrench;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import ic2.api.item.Items;
import net.minecraft.item.ItemStack;

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
}
