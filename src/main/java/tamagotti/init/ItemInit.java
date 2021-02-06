package tamagotti.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import tamagotti.TamagottiMod;
import tamagotti.init.base.BaseReturnItem;
import tamagotti.init.items.armor.AArmor;
import tamagotti.init.items.armor.BArmor;
import tamagotti.init.items.armor.CopperArmor;
import tamagotti.init.items.armor.FArmor;
import tamagotti.init.items.armor.MArmor;
import tamagotti.init.items.armor.RedberylArmor;
import tamagotti.init.items.armor.TArmor;
import tamagotti.init.items.armor.YAArmor;
import tamagotti.init.items.armor.YArmor;
import tamagotti.init.items.tool.bamboo.BA_Arrow;
import tamagotti.init.items.tool.bamboo.BA_Bow;
import tamagotti.init.items.tool.bettyu.BAxe;
import tamagotti.init.items.tool.bettyu.BBow;
import tamagotti.init.items.tool.bettyu.BGadget;
import tamagotti.init.items.tool.bettyu.BManu;
import tamagotti.init.items.tool.bettyu.BNova;
import tamagotti.init.items.tool.bettyu.BPick;
import tamagotti.init.items.tool.bettyu.BShield;
import tamagotti.init.items.tool.bettyu.BShotter;
import tamagotti.init.items.tool.bettyu.BShovel;
import tamagotti.init.items.tool.bettyu.BSpinner;
import tamagotti.init.items.tool.bettyu.BSword;
import tamagotti.init.items.tool.blade.ABlade_0;
import tamagotti.init.items.tool.blade.MSword;
import tamagotti.init.items.tool.blade.TABlade_0;
import tamagotti.init.items.tool.blade.TABlade_1;
import tamagotti.init.items.tool.blade.TABlade_2;
import tamagotti.init.items.tool.blade.YABlade_3;
import tamagotti.init.items.tool.blade.voiceroid.ARod;
import tamagotti.init.items.tool.blade.voiceroid.AkariStar;
import tamagotti.init.items.tool.blade.voiceroid.AncientSword;
import tamagotti.init.items.tool.blade.voiceroid.AncientSwordRemodel;
import tamagotti.init.items.tool.blade.voiceroid.AonoKatana;
import tamagotti.init.items.tool.blade.voiceroid.GreatSpear;
import tamagotti.init.items.tool.blade.voiceroid.KiritanHo;
import tamagotti.init.items.tool.blade.voiceroid.SeikaRod;
import tamagotti.init.items.tool.blade.voiceroid.ZundaBow;
import tamagotti.init.items.tool.cook.Knife;
import tamagotti.init.items.tool.cook.Riter;
import tamagotti.init.items.tool.cook.WCup;
import tamagotti.init.items.tool.extend.ExAxe;
import tamagotti.init.items.tool.extend.ExItem;
import tamagotti.init.items.tool.extend.ExPick;
import tamagotti.init.items.tool.extend.ExRush;
import tamagotti.init.items.tool.extend.ExShovel;
import tamagotti.init.items.tool.extend.ExSword;
import tamagotti.init.items.tool.hero.HBlaster;
import tamagotti.init.items.tool.hero.HCharger;
import tamagotti.init.items.tool.hheld.H_Farm;
import tamagotti.init.items.tool.hheld.H_House;
import tamagotti.init.items.tool.hheld.H_House_1;
import tamagotti.init.items.tool.ore.ruby.RubyAxe;
import tamagotti.init.items.tool.ore.ruby.RubyBow;
import tamagotti.init.items.tool.ore.ruby.RubyHammer;
import tamagotti.init.items.tool.ore.ruby.RubyPick;
import tamagotti.init.items.tool.ore.ruby.RubyShovel;
import tamagotti.init.items.tool.ore.ruby.RubyWand;
import tamagotti.init.items.tool.ore.silver.SShield;
import tamagotti.init.items.tool.ore.silver.SilverHammer;
import tamagotti.init.items.tool.ore.smoky.SmokyAxe;
import tamagotti.init.items.tool.ore.smoky.SmokyPick;
import tamagotti.init.items.tool.ore.smoky.SmokyShovel;
import tamagotti.init.items.tool.ore.smoky.SmokySickle;
import tamagotti.init.items.tool.ore.smoky.SmokySword;
import tamagotti.init.items.tool.smlifle.MagiaRifle;
import tamagotti.init.items.tool.tamagotti.TAmulet;
import tamagotti.init.items.tool.tamagotti.TAxe;
import tamagotti.init.items.tool.tamagotti.TBag;
import tamagotti.init.items.tool.tamagotti.TBlaster;
import tamagotti.init.items.tool.tamagotti.TBook;
import tamagotti.init.items.tool.tamagotti.TBookNeo;
import tamagotti.init.items.tool.tamagotti.TContract;
import tamagotti.init.items.tool.tamagotti.TCrossBow;
import tamagotti.init.items.tool.tamagotti.TCushion;
import tamagotti.init.items.tool.tamagotti.TEgg;
import tamagotti.init.items.tool.tamagotti.TFood;
import tamagotti.init.items.tool.tamagotti.TFoodItem;
import tamagotti.init.items.tool.tamagotti.TFuel;
import tamagotti.init.items.tool.tamagotti.TGlove;
import tamagotti.init.items.tool.tamagotti.THalberd;
import tamagotti.init.items.tool.tamagotti.THammer;
import tamagotti.init.items.tool.tamagotti.TIDoor;
import tamagotti.init.items.tool.tamagotti.TItem;
import tamagotti.init.items.tool.tamagotti.TLink;
import tamagotti.init.items.tool.tamagotti.TParasol;
import tamagotti.init.items.tool.tamagotti.TPendant;
import tamagotti.init.items.tool.tamagotti.TPick;
import tamagotti.init.items.tool.tamagotti.TRepair;
import tamagotti.init.items.tool.tamagotti.TRush;
import tamagotti.init.items.tool.tamagotti.TSail;
import tamagotti.init.items.tool.tamagotti.TSeed;
import tamagotti.init.items.tool.tamagotti.TSeedFood;
import tamagotti.init.items.tool.tamagotti.TShelter;
import tamagotti.init.items.tool.tamagotti.TShield;
import tamagotti.init.items.tool.tamagotti.TShovel;
import tamagotti.init.items.tool.tamagotti.TSpinner;
import tamagotti.init.items.tool.tamagotti.TSword;
import tamagotti.init.items.tool.wand.RedberylWand;
import tamagotti.init.items.tool.wand.TWand;
import tamagotti.init.items.tool.xtool.XAxe;
import tamagotti.init.items.tool.xtool.XPick;
import tamagotti.init.items.tool.xtool.XShovel;
import tamagotti.init.items.tool.xtool.XSword;
import tamagotti.init.items.tool.yukari.YRitter;
import tamagotti.init.items.tool.yukari.YSweeper;
import tamagotti.init.items.tool.yukari.YSweeperC;

