package tamagotti.init;


import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import tamagotti.TamagottiMod;
import tamagotti.init.blocks.block.Akari;
import tamagotti.init.blocks.block.Andon;
import tamagotti.init.blocks.block.Bamboo;
import tamagotti.init.blocks.block.BambooW;
import tamagotti.init.blocks.block.BlockTHarvester;
import tamagotti.init.blocks.block.CopperGlass;
import tamagotti.init.blocks.block.CopperGlassPane;
import tamagotti.init.blocks.block.Fluolight;
import tamagotti.init.blocks.block.GTable;
import tamagotti.init.blocks.block.Jumper;
import tamagotti.init.blocks.block.Kogen;
import tamagotti.init.blocks.block.Lanp;
import tamagotti.init.blocks.block.LogChair;
import tamagotti.init.blocks.block.MobSlider;
import tamagotti.init.blocks.block.SakuraCarpet;
import tamagotti.init.blocks.block.SakuraDoor;
import tamagotti.init.blocks.block.SakuraLeave;
import tamagotti.init.blocks.block.SakuraLog;
import tamagotti.init.blocks.block.SakuraPlank;
import tamagotti.init.blocks.block.SakuraSapring;
import tamagotti.init.blocks.block.SakuraSlab;
import tamagotti.init.blocks.block.SakuraStairs;
import tamagotti.init.blocks.block.TBeacon;
import tamagotti.init.blocks.block.TChair;
import tamagotti.init.blocks.block.TFence;
import tamagotti.init.blocks.block.TIron;
import tamagotti.init.blocks.block.TOre;
import tamagotti.init.blocks.block.TOre_N;
import tamagotti.init.blocks.block.TPot;
import tamagotti.init.blocks.block.TSpawner;
import tamagotti.init.blocks.block.TTable;
import tamagotti.init.blocks.block.W_Light;
import tamagotti.init.blocks.block.WindMill;
import tamagotti.init.blocks.chest.dchest.DChest;
import tamagotti.init.blocks.chest.gc.GC;
import tamagotti.init.blocks.chest.sw.SW;
import tamagotti.init.blocks.chest.thopper.THopper;
import tamagotti.init.blocks.chest.wadansu.WadansuChest;
import tamagotti.init.blocks.chest.ychest.YChest;
import tamagotti.init.blocks.crops.BlockBamboo;
import tamagotti.init.blocks.crops.BlockBambooS;
import tamagotti.init.blocks.crops.BlockOre;
import tamagotti.init.blocks.crops.BlockRice;
import tamagotti.init.blocks.crops.BlockTPlant_3;
import tamagotti.init.blocks.crops.BlockTPlant_4;
import tamagotti.init.blocks.crops.BlockTPlant_5;
import tamagotti.init.blocks.crops.BlockTRoom;
import tamagotti.init.blocks.furnace.DFurnace;
import tamagotti.init.blocks.furnace.FryPan;
import tamagotti.init.blocks.furnace.Pot;

public class BlockInit {

    public static Block copperore,copperblock;
    public static Block copperglass,copperlightglass;
    public static Block redberylore,redberylblock;
    public static Block fluoriteore,fluoriteblock;
    public static Block smokyquartzore,smokyquartzblock;
    public static Block silverore, silverblock;
    public static Block rubyore, rubyblock;
    public static Block sakuralog, sakuraleave, sakurasapling, sakuracarpet;
    public static Block sakuradoor0, sakuradoor1, sakuradoor2;
    public static Block sakuraplanks0, sakuraplanks1;
    public static Block sakura_slab_0, sakura_slab_1;
    public static Block sakura_stairs_0, sakura_stairs_1;
    public static Block bamboo, bambooplanks,bamboo_s, bamboowall, bamboob;

    public static Block riceblock,azukiblock,canolablock,soyblock,sobablock,e_plantblock,strawberryblock;
    public static Block tomatoblock,onionblock,cabbageblock,lettuceblock,negiblock, j_radishblock;
    public static Block ironblock,goldblock,lapisblock,redblock,quablock, starrosequartzore, srqblock;

    public static Block pot_0,pot_1,frypan,frypan_1,potneo_0,potneo_1, potcustom_0, potcustom_1, tdisplay_0, tdisplay_1;
    public static Block thopper,thopper_n, thopper_f, thopper_s;
    public static Block tbeacon, ychest, dchest, dfurnace, gc;

    public static Block lamp, fluolight, tpot, tfence;

