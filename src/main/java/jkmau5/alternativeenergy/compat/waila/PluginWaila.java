package jkmau5.alternativeenergy.compat.waila;

import cpw.mods.fml.common.event.FMLInterModComms;
import jkmau5.alternativeenergy.compat.ICompatPlugin;

/**
 * No description given
 *
 * @author jk-5
 */
public class PluginWaila implements ICompatPlugin {

    @Override
    public String getModID() {
        return "Waila";
    }

    @Override
    public void preInit() {
    }

    @Override
    public void init() {
        FMLInterModComms.sendMessage("Waila", "register", "jkmau5.alternativeenergy.compat.waila.WailaProvider.callbackRegister");
    }

    @Override
    public void postInit() {
    }
}
