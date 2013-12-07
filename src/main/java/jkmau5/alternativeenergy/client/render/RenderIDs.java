package jkmau5.alternativeenergy.client.render;

import cpw.mods.fml.client.registry.RenderingRegistry;
import lombok.Getter;

/**
 * All block render id's are stored in this enum
 * Call {@code getRenderID} on an entry to obtain its id
 *
 * @author jk-5
 */
public enum RenderIDs {
    POWERCABLE;

    @Getter
    private final int renderID;

    private RenderIDs() {
        this.renderID = RenderingRegistry.getNextAvailableRenderId();
    }
}
