package client;

import core.Main;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

/**
 * Author: Lordmau5
 * Date: 29.10.13
 * Time: 18:10
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class ModelPowerCable extends ModelBase {

    ModelRenderer base;
    ModelRenderer[] toNorth = new ModelRenderer[2];
    ModelRenderer[] toSouth = new ModelRenderer[2];
    ModelRenderer[] toEast = new ModelRenderer[2];
    ModelRenderer[] toWest = new ModelRenderer[2];
    ModelRenderer[] toTop = new ModelRenderer[2];
    ModelRenderer[] toBottom = new ModelRenderer[2];

    public ModelPowerCable() {
        initModels();
    }

    public void initModels() {
        base = new ModelRenderer(this, 0, 0);
        base.addBox(0F, 0F, 0F, 10, 10, 10);
        base.setRotationPoint(-5F, 27F, -5F);
        base.setTextureSize(128, 64);
        setRotation(base, 0F, 0F, 0F);

        toNorth[0] = new ModelRenderer(this, 0, 32);
        toNorth[0].addBox(0F, 0F, 0F, 5, 5, 5);
        toNorth[0].setRotationPoint(-2.5F, 13.5F, -7.5F);
        toNorth[0].setTextureSize(128, 64);
        toNorth[0].mirror = true;
        setRotation(toNorth[0], 0F, 0F, 0F);
        /*toNorth[1] = new ModelRenderer(this, 0, 0);
        toNorth[1].addBox(0F, 0F, 0F, 5, 5, 1);
        toNorth[1].setRotationPoint(-2.5F, 13.5F, -8F);
        toNorth[1].setTextureSize(64, 64);
        toNorth[1].mirror = true;
        setRotation(toNorth[1], 0F, 0F, 0F);*/

        toSouth[0] = new ModelRenderer(this, 0, 32);
        toSouth[0].addBox(0F, 0F, 0F, 5, 5, 5);
        toSouth[0].setRotationPoint(-2.5F, 13.5F, 3F);
        toSouth[0].setTextureSize(128, 64);
        toSouth[0].mirror = true;
        setRotation(toSouth[0], 0F, 0F, 0F);
        /*toSouth[1] = new ModelRenderer(this, 0, 0);
        toSouth[1].addBox(0F, 0F, 0F, 5, 5, 1);
        toSouth[1].setRotationPoint(-2.5F, 13.5F, 2.3F);
        toSouth[1].setTextureSize(64, 64);
        toSouth[1].mirror = true;
        setRotation(toSouth[1], 0F, 0F, 0F);*/

        toEast[0] = new ModelRenderer(this, 0, 0);
        toEast[0].addBox(0F, 0F, 0F, 5, 5, 5);
        toEast[0].setRotationPoint(-7.5F, 13.5F, -2.5F);
        toEast[0].setTextureSize(128, 64);
        toEast[0].mirror = true;
        setRotation(toEast[0], 0F, 0F, 0F);
        toEast[1] = new ModelRenderer(this, 0, 0);
        toEast[1].addBox(0F, 0F, 0F, 1, 5, 5);
        toEast[1].setRotationPoint(-8F, 13.5F, -2.5F);
        toEast[1].setTextureSize(128, 64);
        toEast[1].mirror = true;
        setRotation(toEast[1], 0F, 0F, 0F);

        toWest[0] = new ModelRenderer(this, 0, 0);
        toWest[0].addBox(0F, 0F, 0F, 5, 5, 5);
        toWest[0].setRotationPoint(3F, 13.5F, -2.5F);
        toWest[0].setTextureSize(128, 64);
        toWest[0].mirror = true;
        setRotation(toWest[0], 0F, 0F, 0F);
        toWest[1] = new ModelRenderer(this, 0, 0);
        toWest[1].addBox(0F, 0F, 0F, 1, 5, 5);
        toWest[1].setRotationPoint(2.3F, 13.5F, -2.5F);
        toWest[1].setTextureSize(128, 64);
        toWest[1].mirror = true;
        setRotation(toWest[1], 0F, 0F, 0F);

        toTop[0] = new ModelRenderer(this, 0, 0);
        toTop[0].addBox(0F, 0F, 0F, 5, 5, 5);
        toTop[0].setRotationPoint(-2.5F, 8.5F, -2.5F);
        toTop[0].setTextureSize(128, 64);
        toTop[0].mirror = true;
        setRotation(toTop[0], 0F, 0F, 0F);
        toTop[1] = new ModelRenderer(this, 0, 0);
        toTop[1].addBox(0F, 0F, 0F, 5, 1, 5);
        toTop[1].setRotationPoint(-2.5F, 8F, -2.5F);
        toTop[1].setTextureSize(128, 64);
        toTop[1].mirror = true;
        setRotation(toTop[1], 0F, 0F, 0F);

        toBottom[0] = new ModelRenderer(this, 0, 0);
        toBottom[0].addBox(0F, 0F, 0F, 5, 5, 5);
        toBottom[0].setRotationPoint(-2.5F, 19F, -2.5F);
        toBottom[0].setTextureSize(128, 64);
        toBottom[0].mirror = true;
        setRotation(toBottom[0], 0F, 0F, 0F);
        toBottom[1] = new ModelRenderer(this, 0, 0);
        toBottom[1].addBox(0F, 0F, 0F, 5, 1, 5);
        toBottom[1].setRotationPoint(-2.5F, 18.5F, -2.5F);
        toBottom[1].setTextureSize(128, 64);
        toBottom[1].mirror = true;
        setRotation(toBottom[1], 0F, 0F, 0F);

    }

    public void render(TileEntity tile, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(null, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, null);
        base.render(f5);
        for(int i=0; i<6; i++) {
            ForgeDirection dr = ForgeDirection.getOrientation(i);
            TileEntity tmpTile = tile.worldObj.getBlockTileEntity(tile.xCoord + dr.offsetX, tile.yCoord + dr.offsetY, tile.zCoord + dr.offsetZ);
            if(tmpTile != null && Main.isValidPowerTile(tmpTile)) {
                if(dr == ForgeDirection.UP)
                    for(ModelRenderer rendT : toTop)
                        if(rendT != null)
                            rendT.render(f5);
                else if(dr == ForgeDirection.DOWN)
                    for(ModelRenderer rendB : toBottom)
                        if(rendB != null)
                            rendB.render(f5);
                else if(dr == ForgeDirection.NORTH)
                    for(ModelRenderer rendN : toNorth)
                        if(rendN != null)
                            rendN.render(f5);
                else if(dr == ForgeDirection.SOUTH)
                    for(ModelRenderer rendS : toSouth)
                        if(rendS != null)
                            rendS.render(f5);
                else if(dr == ForgeDirection.EAST)
                    for(ModelRenderer rendE : toEast)
                        if(rendE != null)
                            rendE.render(f5);
                else if(dr == ForgeDirection.WEST)
                    for(ModelRenderer rendW : toWest)
                        if(rendW != null)
                            rendW.render(f5);
            }
        }
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }
}