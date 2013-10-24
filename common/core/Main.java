package core;

import block.BlockPowerBox;
import block.ItemBlockPowerBox;
import buildcraft.BuildCraftCore;
import buildcraft.BuildCraftTransport;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import ic2.core.Ic2Items;
import item.ItemUpgrade_Capacity;
import item.ItemUpgrade_OutputSpeed;
import item.Items;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import tile.TileEntityPowerBox;

/**
 * Author: Lordmau5
 * Date: 21.08.13
 * Time: 16:10
 * You are not allowed to change this code,
 * nor publish it without my permission.
 */
@Mod(modid = Main.modid, name = "Power Boxes", version = "1.03.1", dependencies = "required-after:IC2;required-after:BuildCraft|Core;after:ComputerCraft")
@NetworkMod(channels = {"PBoxes"}, packetHandler = PacketHandler.class)
public class Main {

    public static final String modid = "PowerBoxes";
    public static final String channelName = "PBoxes";

    @Mod.Instance(modid)
    public static Main instance;

    public static int bcWrenchId = 0;

    public static CreativeTabs tabPowerBox = new CreativeTabs("tabPowerBox") {
        public ItemStack getIconItemStack() {
            return new ItemStack(Blocks.powerBox_block, 1, 0);
        }
    };

    public void doConfig(FMLPreInitializationEvent e) {
        Configuration config = new Configuration(e.getSuggestedConfigurationFile());
        config.load();

        Config.eu = config.get(Configuration.CATEGORY_GENERAL, "EU Conversion", Config.eu).getDouble(Config.eu);
        Config.mj = config.get(Configuration.CATEGORY_GENERAL, "MJ Conversion", Config.mj).getDouble(Config.mj);
        Config.powerBox_capacity = config.get(Configuration.CATEGORY_GENERAL, "Power Box Capacity", Config.powerBox_capacity).getInt(Config.powerBox_capacity);
        Config.unbreakable = config.get(Configuration.CATEGORY_GENERAL, "Indestructable Power Box", Config.unbreakable).getBoolean(Config.unbreakable);
        Config.powerBox_capacity_multiplier = config.get(Configuration.CATEGORY_GENERAL, "Capacity Upgrade Multiplier", Config.powerBox_capacity_multiplier).getInt(Config.powerBox_capacity_multiplier);

        Config.powerBox_blockId = config.get(Configuration.CATEGORY_BLOCK, "Power Box", Config.powerBox_blockId).getInt(Config.powerBox_blockId);

        Config.capacityUpgrade_itemId = config.get(Configuration.CATEGORY_ITEM, "Capacity Upgrade", Config.capacityUpgrade_itemId).getInt(Config.capacityUpgrade_itemId);
        Config.outputSpeedUpgrade_itemId = config.get(Configuration.CATEGORY_ITEM, "Output Speed Upgrade", Config.outputSpeedUpgrade_itemId).getInt(Config.outputSpeedUpgrade_itemId);

        config.save();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        doConfig(event);

        instance = this;
    }

    public void createBlocks() {
        Blocks.powerBox_block = new BlockPowerBox(Config.powerBox_blockId, Material.iron);
    }

    public void createItems() {
        Items.upgrade_Capacity = new ItemUpgrade_Capacity(Config.capacityUpgrade_itemId);
        Items.upgrade_OutputSpeed = new ItemUpgrade_OutputSpeed(Config.outputSpeedUpgrade_itemId);
    }

    public void registerBlocks() {
        GameRegistry.registerBlock(Blocks.powerBox_block, ItemBlockPowerBox.class, "PowerBox_Block");
    }

    public void registerItems() {
        GameRegistry.registerItem(Items.upgrade_Capacity, "Upgrades_Capacity");
        GameRegistry.registerItem(Items.upgrade_OutputSpeed, "Upgrades_OutputSpeed");
    }

    public void registerTiles() {
        GameRegistry.registerTileEntity(TileEntityPowerBox.class, "PowerBox_Tile");
    }

    public void addNames() {
        LanguageRegistry.addName(new ItemStack(Blocks.powerBox_block, 1, 0), "Power Box");

        LanguageRegistry.addName(Items.upgrade_Capacity, "Capacity Upgrade");
        LanguageRegistry.addName(Items.upgrade_OutputSpeed, "Output Speed Upgrade");

        LanguageRegistry.instance().addStringLocalization("itemGroup.tabPowerBox", "en_US", "Power Boxes");
    }

    public void addRecipes() {
        Item goldPipeId = BuildCraftTransport.pipePowerGold;
        Item woodPipeId = BuildCraftTransport.pipePowerWood;
        Item quartzPipeId = BuildCraftTransport.pipePowerQuartz;
        Item diamondPipeId = BuildCraftTransport.pipePowerDiamond;
        Item ironGearId = BuildCraftCore.ironGearItem;
        ItemStack mfeId = Ic2Items.mfeUnit;
        ItemStack cesuId = Ic2Items.cesuUnit;
        ItemStack elCircId = Ic2Items.electronicCircuit;
        ItemStack goldWire = Ic2Items.insulatedGoldCableItem;
        ItemStack glassFiber = Ic2Items.glassFiberCableItem;

        GameRegistry.addShapedRecipe(new ItemStack(Blocks.powerBox_block), new Object[] {"ABA", "CDE", "FFF", 'A', goldPipeId, 'B', woodPipeId, 'C', ironGearId, 'D', mfeId, 'E', elCircId, 'F', goldWire});

        GameRegistry.addShapedRecipe(new ItemStack(Items.upgrade_Capacity), new Object[] {" A ", "ABA", " A ", 'A', quartzPipeId, 'B', cesuId});

        GameRegistry.addShapedRecipe(new ItemStack(Items.upgrade_OutputSpeed), new Object[] {"DAD", "BCB", "DAD", 'A', Item.redstone, 'B', glassFiber, 'C', diamondPipeId, 'D', Block.blockRedstone});
    }


    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        createBlocks();
        createItems();

        registerBlocks();
        registerItems();
        registerTiles();

        addNames();

        NetworkRegistry.instance().registerGuiHandler(this, new GUIHandler());
    }

    public void findWrenchIds() {
        bcWrenchId = GameRegistry.findItem("BuildCraft|Core", "wrenchItem").itemID;
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        findWrenchIds();

        addRecipes();
    }
}