public class ItemInit {

    public static Item tamagotti, tamagottineo, tamagotticustom, tamagottimadder, tamagottirepair, tamagottilink, h_tama;
    public static Item tamagottirush, tamagottirushneo, tamagottirushcustom, tamagottirushmadder,tamagottirushrepair, tamagottirushlink;
    public static Item tamagotticollaboration, tamagottisorella, tamagottinecro;

    public static Item tamagottishovel, tamagottiaxe, tamagottipickaxe, tamagottisword;
    public static Item tamagottishovelneo, tamagottiaxeneo, tamagottipickaxeneo, tamagottiswordneo;
    public static Item tamagottishovelcustom, tamagottiaxecustom, tamagottipickaxecustom, tamagottiswordcustom;
    public static Item tamagottishovelmadder, tamagottiaxemadder, tamagottipickaxemadder, tamagottiswordmadder;

    public static Item tamagottiakaneblade_0, tamagottiakaneblade_1, tamagottiakaneblade_2, tamagottiakaneblade_3, tamagottiakaneblade_4, ta_blade_5;
    public static Item akaneblade, akanebladezero, akanebladeinfinity, yukarisword, yukaakasword, yukaakablade3;
    public static Item ancientswordreplica, zundabow, greatelectricspear, aononinjato, kiritanho, bondstar, seikarod, akanerod, zundaarrow;
    public static Item ancientswordreremodel;
    public static Item akanecrystal, yukaricrystal, akanepearl, yukaripearl, madderstar;

    public static Item akane_helmet, akane_chestplate, akane_legginsgs, akane_boots;
    public static Item yukari_helmet, yukari_chestplate, yukari_leggings, yukari_boots;
    public static Item copper_helmet, copper_chestplate, copper_legginsgs, copper_boots;
    public static Item fl_helmet, fl_chest, fl_leg, fl_boot;
    public static Item redberyl_helmet, redberyl_chestplate, redberyl_legginsgs, redberyl_boots;
    public static Item bettyu_helmet, bettyu_chestplate, bettyu_legginsgs, bettyu_boots;
    public static Item t_helmet, t_chestplate, t_legginsgs, t_boots;
    public static Item tc_helmet, tc_chestplate, tc_legginsgs, tc_boots;
    public static Item ya_helmet, ya_chestplate, ya_legginsgs, ya_boots;

    public static Item copperaxe, copperpickaxe, coppersword, coppershovel;
    public static Item fluoritepickaxe, fluoriteaxe, fluoriteshovel, fluoritesword, fluoritesickle;
    public static Item smokyaxe, smokypickaxe, smokysword, smokyshovel, smokysickle;
    public static Item silverpickaxe, silveraxe, silvershovel, silversword, silverhammer, silvershield;
    public static Item rubyaxe, rubyshovel, rubypickaxe, rubyhammer, rubysword, rubybow, rubyshield, rubywand;
    public static Item copperingot, redberyl, fluorite, smokyquartz;

    public static Item machinesword, m_boot;

    public static Item bettyubook, bettyupowder, bettyuball, bettyustick, bettyustring, tamagottibettyu, bettyurush, bettyuingot, bettyucoal, bettyubcoal;
    public static Item bettyunova, bettyugadget, bettyuspinner, bettyuscorp, bettyumaneuver, bettyushotter, repairspinner;
    public static Item bettyupickaxe, bettyushovel, bettyusword, bettyuaxe, bettyubow, bettyushield;
    public static Item tamagottiwand,redberylwand,magicwand, magiafluxwand, tamagottiwandrepair, b_tamagotti_book, tamagotti_book_neo;

    public static Item tamagottishelter, tamagottisheltersorella, tamagottiblaster, tamagottiblastercustom, tblasternecro, tamagottispinner, tamagottispinnercustom;
    public static Item yukarrittascope, yukarrittascopecustom, yukaribarrel,yukaristock,yukaritank,yukariscorp, yukarisweeper, yukarisweepercustom, yukarishotter, yukarishotter_co;
    public static Item heroblaster, herocharger;

    public static Item kitchenknife, riter,yagen;
    public static Item riceseed, azukiseed, canolaseed, soyseed, cabbageseed, tomato,onion, lettuceseed, sobaseed, negiseed;
    public static Item sashimi,hugusashi, applepie, unknown_meat, tamagotti_sand;
    public static Item rice,sabaonigiri, shakeonigiri, tamagottirice, mochi, yakimochi, kinakomochi;
    public static Item lettuce, negi, cabbage,strawberry;
    public static Item ohagi, oshiruko;
    public static Item edamame, zunda, zuntamagotti;
    public static Item canolaoil, minioiltank, ine;
    public static Item e_plant,e_plantseed, yakinasu, j_radish, j_radishseed;
    public static Item nc_omurice, omurice, salad, sobapowder, soba, udon;
    public static Item watercup, salt, flowerpowder, cheese, butter, whipping_cream;
    public static Item miso, soysoup, malt,tofu, soymilk, botanical_rennet, nc_soysoup, kinako;
    public static Item natto, natto_rice;
    public static Item pizza_cloth, nc_pizza, pizza, nc_gratin, gratin;
    public static Item nc_hotcake, hotcake;
    public static Item nc_toast, toast, buttertoast;
    public static Item nc_kiritanpo, kiritanpo;
    public static Item nc_pork_cutlet ,pork_cutlet;
    public static Item nc_df_chicken, df_chicken;
    public static Item nc_hamburger_steak, hamburger_steak;
    public static Item nc_h_udon, nc_h_soba;
    public static Item nc_meat_bun, meat_bun;
    public static Item tamagottibull, breadcrumbs, dasi;
    public static Item h_udon, h_soba;
    public static Item purin, hot_cocoa, ice_choco, strawberry_jam, jam_toast;
    public static Item takikomigohan;
    public static Item nc_pork_soup, pork_soup;
    public static Item nc_oyakodon, oyakodon;
    public static Item ce_tomato;
    public static Item w_strawberries, ice_strawberries;
    public static Item shrimp, nc_f_shrimp, f_shrimp, yakiebi;
    public static Item ironseed, goldseed, lapisseed, redseed, quartzseed;
    public static Item milk_pack;

