package Lordmau5.PowerBoxes.item;

import Lordmau5.PowerBoxes.core.Main;
import Lordmau5.PowerBoxes.tile.TileEntityLinkBox;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Author: Lordmau5
 * Date: 03.11.13
 * Time: 11:35
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class ItemLinkCard extends Item {

    public ItemLinkCard(int par1) {
        super(par1);
        setUnlocalizedName("linkCard");
        setCreativeTab(Main.tabPowerBox);
        setMaxStackSize(1);
    }

    @Override
    public void registerIcons(IconRegister iR)
    {
        this.itemIcon = iR.registerIcon(Main.modid + ":" + this.getUnlocalizedName().substring(5));
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if(world.isRemote)
            return true;

        if(itemStack.getTagCompound() == null)
            itemStack.setTagCompound(new NBTTagCompound());

        TileEntity tmpTile = world.getBlockTileEntity(x, y, z);
        if(tmpTile != null && tmpTile instanceof TileEntityLinkBox) {
            TileEntityLinkBox linkBox = (TileEntityLinkBox) tmpTile;

            if(player.isSneaking()) {
                itemStack.getTagCompound().setInteger("linkId", linkBox.getLinkId());
                player.addChatMessage("Gathered the Link ID " + linkBox.getLinkId() + " from the Link Box.");
                return true;
            }

            if(!itemStack.getTagCompound().hasKey("linkId")) {
                player.addChatMessage("No Link ID set or corrupted.");
                return false;
            }
            linkBox.setLinkId(itemStack.getTagCompound().getInteger("linkId"));
            player.addChatMessage("Set the Link ID of the Link Box to " + linkBox.getLinkId() + ".");
            return true;
        }

        if(!player.isSneaking())
            return false;

        int random = new Random().nextInt(9) + 1;
        itemStack.getTagCompound().setInteger("linkId", random);
        player.addChatMessage("Set the Link ID to " + random + " (Random).");
        return true;
    }
}