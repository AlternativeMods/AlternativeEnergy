package jkmau5.alternativeenergy.world;

import cpw.mods.fml.common.registry.GameRegistry;
import jkmau5.alternativeenergy.AltEngCompat;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * No description given
 *
 * @author jk-5
 */
public class AltEngRecipes {

    public static void init(){
        if(AltEngCompat.hasIC2 && AltEngCompat.hasBC){
            initIC2AndBCRecipes();
        }
    }

    private static void initIC2AndBCRecipes(){
        Item goldKinesis = GameRegistry.findItem("BuildCraft|Transport", "item.buildcraftPipe.pipepowergold");
        Item woodKinesis = GameRegistry.findItem("BuildCraft|Transport", "item.buildcraftPipe.pipepowerwood");
        ItemStack diamondChipset = GameRegistry.findItemStack("BuildCraft|Silicon", "redstone_diamond_chipset", 1);

        ItemStack hvTransformer = ic2.api.item.Items.getItem("hvTransformer");
        ItemStack goldCable = ic2.api.item.Items.getItem("insulatedGoldCableItem");
        ItemStack energyCrystal = ic2.api.item.Items.getItem("energyCrystal");

        ItemStack glassFiber = ic2.api.item.Items.getItem("glassFiberCableItem");

        //------------------------------------------------------------------------------------------------------------------

        //GameRegistry.addShapedRecipe(new ItemStack(AltEngBlocks.blockPowerBox), new Object[]{"GWG", "HED", "CCC", 'G', goldKinesis, 'W', woodKinesis, 'H', hvTransformer, 'E', energyCrystal, 'D', diamondChipset, 'C', goldCable});

        //GameRegistry.addShapedRecipe(new ItemStack(AltEngItems.itemUpgrade, 1, 0), " A ", "ABA", " A ", 'A', goldKinesis, 'B', energyCrystal);

        //GameRegistry.addShapedRecipe(new ItemStack(AltEngItems.itemUpgrade, 1, 1), "DAD", "BCB", "DAD", 'A', Item.redstone, 'B', glassFiber, 'C', woodKinesis, 'D', Block.blockRedstone);

        //GameRegistry.addShapedRecipe(new ItemStack(AltEngBlocks.blockPowerCable, 16), new Object[] {" C ", "CPC", " C ", 'C', glassFiber, 'P', AltEngBlocks.blockPowerBox});
    }
}
