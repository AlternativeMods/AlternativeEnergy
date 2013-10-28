package client;

import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import core.Main;
import cpw.mods.fml.client.FMLClientHandler;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.tile.IEnergyStorage;
import net.minecraft.client.model.ModelBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import tile.TileEntityPowerCable;

/**
 * Author: Lordmau5
 * Date: 28.10.13
 * Time: 11:00
 * You are not allowed to change this code,
 * nor publish it without my permission.
 */
public class ModelPowerCable extends ModelBase {
    private int MODE_NS = 1;
    private int MODE_EW = 2;
    private int MODE_TB = 3;

    private IModelCustom North_South;
    private IModelCustom East_West;
    private IModelCustom Top_Bottom;

    public ModelPowerCable()
    {
        North_South = AdvancedModelLoader.loadModel("/assets/powerboxes/textures/models/Standard.obj");
        East_West = AdvancedModelLoader.loadModel("/assets/powerboxes/textures/models/Standard.obj");
        Top_Bottom = AdvancedModelLoader.loadModel("/assets/powerboxes/textures/models/Standard.obj");
    }

    public void render(int mode)
    {
        if(mode == MODE_NS)
            North_South.renderAll();
        else if(mode == MODE_EW)
            East_West.renderAll();
        else if(mode == MODE_TB)
            Top_Bottom.renderAll();
        else
        {
            North_South.renderAll();
            East_West.renderAll();
            Top_Bottom.renderAll();
        }
    }

    public void render(TileEntityPowerCable thisTile, double d0, double d1, double d2)
    {
        World bAccess = thisTile.worldObj;

        double ew_posX = d0;
        double tb_posY = d1;
        double ns_posZ = d2;

        float ew_scaleX = 0.5f;
        float tb_scaleY = 0.5f;
        float ns_scaleZ = 0.5f;

        int x = thisTile.xCoord;
        int y = thisTile.yCoord;
        int z = thisTile.zCoord;

        TileEntity xTile = null;

        xTile = thisTile.worldObj.getBlockTileEntity(x - 1, y, z);
        if(xTile != null)
        {
            if(xTile instanceof TileEntityPowerCable
                    || xTile instanceof IEnergySink
                    || xTile instanceof IEnergyStorage
                    || xTile instanceof IEnergySource
                    || xTile instanceof IPowerReceptor
                    || xTile instanceof IPowerEmitter) {
                ew_posX -= 0.5F;
                ew_scaleX = 2F;
            }
        }

        xTile = thisTile.worldObj.getBlockTileEntity(x + 1, y, z);
        if(xTile != null)
        {
            if(xTile instanceof TileEntityPowerCable
                    || xTile instanceof IEnergySink
                    || xTile instanceof IEnergyStorage
                    || xTile instanceof IEnergySource
                    || xTile instanceof IPowerReceptor
                    || xTile instanceof IPowerEmitter) {
                ew_posX += 0.5F;
                ew_scaleX = 2F;
            }
        }

        xTile = thisTile.worldObj.getBlockTileEntity(x, y - 1, z);
        if(xTile != null)
        {
            if(xTile instanceof TileEntityPowerCable
                    || xTile instanceof IEnergySink
                    || xTile instanceof IEnergyStorage
                    || xTile instanceof IEnergySource
                    || xTile instanceof IPowerReceptor
                    || xTile instanceof IPowerEmitter) {
                tb_posY -= 0.5F;
                tb_scaleY = 2F;
            }
        }

        xTile = thisTile.worldObj.getBlockTileEntity(x, y + 1, z);
        if(xTile != null)
        {
            if(xTile instanceof TileEntityPowerCable
                    || xTile instanceof IEnergySink
                    || xTile instanceof IEnergyStorage
                    || xTile instanceof IEnergySource
                    || xTile instanceof IPowerReceptor
                    || xTile instanceof IPowerEmitter) {
                tb_posY += 0.5F;
                tb_scaleY = 2F;
            }
        }

        xTile = thisTile.worldObj.getBlockTileEntity(x, y, z - 1);
        if(xTile != null)
        {
            if(xTile instanceof TileEntityPowerCable
                    || xTile instanceof IEnergySink
                    || xTile instanceof IEnergyStorage
                    || xTile instanceof IEnergySource
                    || xTile instanceof IPowerReceptor
                    || xTile instanceof IPowerEmitter) {
                ns_posZ -= 0.5F;
                ns_scaleZ = 2F;
            }
        }

        xTile = thisTile.worldObj.getBlockTileEntity(x, y, z + 1);
        if(xTile != null)
        {
            if(xTile instanceof TileEntityPowerCable
                    || xTile instanceof IEnergySink
                    || xTile instanceof IEnergyStorage
                    || xTile instanceof IEnergySource
                    || xTile instanceof IPowerReceptor
                    || xTile instanceof IPowerEmitter) {
                ns_posZ += 0.5F;
                ns_scaleZ = 2F;
            }
        }

        beforeRender(MODE_NS, d0, d1, ns_posZ, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F, ns_scaleZ);
        beforeRender(MODE_EW, ew_posX, d1, d2, 0.5F, 0.5F, 0.5F, ew_scaleX, 0.5F, 0.5F);
        beforeRender(MODE_TB, d0, tb_posY, d2, 0.5F, 0.5F, 0.5F, 0.5F, tb_scaleY, 0.5F);
    }

    public void beforeRender(int mode, double posX, double posY, double posZ, float posX_mod, float posY_mod, float posZ_mod, float scaleX, float scaleY, float scaleZ)
    {
        if(mode == MODE_NS)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)posX + posX_mod, (float)posY + posY_mod, (float)posZ + posZ_mod);
            GL11.glScalef(scaleX, scaleY, scaleZ);
            FMLClientHandler.instance().getClient().renderEngine.bindTexture(new ResourceLocation(Main.modid.toLowerCase(), "textures/blocks/RedstoneCable.png"));
            this.render(MODE_NS);
            GL11.glPopMatrix();
        }
        else if(mode == MODE_EW)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)posX + 0.5F, (float)posY + 0.5F, (float)posZ + 0.5F);
            GL11.glScalef(scaleX, scaleY, scaleZ);
            FMLClientHandler.instance().getClient().renderEngine.bindTexture(new ResourceLocation(Main.modid.toLowerCase(), "textures/blocks/RedstoneCable.png"));
            this.render(MODE_EW);
            GL11.glPopMatrix();
        }
        else if(mode == MODE_TB)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)posX + 0.5F, (float)posY + 0.5F, (float)posZ + 0.5F);
            GL11.glScalef(scaleX, scaleY, scaleZ);
            FMLClientHandler.instance().getClient().renderEngine.bindTexture(new ResourceLocation(Main.modid.toLowerCase(), "textures/blocks/RedstoneCable.png"));
            this.render(MODE_TB);
            GL11.glPopMatrix();
        }
    }
}