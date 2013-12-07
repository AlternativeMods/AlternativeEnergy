package jkmau5.alternativeenergy.compat;

/**
 * No description given
 *
 * @author jk-5
 */
public interface ICompatPlugin {

    /**
     * @return The modid of the mod that this plugin provides compatibility for
     */
    String getModID();

    void preInit();
    void init();
    void postInit();
}
