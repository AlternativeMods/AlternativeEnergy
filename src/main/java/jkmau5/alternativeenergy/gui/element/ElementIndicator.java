package jkmau5.alternativeenergy.gui.element;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jkmau5.alternativeenergy.client.gui.AltEngGuiContainer;
import jkmau5.alternativeenergy.client.render.ToolTip;

/**
 * No description given
 *
 * @author jk-5
 */
public class ElementIndicator extends Element {

    public final IIndicatorController controller;

    public ElementIndicator(IIndicatorController controller, int x, int y, int u, int v, int w, int h) {
        super(x, y, u, v, w, h);
        this.controller = controller;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void draw(AltEngGuiContainer gui, int guiX, int guiY, int mouseX, int mouseY) {
        int scale = this.controller.getScaledLevel(this.h);
        //                        X-----------X, Y----------------------------Y, U----U, V---------------------V,  width, height
        gui.drawTexturedModalRect(guiX + this.x, guiY + this.y + this.h - scale, this.u, this.v + this.h - scale, this.w, scale);
    }

    @Override
    public ToolTip getToolTip() {
        return this.controller.getToolTip();
    }
}
