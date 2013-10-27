package core;

import block.BlockPowerBox;
import block.BlockPowerCable;
import block.ItemBlockPowerBox;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import item.ItemUpgrade;
import item.Items;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import tile.TileEntityPowerBox;
import tile.TileEntityPowerCable;

import javax.swing.*;

/**
 * Author: Lordmau5
 * Date: 21.08.13
 * Time: 16:10
 * You are not allowed to change this code,
 * nor publish it without my permission.
 */
@Mod(modid = Main.modid, name = "Power Boxes", version = "1.04", dependencies = "required-after:Forge@[9.11.1.917,);after:IC2;after:BuildCraft|Core;after:ComputerCraft")
@NetworkMod(channels = {"PBoxes"}, packetHandler = PacketHandler.class)
public class Main {

    public static final String modid = "PowerBoxes";
    public static final String channelName = "PBoxes";

    public static boolean BCSupplied = false;
    public static boolean ICSupplied = false;
    public static boolean CCSupplied = false;

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

        Config.upgrade_ItemId = config.get(Configuration.CATEGORY_ITEM, "Upgrades", Config.upgrade_ItemId).getInt(Config.upgrade_ItemId);

        config.save();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        doConfig(event);

        instance = this;
    }

    public void createBlocks() {
        Blocks.powerBox_block = new BlockPowerBox(Config.powerBox_blockId, Material.iron);
        Blocks.powerCable_block = new BlockPowerCable(Config.powerBox_blockId + 1, Material.iron);
    }

    public void createItems() {
        Items.upgrade_Item = new ItemUpgrade(Config.upgrade_ItemId);
    }

    public void registerBlocks() {
        GameRegistry.registerBlock(Blocks.powerBox_block, ItemBlockPowerBox.class, "PowerBox_Block");
        GameRegistry.registerBlock(Blocks.powerCable_block, "PowerCable_Block");
    }

    public void registerItems() {
        GameRegistry.registerItem(Items.upgrade_Item, "Upgrades");
    }

    public void registerTiles() {
        GameRegistry.registerTileEntity(TileEntityPowerBox.class, "PowerBox_Tile");
        GameRegistry.registerTileEntity(TileEntityPowerCable.class, "PowerCable_Tile");
    }

    public void addNames() {
        LanguageRegistry.addName(new ItemStack(Blocks.powerBox_block, 1, 0), "Power Box");
        LanguageRegistry.addName(new ItemStack(Blocks.powerCable_block, 1, 0), "Power Cable");

        Items.upgrade_Item.addNames();

        LanguageRegistry.instance().addStringLocalization("itemGroup.tabPowerBox", "en_US", "Power Boxes");
    }

    public void checkForMods() {
        BCSupplied = Loader.isModLoaded("BuildCraft|Transport");
        ICSupplied = Loader.isModLoaded("IC2");
        CCSupplied = Loader.isModLoaded("ComputerCraft");

        if(!BCSupplied && !ICSupplied) {
            if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
                JOptionPane optionPane = new JOptionPane();
                optionPane.setMessage("Power Boxes is useless without one of the corresponding mods (BuildCraft or IndustrialCraft2).\n" +
                        "Install atleast one of them, and the mod will have features!");
                optionPane.setMessageType(JOptionPane.WARNING_MESSAGE);
                JDialog dialog = optionPane.createDialog("Power Boxes needs mods!");
                dialog.setAlwaysOnTop(true);
                dialog.setVisible(true);
            }
        }
    }

    public void addRecipes() {
        //------------------------------------------------------
        //------- Power Box ------------------------------------

        Item goldKinesis = GameRegistry.findItem("BuildCraft|Transport", "item.buildcraftPipe.pipepowergold");
        Item woodKinesis = GameRegistry.findItem("BuildCraft|Transport", "item.buildcraftPipe.pipepowerwood");
        ItemStack diamondChipset = GameRegistry.findItemStack("BuildCraft|Silicon", "redstone_diamond_chipset", 1);

        ItemStack hvTransformer = ic2.api.item.Items.getItem("hvTransformer");
        ItemStack goldCable = ic2.api.item.Items.getItem("insulatedGoldCableItem");
        ItemStack energyCrystal = ic2.api.item.Items.getItem("energyCrystal");

        ItemStack glassFiber = ic2.api.item.Items.getItem("glassFiberCableItem");

        if(ICSupplied) {
            hvTransformer = ic2.api.item.Items.getItem("hvTransformer");
            goldCable = ic2.api.item.Items.getItem("insulatedGoldCableItem");
            energyCrystal = ic2.api.item.Items.getItem("energyCrystal");

            glassFiber = ic2.api.item.Items.getItem("glassFiberCableItem");
        }

        GameRegistry.addShapedRecipe(new ItemStack(Blocks.powerBox_block), new Object[]{"GWG", "HED", "CCC", 'G', goldKinesis, 'W', woodKinesis, 'H', hvTransformer, 'E', energyCrystal, 'D', diamondChipset, 'C', goldCable});

        GameRegistry.addShapedRecipe(new ItemStack(Items.upgrade_Item, 1, 0), new Object[] {" A ", "ABA", " A ", 'A', goldKinesis, 'B', energyCrystal});

        GameRegistry.addShapedRecipe(new ItemStack(Items.upgrade_Item, 1, 1), new Object[] {"DAD", "BCB", "DAD", 'A', Item.redstone, 'B', glassFiber, 'C', woodKinesis, 'D', Block.blockRedstone});

        //------------------------------------------------------
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
        if(BCSupplied)
            bcWrenchId = GameRegistry.findItem("BuildCraft|Core", "wrenchItem").itemID;
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        checkForMods();

        findWrenchIds();

        //addRecipes();
    }
}