    public static Item tegg_c, tegg_b , tegg_p, tegg_s, tegg_v;
    public static Item hheld_farm, hheld_house, hheld_house_1;
    public static Item telepo_book;
    public static Item silveringot, ruby, starrosequartz;
    public static Item y_pendant, rack_fuel;

    public static Item bambooi, bamboo_charcoal, cb_charcoal, bamboobow, triplets_bamboobow, bambooarrow, re_bambooarrow;

    public static Item tsail, tsail_n, tcrossbow, tparasol, tshield, tshieldc, thalberd, thammer;
    public static Item tamulet, trepairamulet;

    public static Item srq_ex, t_ex, tpick_ex, tsword_ex, taxe_ex, tshovel_ex, trush_ex, tshield_ex;
    public static Item xpick, xaxe, xshovel, xsword;

    public static Item sakuradoori_0, sakuradoori_1, sakuradoori_2;

    public static Item tcushion_a, tcushion_y;
    public static Item bspawner;

    public static Item tcrystal, tbag, tglove;

    public static Item tcontract, zoma_contract, stalo_contract, tchicken_contract;

    public static Item gyoza_tane, gyoza;

    public static Item aether_rifle;

    public static List<Item> itemList = new ArrayList<Item>();
    public static List<Item> foodList = new ArrayList<Item>();

