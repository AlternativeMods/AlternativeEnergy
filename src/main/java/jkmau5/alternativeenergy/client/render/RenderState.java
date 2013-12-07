package jkmau5.alternativeenergy.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

/**
 * No description given
 *
 * @author jk-5
 */
public class RenderState {

    public static void bindTexture(ResourceLocation resourceLocation) {
        Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation);
    }
}