    public static Block andon, kago, wadansu, cupboard;
    public static Block tamagottichest, tamazonchest, swchest, tbshelfchest;
    public static Block kogen,kogen_s,w_light,akari;
    public static Block cw_glass,cwlight_glass;
    public static Block mob_jumper, mob_slider, p_jumper, overclock;
    public static Block troom;
    public static Block tchair_down, tchair_top, ttable, gtable;
    public static Block tspawner_s, tspawner_z, tspawner_c, tspawner_ws, tspawner_wi, tspawner_b, tspawner_lce;

    public static Block t_ore, t_neoore, t_hore;

    public static Block skr_chair;

    public static Block linkteleport;

    public static Block windmill, windmill_l;

    public static Block tplanter, tharvester, tfelling;

    public static List<Block> blockList = new ArrayList();
    public static List<Block> noTabList = new ArrayList();

    public static void init() {

        copperglass = new CopperGlass("copperglass", 0);
        copperlightglass = new CopperGlass("copperlightglass", 1);
        cw_glass = new CopperGlassPane("cw_glass", 0);
        cwlight_glass = new CopperGlassPane("cwlight_glass", 1);
        lamp = new Lanp("lamp");

        fluolight = new Fluolight("fluolight");

        tpot = new TPot("tpot");
        tfence = new TFence("tfence");

        copperore = new TOre_N("copperore", 2.0F, 4.0F, 1);
        copperblock = new TIron("copperblock", 2.4F, 4.0F, 1, 0);
        redberylore = new TOre("redberylore", 4.5F, 30.0F, 1, 0);
        redberylblock = new TIron("redberylblock", 3.5F, 30.0F, 1, 1);
        fluoriteore = new TOre("fluoriteore", 4.5F, 30.0F, 1, 2);
        fluoriteblock = new TIron("fluoriteblock", 3.5F, 30.0F, 1, 1);
        smokyquartzore = new TOre("smokyquartzore", 4.5F, 30.0F, 1, 1);
        smokyquartzblock = new TIron("smokyquartzblock", 3.5F, 30.0F, 1, 1);
        silverore = new TOre_N("silverore", 2.0F, 4.0F, 1);
        silverblock = new TIron("silverblock", 2.0F, 4.0F, 1, 0);
        rubyore = new TOre("rubyore", 4.5F, 30.0F, 1, 5);
        rubyblock = new TIron("rubyblock", 4.5F, 30.0F, 1, 0);
        starrosequartzore = new TOre("starrosequartzore", 4.5F, 30.0F, 1, 6);
        srqblock = new TIron("srqblock",3.5F,30.0F,1, 1);
        t_ore = new TOre("t_ore", 4.5F, 30.0F, 1, 3);
        t_neoore = new TOre("t_neoore", 4.5F, 30.0F, 1, 4);
        t_hore = new TOre("t_hore", 4.5F, 30.0F, 1, 7);

        sakuralog = new SakuraLog("sakuralog");
        sakuraleave = new SakuraLeave("sakuraleave");
        sakurasapling = new SakuraSapring("sakurasapling");
        sakuracarpet = new SakuraCarpet("sakuracarpet");
        sakuradoor0 = new SakuraDoor("sakuradoor0",0);
        sakuradoor1 = new SakuraDoor("sakuradoor1",1);
        sakuradoor2 = new SakuraDoor("sakuradoor2",2);
        sakuraplanks0 = new SakuraPlank("sakuraplanks0");
        sakuraplanks1 = new SakuraPlank("sakuraplanks1");
        sakura_slab_0 = new SakuraSlab("sakura_slab_0");
        sakura_slab_1 = new SakuraSlab("sakura_slab_1");
        sakura_stairs_0 = new SakuraStairs("sakura_stairs_0", sakuraplanks0.getDefaultState());
        sakura_stairs_1 = new SakuraStairs("sakura_stairs_1", sakuraplanks1.getDefaultState());
        bamboo = new BlockBamboo("bamboo");
        bambooplanks = new SakuraPlank("bambooplanks");
        bamboo_s = new BlockBambooS("bamboo_s");
        bamboowall = new BambooW("bamboowall");
        bamboob = new Bamboo("bamboob");

        tchair_down = new TChair("tchair");
        tchair_top = new TChair("tchair_top");
        ttable = new TTable("ttable");
        gtable = new GTable("gtable");

        pot_0 = new Pot("pot_0", 0, blockList);
        pot_1 = new Pot("pot_1", 0, noTabList);
        potneo_0 = new Pot("potneo_0", 1, blockList);
        potneo_1 = new Pot("potneo_1", 1, noTabList);
        potcustom_0 = new Pot("potcustom_0", 2, blockList);
        potcustom_1 = new Pot("potcustom_1", 2, noTabList);
        tdisplay_0 = new Pot("tdisplay_0", 3, blockList);
        tdisplay_1 = new Pot("tdisplay_1", 3, noTabList);
        frypan = new FryPan(false, "frypan", blockList);
        frypan_1 = new FryPan(true, "frypan_1", noTabList);

        akari = new Akari("akari");
        andon = new Andon("andon");
        cupboard = new WadansuChest("cupboard", 0, SoundType.WOOD);
        gc = new GC("gc");
        kago = new WadansuChest("kago", 1, SoundType.WOOD);
        kogen = new Kogen("kogen", 0);
        kogen_s = new Kogen("kogen_s", 1);							// ===================
        mob_jumper = new Jumper("mob_jumper", 2, 16, 1, 0, 0);
        mob_slider = new MobSlider("mob_slider", 0);
        p_jumper = new MobSlider("p_jumper", 1);
        overclock = new Jumper("overclock", 2, 16, 1, 0.4F, 3);
        swchest = new SW("swchest");
        tbshelfchest = new WadansuChest("tbshelf", 4, SoundType.WOOD);
        tamagottichest = new WadansuChest("tamagottichest", 2, SoundType.METAL);
        tamazonchest = new WadansuChest("tamazonchest", 3, SoundType.CLOTH);
        tbeacon = new TBeacon("tbeacon");
        thopper = new THopper("thopper", 0);
        thopper_n = new THopper("thopper_n", 1);
        thopper_f = new THopper("thopper_f", 2);
        thopper_s = new THopper("thopper_s", 3);
        w_light = new W_Light("w_light");
        wadansu = new WadansuChest("wadansu", 0, SoundType.WOOD);
        ychest = new YChest("ychest");
        dchest = new DChest("dchest");
        dfurnace = new DFurnace("dfurnace");

        tspawner_s = new TSpawner("tspawner_s");
        tspawner_z = new TSpawner("tspawner_z");
        tspawner_c = new TSpawner("tspawner_c");
        tspawner_wi = new TSpawner("tspawner_wi");
        tspawner_ws = new TSpawner("tspawner_ws");
        tspawner_b = new TSpawner("tspawner_b");
        tspawner_lce = new Jumper("tspawner_lce", 2, 16, 1, 0, 4);

        tharvester = new BlockTHarvester("tharvester", 0);
        tplanter = new BlockTHarvester("tplanter", 1);
        tfelling = new BlockTHarvester("tfelling", 2);

        troom = new BlockTRoom("troom");
        riceblock = new BlockRice("riceblock");
        j_radishblock = new BlockTPlant_3("j_radishblock", 0);
        e_plantblock = new BlockTPlant_3("e_plantblock", 1);
        lettuceblock = new BlockTPlant_3("lettuceblock", 2);
        negiblock = new BlockTPlant_3("negiblock", 3);
        cabbageblock = new BlockTPlant_3("cabbageblock", 4);
        onionblock = new BlockTPlant_4("onionblock", 0);
        sobablock = new BlockTPlant_4("sobablock", 1);
        soyblock = new BlockTPlant_4("soyblock", 2);
        tomatoblock = new BlockTPlant_4("tomatoblock", 3);
        strawberryblock = new BlockTPlant_4("strawberryblock", 4);
        azukiblock = new BlockTPlant_5("azukiblock", 0);
        canolablock = new BlockTPlant_5("canolablock", 1);
        ironblock = new BlockOre("ironblock", 0);
        goldblock = new BlockOre("goldblock", 1);
        lapisblock = new BlockOre("lapisblock", 2);
        redblock = new BlockOre("redblock", 3);
        quablock = new BlockOre("quablock", 4);

        skr_chair = new LogChair("sakuralog_chair");

        linkteleport = new Jumper("linktp", 2, 16, 1, 0, 1);

        windmill = new WindMill("windmill", 0);
        windmill_l = new WindMill("windmill_l", 1);
    }

    public static void register() {

    	blockList.remove(blockList.indexOf(kogen_s));

    	for (Block block : blockList) {
            registerBlock(block, 0);
    	}

    	noTabList.add(kogen_s);

    	for (Block block : noTabList) {
            registerBlock(block, 1);
    	}
    }

    public static void registerBlock(Block block, int data) {
        ForgeRegistries.BLOCKS.register(block);
        if(data == 0) { block.setCreativeTab(TamagottiMod.tamagottiTab); }
        ItemBlock item = new ItemBlock(block);
        item.setRegistryName(block.getRegistryName());
        ForgeRegistries.ITEMS.register(item);
        if(FMLCommonHandler.instance().getSide()==Side.CLIENT)ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
    }
}