    public static void init() {

        sakuradoori_0 = new TIDoor("sakuradoor_0", BlockInit.sakuradoor0);
        sakuradoori_1 = new TIDoor("sakuradoor_1", BlockInit.sakuradoor1);
        sakuradoori_2 = new TIDoor("sakuradoor_2", BlockInit.sakuradoor2);

    	bettyu_helmet = new BArmor("bettyu_helmet", 1, EntityEquipmentSlot.HEAD, 0);
    	bettyu_chestplate = new BArmor("bettyu_chestplate", 1, EntityEquipmentSlot.CHEST, 1);
    	bettyu_legginsgs = new BArmor("bettyu_legginsgs", 2, EntityEquipmentSlot.LEGS, 2);
    	bettyu_boots = new BArmor("bettyu_boots", 1, EntityEquipmentSlot.FEET, 3);
    	bettyunova = new BNova("bettyunova", 0);
        bettyuscorp = new BNova("bettyuscorp", 1);
        bettyugadget = new BGadget("bettyugadget");
        bettyumaneuver = new BManu("bettyumaneuver");
        bettyuspinner = new BSpinner("bettyuspinner", 0);
        repairspinner = new BSpinner("repairspinner", 1);
        bettyushotter = new BShotter("bettyushotter");
		bettyurush = new TRush("bettyurush", 6);
		bettyubook = new TBook("bettyubook");
    	bettyuball = new TItem("bettyuball");
    	bettyustick = new TItem("bettyustick");
    	bettyuingot = new TItem("bettyuingot");
    	bettyupowder = new TItem("bettyupowder");
    	bettyustring = new TItem("bettyustring");
    	bettyucoal = new TFuel("bettyucoal", 12800);
    	bettyubcoal = new TFuel("bettyubcoal", 204800);
    	tamagottibettyu = new TItem("tamagottibettyu");
    	bettyusword = new BSword("bettyusword", EnumHelper.addToolMaterial("bettyusword", 1, 1562, 8.5F, 3, 16));
    	bettyushovel = new BShovel("bettyushovel", EnumHelper.addToolMaterial("bettyushovel", 3, 1562, 8, 0, 16));
    	bettyupickaxe = new BPick("bettyupickaxe", EnumHelper.addToolMaterial("bettyupickaxe", 3, 1562, 8, 2, 16));
    	bettyuaxe = new BAxe("bettyuaxe", EnumHelper.addToolMaterial("bettyuaxe", 3, 1562, 16, 2, 16),3,-3f);
        bettyubow = new BBow("bettyubow");
        bettyushield = new BShield("bshield");
    	tamagottiwand = new TWand("tamagottiwand", EnumHelper.addToolMaterial("tamagottiwand", 3, 1024, 8.5F, 6, 16), 0);
    	tamagottiwandrepair = new TWand("tamagottiwandrepair",EnumHelper.addToolMaterial("tamagottiwandrepair", 5, 1024, 8.5F, 8, 16), 1);

        redberylwand = new RedberylWand("redberylwand",EnumHelper.addToolMaterial("redberylwand", 3, 1024, 7.5F, 7, 32), 2, 1, 3000, 2);
    	magicwand = new RedberylWand("magicwand",EnumHelper.addToolMaterial("magicwand", 3, 2048, 7.5F, 10, 64), 5, 2, 15000, 4);
    	magiafluxwand = new RedberylWand("magiafluxwand",EnumHelper.addToolMaterial("magiafluxwand", 5, 4096, 9F, 16, 64), 17, 3, 75000, 8);

    	aether_rifle = new MagiaRifle("aether_rifle", 2, 10000, 4);

		tamagottirush = new TRush("tamagottirush", 0);
		tamagottirushneo = new TRush("tamagottirushneo", 1);
		tamagottirushcustom = new TRush("tamagottirushcustom", 2);
		tamagottirushlink = new TRush("tamagottirushlink", 3);
		tamagottirushmadder = new TRush("tamagottirushmadder", 4);
		tamagottirushrepair = new TRush("tamagottirushrepair", 5);

		//備忘メモ：アイテム名　=new 防具アイテムクラス(防具のスロット)、("アイテム名")、("アイテム名")
		t_helmet = new TArmor("t_helmet", 1, EntityEquipmentSlot.HEAD, 0);
    	t_chestplate = new TArmor("t_chestplate", 1, EntityEquipmentSlot.CHEST, 0);
    	t_legginsgs = new TArmor("t_legginsgs", 2, EntityEquipmentSlot.LEGS, 0);
    	t_boots = new TArmor("t_boots", 1, EntityEquipmentSlot.FEET, 0);

		tc_helmet = new TArmor("tc_helmet", 1, EntityEquipmentSlot.HEAD, 1);
    	tc_chestplate = new TArmor("tc_chestplate", 1, EntityEquipmentSlot.CHEST, 1);
    	tc_legginsgs = new TArmor("tc_legginsgs", 2, EntityEquipmentSlot.LEGS, 1);
    	tc_boots = new TArmor("tc_boots", 1, EntityEquipmentSlot.FEET, 1);

    	akane_helmet = new AArmor("akane_helmet", 1, EntityEquipmentSlot.HEAD, 0);
    	akane_chestplate = new AArmor("akane_chestplate", 1, EntityEquipmentSlot.CHEST, 1);
		akane_legginsgs = new AArmor("akane_leggings", 2, EntityEquipmentSlot.LEGS, 2);
		akane_boots = new AArmor("akane_boots", 1, EntityEquipmentSlot.FEET, 3);

		yukari_helmet = new YArmor("yukari_helmet", 1, EntityEquipmentSlot.HEAD, 0);
		yukari_chestplate = new YArmor("yukari_chestplate", 1, EntityEquipmentSlot.CHEST, 1);
		yukari_leggings = new YArmor("yukari_leggings", 2, EntityEquipmentSlot.LEGS, 2);
		yukari_boots = new YArmor("yukari_boots", 1, EntityEquipmentSlot.FEET, 3);

    	ya_helmet = new YAArmor("ya_helmet", 1, EntityEquipmentSlot.HEAD, 0);
    	ya_chestplate = new YAArmor("ya_chestplate", 1, EntityEquipmentSlot.CHEST, 1);
    	ya_legginsgs = new YAArmor("ya_legginsgs", 2, EntityEquipmentSlot.LEGS, 2);
    	ya_boots = new YAArmor("ya_boots", 1, EntityEquipmentSlot.FEET, 3);

    	copper_helmet = new CopperArmor("copper_helmet", 1, EntityEquipmentSlot.HEAD, 0);
    	copper_chestplate = new CopperArmor("copper_chestplate", 1, EntityEquipmentSlot.CHEST, 1);
    	copper_legginsgs = new CopperArmor("copper_leggings", 2, EntityEquipmentSlot.LEGS, 2);
    	copper_boots = new CopperArmor("copper_boots", 1, EntityEquipmentSlot.FEET, 3);

    	fl_helmet = new FArmor("fl_helmet", 1, EntityEquipmentSlot.HEAD, 0);
    	fl_chest = new FArmor("fl_chest", 1, EntityEquipmentSlot.CHEST, 1);
    	fl_leg = new FArmor("fl_leg", 2, EntityEquipmentSlot.LEGS, 2);
    	fl_boot = new FArmor("fl_boot", 1, EntityEquipmentSlot.FEET, 3);

    	redberyl_helmet = new RedberylArmor("redberyl_helmet", 1, EntityEquipmentSlot.HEAD);
    	redberyl_chestplate = new RedberylArmor("redberyl_chestplate", 1,EntityEquipmentSlot.CHEST);
    	redberyl_legginsgs = new RedberylArmor("redberyl_leggings", 2, EntityEquipmentSlot.LEGS);
    	redberyl_boots = new RedberylArmor("redberyl_boots", 1, EntityEquipmentSlot.FEET);

        tamagotti = new TFood(4, 1F, "tamagotti", 21);
        tamagottineo = new TFood(8, 1F, "tamagottineo", 22);
        tamagotticustom = new TItem("tamagotticustom");
    	tamagottimadder = new TItem("tamagottimadder");
        tamagottirepair = new TItem("tamagottirepair");
        tamagottisorella = new TItem("tamagottisorella");
        tamagottilink = new TLink("tamagottilink", 0);
        tamagotticollaboration =new TItem("tamagotticollaboration");
        tamagottinecro =new TItem("tamagottinecro");
    	h_tama = new TItem("h_tama");

		//備忘メモ：AddToolMaterial("アイテム名"、採掘レベル、耐久値、効率、攻撃力、エンチャントレベル),攻撃速度(加算)
		//備忘メモ：斧のみ、最後にattackDamage変数とattackSpeed変数へ代入する。値は加算される。

    	tamagottisword = new TSword("tamagottisword", EnumHelper.addToolMaterial("tamagottisword", 1, 256, 8.5F, 0, 8), 0D, 4);
        tamagottishovel = new TShovel("tamagottishovel", EnumHelper.addToolMaterial("tamagottishovel", 2, 256, 6, 2, 8));
        tamagottipickaxe = new TPick("tamagottipickaxe", EnumHelper.addToolMaterial("tamagottipickaxe", 2, 256, 6, 2, 8));
    	tamagottiaxe = new TAxe("tamagottiaxe", EnumHelper.addToolMaterial("tamagottiaxe", 2, 256, 6, 7, 16), 7, -3.1f);

    	tamagottiswordneo = new TSword("tamagottiswordneo", EnumHelper.addToolMaterial("tamagottiswordneo", 1, 1562, 8.5F, 1, 16), 0.1D, 5);
        tamagottishovelneo = new TShovel("tamagottishovelneo", EnumHelper.addToolMaterial("tamagottishovelneo", 3, 1562, 8, 3, 16));
        tamagottipickaxeneo = new TPick("tamagottipickaxeneo", EnumHelper.addToolMaterial("tamagottipickaxeneo", 3, 1562, 8, 3, 16));
    	tamagottiaxeneo = new TAxe("tamagottiaxeneo", EnumHelper.addToolMaterial("tamagottiaxeneo", 3, 1562, 16, 7, 16), 9, -3f);

    	tamagottiswordcustom = new TSword("tamagottiswordcustom", EnumHelper.addToolMaterial("tamagottiswordcustom", 1, 6248, 8.5F, 4, 32), 4D, 8);
        tamagottishovelcustom = new TShovel("tamagottishovelcustom", EnumHelper.addToolMaterial("tamagottishovelcustom", 3, 6248, 64, 5, 32));
        tamagottipickaxecustom = new TPick("tamagottipickaxecustom", EnumHelper.addToolMaterial("tamagottipickaxecustom", 3, 6248, 64, 5, 32));
    	tamagottiaxecustom = new TAxe("tamagottiaxecustom", EnumHelper.addToolMaterial("tamagottiaxecustom", 3, 6248, 64, 10, 64), 11, -2f);

    	tamagottiswordmadder = new TSword("tamagottiswordmadder", EnumHelper.addToolMaterial("tamagottiswordmadder", 1, 99999, 8.5F, 12, 64), 6.4D, 10);
        tamagottishovelmadder = new TShovel("tamagottishovelmadder", EnumHelper.addToolMaterial("tamagottishovelmadder", 5, 99999, 512, 7, 128));
        tamagottipickaxemadder = new TPick("tamagottipickaxemadder", EnumHelper.addToolMaterial("tamagottipickaxemadder", 5, 99999, 512, 7, 128));
    	tamagottiaxemadder = new TAxe("tamagottiaxemadder", EnumHelper.addToolMaterial("tamagottiaxemadder", 5, 99999, 512, 16, 128), 15, -2f);

        xsword = new XSword("xsword", EnumHelper.addToolMaterial("xsword", 1, 256, 8.5F, 3, 32));
        xshovel = new XShovel("xshovel", EnumHelper.addToolMaterial("xshovel", 2, 256, 6, 2, 8));
        xpick = new XPick("xpick", EnumHelper.addToolMaterial("xpick", 3, 256, 64, 5, 32));
        xaxe = new XAxe("xaxe", EnumHelper.addToolMaterial("xaxe", 3, 256, 64, 10, 64),9,-2f);

        coppersword = new TSword("coppersword", EnumHelper.addToolMaterial("coppersword", 1, 256, 8.5F, 0, 8), 3.4D, 0);
        coppershovel = new TShovel("coppershovel", EnumHelper.addToolMaterial("coppershovel", 1, 256, 5, 4, 8));
        copperpickaxe = new TPick("copperpickaxe", EnumHelper.addToolMaterial("copperpickaxe", 2, 256, 5, 4, 8));
        copperaxe = new TAxe("copperaxe", EnumHelper.addToolMaterial("copperaxe", 1, 256, 5, 6, 8),9,-3.3f);

        silversword = new TSword("silversword", EnumHelper.addToolMaterial("silversword", 1, 128, 8.5F, 8, 16), -1D, 0);
        silvershovel = new TShovel("silvershovel", EnumHelper.addToolMaterial("silvershovel", 3, 128, 64, 0, 16));
        silverpickaxe = new TPick("silverpickaxe", EnumHelper.addToolMaterial("silverpickaxe", 3, 128, 64, 2, 16));
        silveraxe = new TAxe("silveraxe", EnumHelper.addToolMaterial("silveraxe", 3, 128, 64, 2, 16),3,-3f);
        silverhammer = new SilverHammer("silverhammer", EnumHelper.addToolMaterial("silverhammer", 3, 128, 8, 2, 16));
        silvershield = new SShield("silvershield", 0);

        fluoritesword = new SmokySword("fluoritesword", EnumHelper.addToolMaterial("fluoritesword", 3, 512, 8.5F, 2, 16), 2F);
        fluoriteshovel = new SmokyShovel("fluoriteshovel", EnumHelper.addToolMaterial("fluoriteshovel", 3, 256, 8, 4, 16), 1);
        fluoritepickaxe = new SmokyPick("fluoritepickaxe", EnumHelper.addToolMaterial("fluoritepickaxe", 3, 256, 8, 4, 16), 1);
        fluoriteaxe = new SmokyAxe("fluoriteaxe", EnumHelper.addToolMaterial("fluoriteaxe", 3, 1024, 16, 4, 16), 9, -3.3f, 3);
        fluoritesickle = new SmokySickle("fluoritesickle", EnumHelper.addToolMaterial("fluoritesickle", 3, 512, 1, 4, 16), 4);

        smokysword = new SmokySword("smokysword", EnumHelper.addToolMaterial("smokysword", 3, 1024, 8.5F, 4, 16), 3.5F);
        smokyshovel = new SmokyShovel("smokyshovel", EnumHelper.addToolMaterial("smokyshovel", 3, 512, 16, 4, 16), 4);
        smokypickaxe = new SmokyPick("smokypickaxe", EnumHelper.addToolMaterial("smokypickaxe", 3, 512, 16, 4, 16), 4);
    	smokyaxe = new SmokyAxe("smokyaxe", EnumHelper.addToolMaterial("smokyaxe", 3, 2048, 32, 4, 16), 12, -3f, 6);
        smokysickle = new SmokySickle("smokysickle", EnumHelper.addToolMaterial("smokysickle", 3, 512, 1, 4, 16), 7);

        rubysword = new TSword("rubysword", EnumHelper.addToolMaterial("rubysword", 1, 512, 8.5F, 0, 8), 0D, 0);
        rubyshovel = new RubyShovel("rubyshovel", EnumHelper.addToolMaterial("rubyshovel", 3, 512, 16, 0, 16));
        rubypickaxe = new RubyPick("rubypickaxe", EnumHelper.addToolMaterial("rubypickaxe", 3, 512, 16, 2, 16));
		rubyaxe = new RubyAxe("rubyaxe", EnumHelper.addToolMaterial("rubyaxe", 3, 512, 16, 4, 16), 9, -3.3f);
        rubyhammer = new RubyHammer("rubyhammer", EnumHelper.addToolMaterial("rubyhammer", 3, 512, 16, 2, 16));
        rubybow = new RubyBow("rubybow");
        rubywand = new RubyWand("rubywand", EnumHelper.addToolMaterial("rubywand", 3, 512, 16, 2, 16));
        rubyshield = new SShield("rubyshield", 1);

    	yukarisword = new TABlade_0("yukarisword", EnumHelper.addToolMaterial("yukarisword", 1, 1024, 8.5F, 6, 16), 0, 1D, 0.2D);
        yukaakasword = new TABlade_1("yukaakasword", EnumHelper.addToolMaterial("yukaakasword", 1, 10000, 8.5F, 11, 16), 2.4D, 2, 0.6D);
        yukaakablade3 = new YABlade_3("yukaakablade3", EnumHelper.addToolMaterial("yukaakablade3", 1, 10000, 8.5F, 11, 16), 5, 100000, 16);
        akaneblade = new TABlade_0("akaneblade", EnumHelper.addToolMaterial("akaneblade", 1, 1024, 8.5F, 3, 8), 0, 0D, 0);
        tamagottiakaneblade_0 = new TABlade_0("tamagottiakaneblade_0", EnumHelper.addToolMaterial("tamagottiakaneblade_0", 1, 1024, 8.5F, 4, 16), 1, 0D, 0);
        tamagottiakaneblade_1 = new TABlade_0("tamagottiakaneblade_1", EnumHelper.addToolMaterial("tamagottiakaneblade_1", 1, 2048, 8.5F, 6, 32), 2, 0D, 0.2D);
        tamagottiakaneblade_2 = new TABlade_0("tamagottiakaneblade_2", EnumHelper.addToolMaterial("tamagottiakaneblade_2", 1, 3072, 8.5F, 8, 64), 3, 1D, 0.4D);
        tamagottiakaneblade_3 = new TABlade_1("tamagottiakaneblade_3", EnumHelper.addToolMaterial("tamagottiakaneblade_3", 1, 4096, 8.5F, 10, 80), 1.4D, 0, 0.4D);
        tamagottiakaneblade_4 = new TABlade_1("tamagottiakaneblade_4", EnumHelper.addToolMaterial("tamagottiakaneblade_4", 1, 8192, 8.5F, 12, 99), 2.4D, 1, 0.6D);
        ta_blade_5 = new TABlade_2("ta_blade_5", EnumHelper.addToolMaterial("ta_blade_5", 1, 1200, 8.5F, 12, 99), 2.4D);

        ancientswordreplica = new AncientSword("ancientswordreplica", EnumHelper.addToolMaterial("ancientswordreplica", 1, 1024, 8.5F, 4, 16), 1D);
        ancientswordreremodel = new AncientSwordRemodel("ancientswordreremodel", EnumHelper.addToolMaterial("ancientswordreremodel", 1, 1024, 8.5F, 12, 16), 1D);
        zundabow = new ZundaBow("zundabow");
        greatelectricspear = new GreatSpear("greatelectricspear", EnumHelper.addToolMaterial("greatelectricspear", 1, 1024, 8.5F, 4, 16), 1D);
        aononinjato = new AonoKatana("aononinjato", EnumHelper.addToolMaterial("aononinjato", 1, 1024, 8.5F, 2, 16), 4.4D);
        kiritanho = new KiritanHo("kiritanho");
        bondstar = new AkariStar("bondstar");
        seikarod = new SeikaRod("seikarod", EnumHelper.addToolMaterial("seikarod", 3, 1024, 1, 4, 16));
        akanerod = new ARod("akanerod", EnumHelper.addToolMaterial("akanerod", 1, 1024, 8.5F, 0, 16));
        zundaarrow = new TItem("zundaarrow");

        machinesword = new MSword("machinesword", EnumHelper.addToolMaterial("machinesword", 1, 1024, 8.5F, 8, 16));
    	m_boot = new MArmor("m_boot", 1, EntityEquipmentSlot.FEET);

        tcrossbow = new TCrossBow("tcrossbow");
        thalberd = new THalberd("thalberd", EnumHelper.addToolMaterial("thalberd", 1, 1024, 8.5F, 2, 16), 4.4D);
        thammer = new THammer("thammer", EnumHelper.addToolMaterial("thammer", 3, 1024, 16, 2, 16),9f, -2);
        tshield = new TShield("tshield", 256, 0);
        tshieldc = new TShield("tshieldc", 1024, 1);
        bamboobow = new BA_Bow("bamboobow", 256, 0);
        bambooarrow = new BA_Arrow("bambooarrow");
        triplets_bamboobow = new BA_Bow("triplets_bamboobow", 1024, 1);
        re_bambooarrow = new BA_Arrow("re_bambooarrow");

		tamagottiblaster = new TBlaster("tamagottiblaster", 0, 128);
        tamagottiblastercustom = new TBlaster("tamagottiblastercustom", 1, 256);
        tblasternecro = new TBlaster("tblasternecro", 2, 512);
        tamagottispinner = new TSpinner("tamagottispinner", 0, 32);
        tamagottispinnercustom = new TSpinner("tamagottispinnercustom", 1, 64);
        tamagottishelter = new TShelter("tamagottishelter", 0, 128);
        tamagottisheltersorella = new TShelter("tamagottisheltersorella", 1, 256);
        yukarisweeper = new YSweeper("yukarisweeper", 0, 128);
        yukarisweepercustom = new YSweeperC("yukarisweepercustom");
        yukarrittascope = new YRitter("yukarrittascope", 30 , 20, 128);
        yukarrittascopecustom = new YRitter("yukarrittascopecustom", 40 , 30, 256);
        yukarishotter = new YSweeper("yukarishotter", 1, 128);
        yukarishotter_co = new YSweeper("yukarishotter_co", 2, 256);
        heroblaster = new HBlaster("heroblaster");
        herocharger = new HCharger("herocharger");
        yukaribarrel = new TItem("yukaribarrel");
        yukaristock = new TItem("yukaristock");
        yukaritank = new TItem("yukaritank");
        yukariscorp = new TItem("yukariscorp");
        akanebladezero = new ABlade_0("akanebladezero", EnumHelper.addToolMaterial("akanebladezero", 1, 99999, 8.5F, 99995, 9999), 0D, 0);
        akanebladeinfinity = new ABlade_0("akanebladeinfinity", EnumHelper.addToolMaterial("akanebladeinfinity", 1, 99999, 8.5F, -3, 9999), 0D, 1);

    	akanecrystal = new TItem("akanecrystal");
    	yukaricrystal = new TItem("yukaricrystal");
    	akanepearl = new TItem("akanepearl");
    	yukaripearl = new TItem("yukaripearl");
    	madderstar = new BaseReturnItem("madderstar");

    	copperingot = new TItem("copperingot");
        silveringot = new TItem("silveringot");
        starrosequartz = new TItem("starrosequartz");
    	fluorite = new TItem("fluorite");
    	redberyl = new TItem("redberyl");
        ruby = new TItem("ruby");
    	smokyquartz = new TItem("smokyquartz");

        srq_ex = new ExItem("srq_extends");
        t_ex = new ExItem("t_extends");
        tpick_ex = new ExPick("tpick_extends", EnumHelper.addToolMaterial("tpick_extends", 3, 6248, 64, 5, 32), 12);
        tsword_ex = new ExSword("tsword_extends", EnumHelper.addToolMaterial("tsword_extends", 1, 6248, 8.5F, 6, 32), 4D);
        taxe_ex = new ExAxe("taxe_extends", EnumHelper.addToolMaterial("taxe_extends", 3, 6248, 64, 10, 64),9,-2f);
        tshovel_ex = new ExShovel("tshovel_extends", EnumHelper.addToolMaterial("tshovel_extends", 2, 6248, 6, 2, 8));
        trush_ex = new ExRush("trush_extends");
        tshield_ex = new TShield("tshield_extends", 6248, 2);

        y_pendant = new TPendant("y_pendant");
    	b_tamagotti_book = new TBook("b_tamagotti_book");
    	tamagotti_book_neo = new TBookNeo("tamagotti_book_neo");
        tamulet = new TAmulet("tamulet");
        trepairamulet = new TRepair("trepairamulet");
        tcontract = new TContract ("tcontract", 0);
        zoma_contract = new TContract ("zoma_contract", 1);
        stalo_contract = new TContract ("stalo_contract", 2);
        tchicken_contract = new TContract ("tchicken_contract", 3);
        tsail = new TSail("tsail", 0);
        tsail_n = new TSail("tsail_n", 1);

        hheld_farm = new H_Farm("hheld_farm");
        hheld_house = new H_House("hheld_house");
        hheld_house_1 = new H_House_1("hheld_house_1");
        telepo_book = new TLink("telepo_book", 1);
        tcushion_y = new TCushion("tcushion_y", 1);
        tcushion_a = new TCushion("tcushion_a", 0);
        tcrystal = new TCushion("tcrystal", 2);
        tbag =new TBag("tbag");
        tglove = new TGlove("tglove");

        tegg_c = new TEgg("tegg_c", 0);
        tegg_b = new TEgg("tegg_b", 1);
        tegg_p = new TEgg("tegg_p", 2);
        tegg_s = new TEgg("tegg_s", 3);
        tegg_v = new TEgg("tegg_v", 4);

        kitchenknife = new Knife("kitchenknife",EnumHelper.addToolMaterial("kitchenknife", 1, 128, 6.5F, 0.5F, 0), 3.5D);
        riter = new Riter("riter");
        yagen = new BaseReturnItem("yagen");
        minioiltank = new TItem("minioiltank");

        rack_fuel = new TFuel("rack_fuel", 600);
        bamboo_charcoal = new TFuel("bamboo_charcoal", 800);
        cb_charcoal = new TFuel("cb_charcoal", 9600);
        bambooi = new TItem("bambooi");

        tparasol = new TParasol("tparasol");
    	bspawner = new TItem("bspawner");

		//備忘メモ：アイテム名　=new アイテムクラス("アイテム名")
        // 備忘メモ：CustomFood(満腹度回復、隠し満腹度の量、狼が食べられるか)　となる。
        ine = new TFoodItem("ine");
        salt = new TFoodItem("salt");
        malt = new TFoodItem("malt");
        miso = new TFoodItem("miso");
        shrimp = new TFoodItem("shrimp");
        kinako = new TFoodItem("kinako");
        watercup = new WCup("watercup");
        sobapowder = new TFoodItem("sobapowder");
        flowerpowder = new TFoodItem("flowerpowder");
        botanical_rennet = new TFoodItem("botanical_rennet");
        whipping_cream = new TFoodItem("whipping_cream");
        butter = new TFoodItem("butter");
        breadcrumbs = new TFoodItem("breadcrumbs");
        pizza_cloth = new TFoodItem("pizza_cloth");
        nc_omurice = new TFoodItem("nc_omurice");
        nc_soysoup = new TFoodItem("nc_soysoup");
        nc_pizza = new TFoodItem("nc_pizza");
        nc_gratin = new TFoodItem("nc_gratin");
        nc_hotcake = new TFoodItem("nc_hotcake");
        nc_toast = new TFoodItem("nc_toast");
        nc_kiritanpo = new TFoodItem("nc_kiritanpo");
        nc_pork_cutlet = new TFoodItem("nc_pork_cutlet");
        nc_df_chicken = new TFoodItem("nc_df_chicken");
        nc_hamburger_steak = new TFoodItem("nc_hamburger_steak");
        nc_h_soba = new TFoodItem("nc_h_soba");
        nc_h_udon = new TFoodItem("nc_h_udon");
        nc_pork_soup = new TFoodItem("nc_pork_soup");
        nc_oyakodon = new TFoodItem("nc_oyakodon");
        nc_f_shrimp = new TFoodItem("nc_f_shrimp");
        nc_meat_bun = new TFoodItem("nc_meat_bun");
        gyoza_tane = new TFoodItem("gyoza_tane");

        canolaoil = new TFoodItem("canolaoil");
        dasi = new TFoodItem("dasi");
        tofu = new TFood(6, 0.5F, "tofu", 0);
        negi = new TFood(3, 0.5F, "negi", 0);
        cheese = new TFood(3,0.8F,"cheese", 0);
        mochi = new TFood(6, 0.3F, "mochi", 0);
        yakiebi = new TFood(7,0.8F,"yakiebi", 0);
        cabbage = new TFood(3, 0.4F, "cabbage", 0);
        lettuce = new TFood(3, 0.4F, "lettuce", 0);
        edamame = new TFood(3, 0.4F, "edamame", 0);
        e_plant = new TFood(3, 0.7F, "e_plant", 0);
        yakinasu = new TFood(7, 0.3F, "yakinasu", 0);
        j_radish = new TFood(3, 0.5F, "j_radish", 0);
        yakimochi = new TFood(8, 0.5F, "yakimochi", 0);
        w_strawberries = new TFood(3,0.5F, "w_strawberries",0);
        strawberry_jam = new TFood(3, 0.5F, "strawberry_jam",0);
        ohagi = new TFood(10, 0.5F, "ohagi", 1);
        oshiruko = new TFood(10, 0.5F, "oshiruko", 1);
        soysoup = new TFood(9, 0.75F, "soysoup", 2);
        soymilk  = new TFood(0, 0F, "soymilk", 2);
        milk_pack  = new TFood(0, 0F, "milk_pack", 2);
        natto = new TFood(6, 0.4F, "natto", 3);
        natto_rice = new TFood(9, 0.5F, "natto_rice", 3);
        sabaonigiri = new TFood(10, 0.5F, "sabaonigiri", 4);
        shakeonigiri = new TFood(10, 0.5F, "shakeonigiri", 4);
        pizza = new TFood(16,1.5F,"pizza", 6);
        gratin = new TFood(12,1.2F,"gratin", 6);
        soba = new TFood(10, 0.6F, "soba", 7);
        udon = new TFood(10, 0.6F, "udon", 7);
        h_udon = new TFood(14, 0.5F, "h_udon", 7);
        h_soba = new TFood(14, 0.5F, "h_soba", 7);
        rice = new TFood(6, 0.33F, "rice", 8);
        zunda = new TFood(10, 0.5F, "zunda", 9);
        toast = new TFood(7, 0.75F, "toast", 10);
        salad = new TFood(10, 0.5F, "salad", 10);
        jam_toast = new TFood(9, 0.5F, "jam_toast", 10);
        buttertoast = new TFood(10, 0.4F, "buttertoast", 10);
        ice_choco = new TFood(8, 0.3F, "ice_choco", 11);
        ice_strawberries = new TFood(7, 0.75F, "ice_strawberries", 11);
        sashimi = new TFood(7, 1F, "sashimi", 12);
        hotcake = new TFood(14, 0.65F, "hotcake", 13);
        omurice = new TFood(16, 1.5F, "omurice", 14);
        applepie = new TFood(10, 0.5F, "applepie", 15);
        kiritanpo = new TFood(8, 1F, "kiritanpo", 16);
        kinakomochi = new TFood(9, 0.4F, "kinakomochi", 17);
        tamagottirice = new TFood(10, 0.5F, "tamagottirice", 18);
        zuntamagotti = new TFood(12, 99F, "zuntamagotti", 19);
        tamagotti_sand = new TFood(8, 0.75F, "tamagotti_sand", 20);
        tamagottibull = new TFood(4, 0.5F, "tamagottibull", 23);
        f_shrimp = new TFood(13, 1F, "f_shrimp", 24);
        df_chicken = new TFood(10, 2F, "df_chicken", 24);
        pork_cutlet = new TFood(14, 2F, "pork_cutlet", 24);
        oyakodon = new TFood(17, 1.2F, "oyakodon", 25);
        pork_soup = new TFood(18, 1.2F, "pork_soup", 25);
        hamburger_steak = new TFood(16, 1.3F, "hamburger_steak", 25);
        purin = new TFood(8, 0.6F, "purin", 26);
        hot_cocoa = new TFood(5, 0.6F, "hot_cocoa", 27);
        takikomigohan = new TFood(8, 0.75F, "takikomigohan", 28);
        unknown_meat = new TFood(8, 128F, "unknown_meat", 29);
        hugusashi =new TFood(8, 1F, "hugusashi", 30);
        ce_tomato =new TFood(8, 0.4F, "ce_tomato", 31);
        meat_bun = new TFood(8, 0.75F, "meat_bun", 32);
        gyoza = new TFood(8, 0.6F, "gyoza", 33);

        		//備忘メモ：アイテム名　=new アイテムシードフードクラス(満腹度、隠し満腹度、植える作物ブロック)、("アイテム名")、("アイテム名")
        onion = new TSeedFood("onion", BlockInit.onionblock, 3, 0.5F);
        tomato = new TSeedFood("tomato", BlockInit.tomatoblock, 3, 0.5F);
        soyseed = new TSeedFood("soyseed", BlockInit.soyblock, 3, 0.5F);
        strawberry = new TSeedFood("strawberry", BlockInit.strawberryblock, 6, 0.5F);

        		//備忘メモ：アイテム名　=new アイテムシー3ドクラス(植える作物ブロック)、("アイテム名")、("アイテム名")
        sobaseed = new TSeed("sobaseed", BlockInit.sobablock);
        negiseed = new TSeed("negiseed", BlockInit.negiblock);
        riceseed = new TSeed("riceseed", BlockInit.riceblock);
        e_plantseed = new TSeed("e_plantseed", BlockInit.e_plantblock);
        j_radishseed = new TSeed("j_radishseed", BlockInit.j_radishblock);
        azukiseed = new TSeed("azukiseed", BlockInit.azukiblock);
        canolaseed = new TSeed("canolaseed", BlockInit.canolablock);
        lettuceseed = new TSeed("lettuceseed", BlockInit.lettuceblock);
        cabbageseed = new TSeed("cabbageseed", BlockInit.cabbageblock);
        ironseed = new TSeed("ironseed", BlockInit.ironblock);
        goldseed = new TSeed("goldseed", BlockInit.goldblock);
        lapisseed = new TSeed("lapisseed", BlockInit.lapisblock);
        redseed = new TSeed("redseed", BlockInit.redblock);
        quartzseed = new TSeed("quartzseed", BlockInit.quablock);

    }

	public static void register() {

		for (Item item : itemList) {
			registerItem(item, 0);
		}

		for (Item item : foodList) {
			registerItem(item, 1);
		}
    }

    public static void registerItem(Item item, int tabNum) {
    	ForgeRegistries.ITEMS.register(item);
    	//フード用にクリエイティブタブを整数で条件分けするように変更、0は従来のタブ、1はフードタブ
        if(tabNum == 0) {
        	item.setCreativeTab(TamagottiMod.tamagottiTab);
        } else if(tabNum == 1){
        	item.setCreativeTab(TamagottiMod.tamagottiFoodTab);
        }
        if(FMLCommonHandler.instance().getSide()==Side.CLIENT)
        	ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
}
