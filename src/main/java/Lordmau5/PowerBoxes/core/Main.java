package Lordmau5.PowerBoxes.core;

import Lordmau5.PowerBoxes.block.*;
import Lordmau5.PowerBoxes.compatibility.buildCraft.BuildCraftCompatibility;
import Lordmau5.PowerBoxes.item.ItemLinkCard;
import Lordmau5.PowerBoxes.item.ItemUpgrade;
import Lordmau5.PowerBoxes.item.Items;
import Lordmau5.PowerBoxes.network.LinkBoxNetwork;
import Lordmau5.PowerBoxes.proxy.CommonProxy;
import Lordmau5.PowerBoxes.tile.TileEntityLinkBox;
import Lordmau5.PowerBoxes.tile.TileEntityPowerBox;
import Lordmau5.PowerBoxes.tile.TileEntityPowerCable;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.transport.IPipeTile;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import ic2.api.energy.tile.IEnergyConductor;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.tile.IEnergyStorage;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.Configuration;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lordmau5
 * Date: 21.08.13
 * Time: 16:10
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
@Mod(modid = Main.modid, name = "Power Boxes", dependencies = "required-after:Forge@[9.11.1.942,);after:IC2;after:BuildCraft|Core;after:ComputerCraft", useMetadata = true)
@NetworkMod(channels = {"PBoxes"}, packetHandler = PacketHandler.class)
public class Main {

    public static final String modid = "PowerBoxes";
    public static final String channelName = "PBoxes";

    public static boolean BCSupplied = false;
    public static boolean ICSupplied = false;
    public static boolean CCSupplied = false;

    @SidedProxy(clientSide = "Lordmau5.PowerBoxes.proxy.ClientProxy", serverSide = "Lordmau5.PowerBoxes.proxy.CommonProxy")
    public static CommonProxy commonProxy;

    public static BuildCraftCompatibility bcComp;

    public static LinkBoxNetwork linkBoxNetwork;

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
        Config.powerCable_blockId = config.get(Configuration.CATEGORY_BLOCK, "Power Cable", Config.powerCable_blockId).getInt(Config.powerCable_blockId);
        Config.linkBox_blockId = config.get(Configuration.CATEGORY_BLOCK, "Link Box", Config.linkBox_blockId).getInt(Config.linkBox_blockId);

        Config.upgrade_ItemId = config.get(Configuration.CATEGORY_ITEM, "Upgrades", Config.upgrade_ItemId).getInt(Config.upgrade_ItemId);
        Config.linkCard_ItemId = config.get(Configuration.CATEGORY_ITEM, "Link Card", Config.linkCard_ItemId).getInt(Config.linkCard_ItemId);

        config.save();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        doConfig(event);

