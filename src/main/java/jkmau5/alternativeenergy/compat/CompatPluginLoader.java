package jkmau5.alternativeenergy.compat;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.Loader;
import jkmau5.alternativeenergy.AltEngLog;
import jkmau5.alternativeenergy.compat.waila.PluginWaila;
import lombok.Getter;

import java.util.List;

/**
 * No description given
 *
 * @author jk-5
 */
public class CompatPluginLoader {

    private enum Phase{
        PRELAUNCH, PREINIT, INIT, POSTINIT, DONE
    }

    @Getter
    private static CompatPluginLoader instance = new CompatPluginLoader();

    private List<ICompatPlugin> plugins = Lists.newArrayList();
    private Phase currPhase = Phase.PRELAUNCH;

    private CompatPluginLoader(){}

    public void registerPlugin(ICompatPlugin plugin){
        if (Loader.isModLoaded(plugin.getModID())){
            AltEngLog.info("Loading compat plugin for %s", plugin.getModID());
            this.plugins.add(plugin);

            switch (currPhase){
                case DONE:
                case POSTINIT:
                    plugin.preInit();
                    plugin.init();
                    plugin.postInit();
                    break;
                case INIT:
                    plugin.preInit();
                    plugin.init();
                    break;
                case PREINIT:
                    plugin.preInit();
                    break;
                default:
                    break;
            }
        }
    }

    public void preInit(){
        this.currPhase = Phase.PREINIT;
        for (ICompatPlugin plugin : plugins) plugin.preInit();
    }

    public void init(){
        this.currPhase = Phase.INIT;
        for (ICompatPlugin plugin : plugins) plugin.init();
    }

    public void postInit(){
        this.currPhase = Phase.POSTINIT;
        for (ICompatPlugin plugin : plugins) plugin.postInit();
        this.currPhase = Phase.DONE;
    }

    public void registerBuiltInPlugins(){
        this.registerPlugin(new PluginWaila());
    }
}
