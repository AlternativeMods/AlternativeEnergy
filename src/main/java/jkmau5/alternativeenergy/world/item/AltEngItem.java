package jkmau5.alternativeenergy.world.item;

import cpw.mods.fml.common.registry.GameRegistry;
import jkmau5.alternativeenergy.AlternativeEnergy;
import jkmau5.alternativeenergy.Constants;
import jkmau5.alternativeenergy.util.Utils;
import jkmau5.alternativeenergy.util.config.ConfigTag;
import lombok.Getter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * No description given
 *
 * @author jk-5
 */
public abstract class AltEngItem extends Item {

    @Getter
    private final ConfigTag itemConfig;

    protected AltEngItem(String unlocalizedName) {
        super(AlternativeEnergy.getInstance().getConfig().getTag("items").useBraces().getTag(unlocalizedName).useBraces().getTag("itemid").getIntValue(Utils.getFreeItemID()));
        this.itemConfig = AlternativeEnergy.getInstance().getConfig().getTag("items").useBraces().getTag(unlocalizedName).useBraces();
        this.setUnlocalizedName("altEng." + unlocalizedName);
        this.setTextureName(Constants.TEXTURE_DOMAIN + ":" + unlocalizedName);
    }

    protected AltEngItem(String unlocalizedName, Object[] recipe) {
        super(AlternativeEnergy.getInstance().getConfig().getTag("items").useBraces().getTag(unlocalizedName).useBraces().getTag("itemid").getIntValue(Utils.getFreeItemID()));
        this.itemConfig = AlternativeEnergy.getInstance().getConfig().getTag("items").useBraces().getTag(unlocalizedName).useBraces();
        this.setUnlocalizedName("altEng." + unlocalizedName);
        this.setTextureName(Constants.TEXTURE_DOMAIN + ":" + unlocalizedName);

        GameRegistry.addShapedRecipe(new ItemStack(this, 1), recipe);
    }
}