        instance = this;
        linkBoxNetwork = new LinkBoxNetwork();
    }

    public void createBlocks() {
        Blocks.powerBox_block = new BlockPowerBox(Config.powerBox_blockId, Material.iron);
        Blocks.powerCable_block = new BlockPowerCable(Config.powerCable_blockId, Material.iron);
        Blocks.linkBox_block = new BlockLinkBox(Config.linkBox_blockId, Material.iron);
    }

    public void createItems() {
        Items.upgrade_Item = new ItemUpgrade(Config.upgrade_ItemId);
        Items.linkCard_Item = new ItemLinkCard(Config.linkCard_ItemId);
    }

    public void registerBlocks() {
        GameRegistry.registerBlock(Blocks.powerBox_block, ItemBlockPowerBox.class, "PowerBox_Block");
        GameRegistry.registerBlock(Blocks.powerCable_block, "PowerCable_Block");
        GameRegistry.registerBlock(Blocks.linkBox_block, "LinkBox_Block");
    }

    public void registerItems() {
        GameRegistry.registerItem(Items.upgrade_Item, "Upgrades");
        GameRegistry.registerItem(Items.linkCard_Item, "LinkCard");
    }

    public void registerTiles() {
        GameRegistry.registerTileEntity(TileEntityPowerBox.class, "PowerBox_Tile");
        GameRegistry.registerTileEntity(TileEntityPowerCable.class, "PowerCable_Tile");
        GameRegistry.registerTileEntity(TileEntityLinkBox.class, "LinkBox_Tile");
    }

    public void addNames() {
        LanguageRegistry.addName(Blocks.powerBox_block, "Power Box");
        LanguageRegistry.addName(Blocks.powerCable_block, "Power Cable");
        LanguageRegistry.addName(Blocks.linkBox_block, "Link Box");

        LanguageRegistry.addName(Items.linkCard_Item, "Link Card");

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
        if(BCSupplied)
            bcComp = new BuildCraftCompatibility();
    }

    public void addBCRecipies() {

    }

    public void addICRecipies() {
        Item goldKinesis = GameRegistry.findItem("BuildCraft|Transport", "item.buildcraftPipe.pipepowergold");
        Item woodKinesis = GameRegistry.findItem("BuildCraft|Transport", "item.buildcraftPipe.pipepowerwood");
        ItemStack diamondChipset = GameRegistry.findItemStack("BuildCraft|Silicon", "redstone_diamond_chipset", 1);

        ItemStack hvTransformer = ic2.api.item.Items.getItem("hvTransformer");
        ItemStack goldCable = ic2.api.item.Items.getItem("insulatedGoldCableItem");
        ItemStack energyCrystal = ic2.api.item.Items.getItem("energyCrystal");

        ItemStack glassFiber = ic2.api.item.Items.getItem("glassFiberCableItem");

        //------------------------------------------------------------------------------------------------------------------

        GameRegistry.addShapedRecipe(new ItemStack(Blocks.powerBox_block), new Object[]{"GWG", "HED", "CCC", 'G', goldKinesis, 'W', woodKinesis, 'H', hvTransformer, 'E', energyCrystal, 'D', diamondChipset, 'C', goldCable});

        GameRegistry.addShapedRecipe(new ItemStack(Items.upgrade_Item, 1, 0), new Object[] {" A ", "ABA", " A ", 'A', goldKinesis, 'B', energyCrystal});

        GameRegistry.addShapedRecipe(new ItemStack(Items.upgrade_Item, 1, 1), new Object[] {"DAD", "BCB", "DAD", 'A', Item.redstone, 'B', glassFiber, 'C', woodKinesis, 'D', Block.blockRedstone});

        GameRegistry.addShapedRecipe(new ItemStack(Blocks.powerCable_block, 16), new Object[] {" C ", "CPC", " C ", 'C', glassFiber, 'P', Blocks.powerBox_block});
    }

    public void addBCandICRecipies() {
        Item goldKinesis = GameRegistry.findItem("BuildCraft|Transport", "item.buildcraftPipe.pipepowergold");
        Item woodKinesis = GameRegistry.findItem("BuildCraft|Transport", "item.buildcraftPipe.pipepowerwood");
        ItemStack diamondChipset = GameRegistry.findItemStack("BuildCraft|Silicon", "redstone_diamond_chipset", 1);

        ItemStack hvTransformer = ic2.api.item.Items.getItem("hvTransformer");
        ItemStack goldCable = ic2.api.item.Items.getItem("insulatedGoldCableItem");
        ItemStack energyCrystal = ic2.api.item.Items.getItem("energyCrystal");

        ItemStack glassFiber = ic2.api.item.Items.getItem("glassFiberCableItem");

        //------------------------------------------------------------------------------------------------------------------

        GameRegistry.addShapedRecipe(new ItemStack(Blocks.powerBox_block), new Object[]{"GWG", "HED", "CCC", 'G', goldKinesis, 'W', woodKinesis, 'H', hvTransformer, 'E', energyCrystal, 'D', diamondChipset, 'C', goldCable});

        GameRegistry.addShapedRecipe(new ItemStack(Items.upgrade_Item, 1, 0), new Object[] {" A ", "ABA", " A ", 'A', goldKinesis, 'B', energyCrystal});

        GameRegistry.addShapedRecipe(new ItemStack(Items.upgrade_Item, 1, 1), new Object[] {"DAD", "BCB", "DAD", 'A', Item.redstone, 'B', glassFiber, 'C', woodKinesis, 'D', Block.blockRedstone});

        GameRegistry.addShapedRecipe(new ItemStack(Blocks.powerCable_block, 16), new Object[] {" C ", "CPC", " C ", 'C', glassFiber, 'P', Blocks.powerBox_block});
    }

    public void addRecipes() {
        //------------------------------------------------------
        //------- Power Box ------------------------------------

        if(ICSupplied && BCSupplied)
            addBCandICRecipies();
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

        commonProxy.initRendering();
    }

    private static List<Integer> wrenches = new ArrayList<Integer>();
    public static boolean isWrench(int itemID) {
        for(Integer wInt : wrenches)
            if(wInt == itemID)
                return true;
        return false;
    }

    public void findWrenchIds() {
        if(BCSupplied)
            wrenches.add(GameRegistry.findItem("BuildCraft|Core", "wrenchItem").itemID);
        if(ICSupplied) {
            wrenches.add(ic2.api.item.Items.getItem("wrench").itemID);
            wrenches.add(ic2.api.item.Items.getItem("electricWrench").itemID);
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        checkForMods();

        findWrenchIds();

        addRecipes();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        linkBoxNetwork.initNetwork();
    }

    private static String[] invalidTiles_Classes = {"TileEntityTeleporter", "TileCapacitorBank", "TileConduitBundle", "PipeTile"};

    private static String[] validTiles_Classes = {"TileEntityCompactSolar"};
    private static String[] validTiles_Superclasses = {"TileEntityCompactSolar"};
    public static boolean isInvalidPowerTile(TileEntity tile) {
        Class iClass = tile.getClass();

        for(String cClass : invalidTiles_Classes) {
            if(iClass.getSimpleName().equalsIgnoreCase(cClass))
                return true;
        }
        return false;
    }

    public static boolean checkForModTile(TileEntity tile) {
        if(BCSupplied) {
            if(tile instanceof IPipeTile)
                return false;

            if(tile instanceof IPowerReceptor)
                return true;
        }
        if(ICSupplied) {
            if(tile instanceof IEnergyConductor)
                return false;

            if(tile instanceof IEnergySink || tile instanceof IEnergyStorage || tile instanceof IEnergySource)
                return true;
        }

        return false;
    }

    public static boolean isValidPowerTile(TileEntity tile) {

        if(isInvalidPowerTile(tile))
            return false;
        //---------------------------------------------------------------------------
        Class iClass = tile.getClass();

        for(String cClass : validTiles_Classes) {
            if(iClass.getSimpleName().equalsIgnoreCase(cClass))
                return true;
        }

        Class superClass = iClass.getSuperclass();
        for(String sClass : validTiles_Superclasses) {
            if(superClass.getSimpleName().equalsIgnoreCase(sClass))
                return true;
        }

        if(checkForModTile(tile))
            return true;

        return false;
    }
}