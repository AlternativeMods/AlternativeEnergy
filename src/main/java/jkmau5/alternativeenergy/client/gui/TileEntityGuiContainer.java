package jkmau5.alternativeenergy.client.gui;

import cpw.mods.fml.common.network.PacketDispatcher;
import jkmau5.alternativeenergy.gui.container.AltEngContainer;
import jkmau5.alternativeenergy.network.PacketGuiCloseSaveData;
import jkmau5.alternativeenergy.util.interfaces.IGuiCloseSaveDataHandler;
import jkmau5.alternativeenergy.world.tileentity.AltEngTileEntity;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;

/**
 * No description given
 *
 * @author jk-5
 */
public class TileEntityGuiContainer extends AltEngGuiContainer {

    @Getter
    private final AltEngTileEntity tileEntity;

    public TileEntityGuiContainer(AltEngContainer container, ResourceLocation background, AltEngTileEntity tileEntity) {
        super(container, background);
        this.tileEntity = tileEntity;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();

        if(this.tileEntity instanceof IGuiCloseSaveDataHandler) {
            PacketDispatcher.sendPacketToServer(new PacketGuiCloseSaveData((IGuiCloseSaveDataHandler) this.tileEntity).getPacket());
        }
    }
}
