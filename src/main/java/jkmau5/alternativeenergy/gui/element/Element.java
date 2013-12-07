package jkmau5.alternativeenergy.gui.element;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jkmau5.alternativeenergy.client.gui.AltEngGuiContainer;
import jkmau5.alternativeenergy.client.render.ToolTip;
import jkmau5.alternativeenergy.gui.container.AltEngContainer;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.minecraft.inventory.ICrafting;

import java.io.DataInput;
import java.io.IOException;

/**
 * No description given
 *
 * @author jk-5
 */
@RequiredArgsConstructor
public abstract class Element {

    public final int x;
    public final int y;
    public final int u;
    public final int v;
    public final int w;
    public final int h;
    public boolean isHidden = false;
    @Setter protected AltEngContainer container;

    @SideOnly(Side.CLIENT)
    public final boolean isMouseOver(int mouseX, int mouseY) {
        return (mouseX >= this.x - 1) && (mouseX < this.x + this.w + 1) && (mouseY >= this.y - 1) && (mouseY < this.y + this.h + 1);
    }

    @SideOnly(Side.CLIENT)
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void draw(AltEngGuiContainer gui, int guiX, int guiY, int mouseX, int mouseY) {
        gui.drawTexturedModalRect(guiX + this.x, guiY + this.y, this.u, this.v, this.w, this.h);
    }

    @SideOnly(Side.CLIENT)
    public ToolTip getToolTip() {
        return null;
    }

    public void initElement(ICrafting player) {

    }

    public void updateElement(ICrafting player) {

    }

    @SideOnly(Side.CLIENT)
    public void handleClientPacketData(DataInput input) throws IOException {

    }
}
