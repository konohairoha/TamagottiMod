package tamagotti.handlers;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import tamagotti.TamagottiMod;
import tamagotti.init.BlockInit;
import tamagotti.init.ItemInit;
import tamagotti.recipe.RecipeEnchantExtend;
import tamagotti.recipe.RecipeHelper;
import tamagotti.recipe.RecipeNBTExtend;
import tamagotti.util.DummyRecipeBase;

public class RecipeHandler {

	public static void registerCrafting() {

		String modId = TamagottiMod.MODID;

		RecipeHandler.addRecipe("tAakne0",
			new RecipeEnchantExtend(new ResourceLocation(modId, "tAakne0"),
			new ItemStack(ItemInit.tamagottiakaneblade_0),
				" T ",
				"TAT",
				" T ",
				'T', ItemInit.tamagotti,
				'A', ItemInit.akaneblade)
		);

		RecipeHandler.addRecipe("tAakne1",
			new RecipeEnchantExtend(new ResourceLocation(modId, "tAakne1"),
			new ItemStack(ItemInit.tamagottiakaneblade_1),
				" T ",
				"TAT",
				" T ",
				'T', ItemInit.tamagottineo,
				'A', ItemInit.tamagottiakaneblade_0)
		);

		RecipeHandler.addRecipe("tAakne2",
			new RecipeEnchantExtend(new ResourceLocation(modId, "tAakne2"),
			new ItemStack(ItemInit.tamagottiakaneblade_2),
				" T ",
				"TAT",
				" T ",
				'T', BlockInit.redberylblock,
				'A', ItemInit.tamagottiakaneblade_1)
		);

		RecipeHandler.addRecipe("tAakne3",
			new RecipeEnchantExtend(new ResourceLocation(modId, "tAakne3"),
			new ItemStack(ItemInit.tamagottiakaneblade_3),
				" AR",
				"YTA",
				"RY ",
				'Y', ItemInit.yukaricrystal,
				'A', ItemInit.akanecrystal,
				'R', ItemInit.tamagottirush,
				'T', ItemInit.tamagottiakaneblade_2)
		);

		RecipeHandler.addRecipe("tAakne4",
			new RecipeEnchantExtend(new ResourceLocation(modId, "tAakne4"),
			new ItemStack(ItemInit.tamagottiakaneblade_4),
				" AR",
				"YTA",
				"RY ",
				'Y', ItemInit.yukaripearl,
				'A', ItemInit.akanepearl,
				'R', ItemInit.tamagotticustom,
				'T', ItemInit.tamagottiakaneblade_3)
		);

		RecipeHandler.addRecipe("tAakne5",
			new RecipeEnchantExtend(new ResourceLocation(modId, "tAakne5"),
			new ItemStack(ItemInit.ta_blade_5),
				"YRA",					"RBR",
				"TRY",
				'Y', ItemInit.yukaripearl,
				'A', ItemInit.akanepearl,
				'R', ItemInit.tamagotticollaboration,
				'B', ItemInit.yukarisword,
				'T', ItemInit.tamagottiakaneblade_2)
		);

		RecipeHandler.addRecipe("yukarisword",
			new RecipeEnchantExtend(new ResourceLocation(modId, "yukarisword"),
			new ItemStack(ItemInit.yukarisword),
				"OYO",
				"OYO",
				"ODO",
				'Y', ItemInit.yukaricrystal,
				'O', Blocks.OBSIDIAN,
				'D', Items.DIAMOND_SWORD)
		);

		RecipeHandler.addRecipe("yukaakasword",
			new RecipeEnchantExtend(new ResourceLocation(modId, "yukaakasword"),
			new ItemStack(ItemInit.yukaakasword),
				"PRP",
				"CTC",
				"PYP",
				'P', ItemInit.yukaripearl,
				'C', ItemInit.yukaricrystal,
				'R', ItemInit.tamagottirush,
				'T', ItemInit.tamagottiakaneblade_2,
				'Y', ItemInit.yukarisword)
		);

		RecipeHandler.addRecipe("thalberd",
			new RecipeEnchantExtend(new ResourceLocation(modId, "thalberd"),
			new ItemStack(ItemInit.thalberd),
				" XR",
				"FCX",
				"SF ",
				'S', Items.STICK,
				'R', ItemInit.ruby,
				'X', ItemInit.xsword,
				'F', ItemInit.fluorite,
				'C', ItemInit.tamagottiswordcustom)
		);

		RecipeHandler.addRecipe("thammer",
			new RecipeEnchantExtend(new ResourceLocation(modId, "thammer"),
			new ItemStack(ItemInit.thammer),
				"RQB",
				"QC ",
				"B S",
				'S', Items.STICK,
				'B', BlockInit.silverblock,
				'R', ItemInit.ruby,
				'Q', ItemInit.smokyquartz,
				'C', ItemInit.tamagottiaxecustom)
		);

		RecipeHandler.addRecipe("aononinjato",
			new RecipeEnchantExtend(new ResourceLocation(modId, "aononinjato"),
			new ItemStack(ItemInit.aononinjato),
				" IR",
				" IR",
				"FDR",
				'D', Items.DIAMOND_SWORD,
				'I', Blocks.PACKED_ICE,
				'F', ItemInit.fluorite,
				'R', "dyeBlue")
		);

		RecipeHandler.addRecipe("gspear",
			new RecipeEnchantExtend(new ResourceLocation(modId, "gspear"),
			new ItemStack(ItemInit.greatelectricspear),
				"GFG",
				"GFG",
				"GDG",
				'D', Items.DIAMOND_SWORD,
				'G', Items.GOLD_INGOT,
				'F', ItemInit.fluorite)
		);

		RecipeHandler.addRecipe("zundabow",
			new RecipeEnchantExtend(new ResourceLocation(modId, "zundabow"),
			new ItemStack(ItemInit.zundabow, 1, 1024),
				" Z ",
				"ZBZ",
				" Z ",
				'Z', ItemInit.zunda,
				'B', Ingredient.fromItems(Items.BOW, ItemInit.bamboobow))
		);


		RecipeHandler.addRecipe("an_sword",
			new RecipeEnchantExtend(new ResourceLocation(modId, "an_sword"),
			new ItemStack(ItemInit.ancientswordreplica),
				"ORO",
				"ORO",
				"ODO",
				'D', Items.DIAMOND_SWORD,
				'O', Blocks.OBSIDIAN,
				'R', ItemInit.redberyl)
		);

		RecipeHandler.addRecipe("an_swordre",
			new RecipeEnchantExtend(new ResourceLocation(modId, "an_swordre"),
			new ItemStack(ItemInit.ancientswordreremodel),
				" S ",
				"SAS",
				" S ",
				'S', ItemInit.srq_ex,
				'A', ItemInit.ancientswordreplica)
		);

		RecipeHandler.addRecipe("tshelter_so",
			new RecipeEnchantExtend(new ResourceLocation(modId, "tshelter_so"),
				new ItemStack(ItemInit.tamagottisheltersorella),
				" S ",
				"SAS",
				" S ",
				'S', ItemInit.tamagottisorella,
				'A', ItemInit.tamagottishelter)
		);

		RecipeHandler.addRecipe("triplets_bamboobow",
			new RecipeEnchantExtend(new ResourceLocation(modId, "triplets_bamboobow"),
			new ItemStack(ItemInit.triplets_bamboobow),
				"TSI",
				"SB ",
				"I  ",
				'S', ItemInit.silveringot,
				'T', ItemInit.tamagotticollaboration,
				'B', ItemInit.bamboobow,
				'I', BlockInit.bamboob)
		);

		RecipeHandler.addRecipe("swchest",
			new RecipeNBTExtend(new ResourceLocation(modId, "swchest"),
			new ItemStack(BlockInit.swchest),
				" S ",
				"SAS",
				" S ",
				'S', ItemInit.starrosequartz,
				'A', Ingredient.fromItems(Item.getItemFromBlock(BlockInit.tamagottichest), Item.getItemFromBlock(BlockInit.tamazonchest)))
		);

		RecipeHandler.addRecipe("tchest",
			new RecipeNBTExtend(new ResourceLocation(modId, "tchest"),
			new ItemStack(BlockInit.tamagottichest),
				" S ",
				"SAS",
				" S ",
				'S', Ingredient.fromItems(Items.IRON_INGOT, ItemInit.copperingot),
				'A', BlockInit.wadansu)
		);

		RecipeHandler.addRecipe("potc",
			new RecipeNBTExtend(new ResourceLocation(modId, "potc"),
			new ItemStack(BlockInit.potcustom_0),
				" S ",
				"SPS",
				" S ",
				'P', Ingredient.fromItems(Item.getItemFromBlock(BlockInit.potneo_0), Item.getItemFromBlock(BlockInit.potneo_1)),
				'S', ItemInit.ruby)
		);

		RecipeHandler.addRecipe("potn",
			new RecipeNBTExtend(new ResourceLocation(modId, "potn"),
			new ItemStack(BlockInit.potneo_0),
				" S ",
				"SPS",
				" S ",
				'P', Ingredient.fromItems(Item.getItemFromBlock(BlockInit.pot_0), Item.getItemFromBlock(BlockInit.pot_1)),
				'S', ItemInit.smokyquartz)
		);

		RecipeHandler.addRecipe("redberylwand",
			new RecipeNBTExtend(new ResourceLocation(modId, "redberylwand"),
			new ItemStack(ItemInit.redberylwand),
				"GGR",
				" BG",
				"S G",
				'G', Items.GOLD_INGOT,
				'R', BlockInit.redberylblock,
				'B', BlockInit.rubyblock,
				'S', Items.STICK)
		);

		RecipeHandler.addRecipe("magicwand",
			new RecipeNBTExtend(new ResourceLocation(modId, "magicwand"),
			new ItemStack(ItemInit.magicwand),
				"SSQ",
				" DS",
				"R S",
				'Q', BlockInit.smokyquartzblock,
				'S', ItemInit.starrosequartz,
				'D', Blocks.DIAMOND_BLOCK,
				'R', ItemInit.redberylwand)
		);

		RecipeHandler.addRecipe("mfwand",
			new RecipeNBTExtend(new ResourceLocation(modId, "mfwand"),
				new ItemStack(ItemInit.magiafluxwand),
				"MMS",
				" SM",
				"R M",
				'S', ItemInit.srq_ex,
				'M', "mfbottle",
				'R', ItemInit.magicwand)
		);


		List<RecipeHelper> toolList = new ArrayList<>();
		toolList.add(new RecipeHelper(ItemInit.copperingot, Items.STICK, ItemInit.copperpickaxe, ItemInit.copperaxe, ItemInit.coppershovel, ItemInit.coppersword));
		toolList.add(new RecipeHelper(ItemInit.silveringot, Items.STICK, ItemInit.silverpickaxe, ItemInit.silveraxe, ItemInit.silvershovel, ItemInit.silversword));
		toolList.add(new RecipeHelper(ItemInit.tamagotti, Items.STICK, ItemInit.tamagottipickaxe, ItemInit.tamagottiaxe, ItemInit.tamagottishovel, ItemInit.tamagottisword));
		toolList.add(new RecipeHelper(ItemInit.tamagottineo, Items.STICK, ItemInit.tamagottipickaxeneo, ItemInit.tamagottiaxeneo, ItemInit.tamagottishovelneo, ItemInit.tamagottiswordneo));
		toolList.add(new RecipeHelper(ItemInit.tamagotticustom, Items.STICK, ItemInit.tamagottipickaxecustom, ItemInit.tamagottiaxecustom, ItemInit.tamagottishovelcustom, ItemInit.tamagottiswordcustom));
		toolList.add(new RecipeHelper(ItemInit.tamagottimadder, Items.STICK, ItemInit.tamagottipickaxemadder, ItemInit.tamagottiaxemadder, ItemInit.tamagottishovelmadder, ItemInit.tamagottiswordmadder));
		toolList.add(new RecipeHelper(ItemInit.tamagottibettyu, ItemInit.bettyustick, ItemInit.bettyupickaxe, ItemInit.bettyuaxe, ItemInit.bettyushovel, ItemInit.bettyusword));
		toolList.add(new RecipeHelper(ItemInit.tamagotticollaboration, Items.STICK, ItemInit.xpick, ItemInit.xaxe, ItemInit.xshovel, ItemInit.xsword));
		toolList.add(new RecipeHelper(ItemInit.t_ex, Items.STICK, ItemInit.tpick_ex, ItemInit.taxe_ex, ItemInit.tshovel_ex, ItemInit.tsword_ex));

		// リストの分だけ回す
		for (RecipeHelper recipe : toolList ) {

			Item material = recipe.getMaterial();
			Item stick = recipe.getStick();
			Item axe = recipe.getAxe();
			Item pick = recipe.getPick();
			Item shovel = recipe.getShovel();
			Item sword = recipe.getSword();

			// 斧
			GameRegistry.addShapedRecipe(new ResourceLocation(modId, axe.getUnlocalizedName()), new ResourceLocation(modId),
				new ItemStack(axe),
				new Object[] {
					"MM ",
					"MS ",
					" S ",
					'M', material,
					'S', stick
				}
			);

			// ピッケル
			GameRegistry.addShapedRecipe(new ResourceLocation(modId, pick.getUnlocalizedName()), new ResourceLocation(modId),
				new ItemStack(pick),
				new Object[] {
					"MMM",
					" S ",
					" S ",
					'M', material,
					'S', stick
				}
			);

			// シャベル
			GameRegistry.addShapedRecipe(new ResourceLocation(modId, shovel.getUnlocalizedName()), new ResourceLocation(modId),
				new ItemStack(shovel),
				new Object[] {
					" M ",
					" S ",
					" S ",
					'M', material,
					'S', stick
				}
			);

			// 剣
			GameRegistry.addShapedRecipe(new ResourceLocation(modId, sword.getUnlocalizedName()), new ResourceLocation(modId),
				new ItemStack(sword),
				new Object[] {
					" M ",
					" M ",
					" S ",
					'M', material,
					'S', stick
				}
			);
		}


		List<RecipeHelper> armorList = new ArrayList<>();
		armorList.add(new RecipeHelper(ItemInit.copperingot, ItemInit.copper_helmet, ItemInit.copper_chestplate, ItemInit.copper_legginsgs, ItemInit.copper_boots));
		armorList.add(new RecipeHelper(ItemInit.tamagotti, ItemInit.t_helmet, ItemInit.t_chestplate, ItemInit.t_legginsgs, ItemInit.t_boots));
		armorList.add(new RecipeHelper(ItemInit.tamagotticustom, ItemInit.tc_helmet, ItemInit.tc_chestplate, ItemInit.tc_legginsgs, ItemInit.tc_boots));
		armorList.add(new RecipeHelper(ItemInit.tamagottibettyu, ItemInit.bettyu_helmet, ItemInit.bettyu_chestplate, ItemInit.bettyu_legginsgs, ItemInit.bettyu_boots));
		armorList.add(new RecipeHelper(ItemInit.fluorite, ItemInit.fl_helmet, ItemInit.fl_chest, ItemInit.fl_leg, ItemInit.fl_boot));
		armorList.add(new RecipeHelper(ItemInit.redberyl, ItemInit.redberyl_helmet, ItemInit.redberyl_chestplate, ItemInit.redberyl_legginsgs, ItemInit.redberyl_boots));
		armorList.add(new RecipeHelper(ItemInit.tamagottibettyu, ItemInit.bettyu_helmet, ItemInit.bettyu_chestplate, ItemInit.bettyu_legginsgs, ItemInit.bettyu_boots));

		// リストの分だけ回す
		for (RecipeHelper recipe : armorList ) {

			Item material = recipe.getMaterial();
			Item hel = recipe.getHel();
			Item chest = recipe.getChest();
			Item leg = recipe.getLeg();
			Item boot = recipe.getBoot();

			// ヘルメット
			GameRegistry.addShapedRecipe(new ResourceLocation(modId, hel.getUnlocalizedName()), new ResourceLocation(modId),
				new ItemStack(hel),
				new Object[] {
					"MMM",
					"M M",
					'M', material
				}
			);

			// チェストプレート
			GameRegistry.addShapedRecipe(new ResourceLocation(modId, chest.getUnlocalizedName()), new ResourceLocation(modId),
				new ItemStack(chest),
				new Object[] {
					"M M",
					"MMM",
					"MMM",
					'M', material
				}
			);

			// レギンス
			GameRegistry.addShapedRecipe(new ResourceLocation(modId, leg.getUnlocalizedName()), new ResourceLocation(modId),
				new ItemStack(leg),
				new Object[] {
					"MMM",
					"M M",
					"M M",
					'M', material
				}
			);

			// ブーツ
			GameRegistry.addShapedRecipe(new ResourceLocation(modId, boot.getUnlocalizedName()), new ResourceLocation(modId),
				new ItemStack(boot),
				new Object[] {
					"M M",
					"M M",
					'M', material
				}
			);
		}


		List<RecipeHelper> oreList = new ArrayList<>();
		oreList.add(new RecipeHelper(ItemInit.copperingot, BlockInit.copperblock));
		oreList.add(new RecipeHelper(ItemInit.redberyl, BlockInit.redberylblock));
		oreList.add(new RecipeHelper(ItemInit.fluorite, BlockInit.fluoriteblock));
		oreList.add(new RecipeHelper(ItemInit.smokyquartz, BlockInit.smokyquartzblock));
		oreList.add(new RecipeHelper(ItemInit.silveringot, BlockInit.silverblock));
		oreList.add(new RecipeHelper(ItemInit.ruby, BlockInit.rubyblock));
		oreList.add(new RecipeHelper(ItemInit.starrosequartz, BlockInit.srqblock));

		// リストの分だけ回す
		for (RecipeHelper recipe : oreList ) {

			Item material = recipe.getMaterial();
			Block ore = recipe.getOre();

			// インゴット→ブロック
			GameRegistry.addShapedRecipe(new ResourceLocation(modId, ore.getUnlocalizedName()), new ResourceLocation(modId),
				new ItemStack(ore),
				new Object[] {
					"MMM",
					"MMM",
					"MMM",
					'M', material
				}
			);

			// ブロック→インゴット
			GameRegistry.addShapelessRecipe(new ResourceLocation(modId, material.getUnlocalizedName()), new ResourceLocation(modId),
				new ItemStack(material, 9),
				new Ingredient[] { Ingredient.fromStacks(new ItemStack(ore)) }
			);
		}


		List<RecipeHelper> rushList = new ArrayList<>();
		rushList.add(new RecipeHelper(ItemInit.tamagotti, ItemInit.tamagottirush));
		rushList.add(new RecipeHelper(ItemInit.tamagottineo, ItemInit.tamagottirushneo));
		rushList.add(new RecipeHelper(ItemInit.tamagotticustom, ItemInit.tamagottirushcustom));
		rushList.add(new RecipeHelper(ItemInit.tamagottilink, ItemInit.tamagottirushlink));
		rushList.add(new RecipeHelper(ItemInit.tamagottimadder, ItemInit.tamagottirushmadder));
		rushList.add(new RecipeHelper(ItemInit.tamagottibettyu, ItemInit.bettyurush));
		rushList.add(new RecipeHelper(ItemInit.tamagottirepair, ItemInit.tamagottirushrepair));
		rushList.add(new RecipeHelper(ItemInit.t_ex, ItemInit.trush_ex));

		// リストの分だけ回す
		for (RecipeHelper recipe : rushList ) {

			Item material = recipe.getMaterial();
			Item rush = recipe.getRush();

			// ラッシュ
			GameRegistry.addShapedRecipe(new ResourceLocation(modId, rush.getUnlocalizedName()), new ResourceLocation(modId),
				new ItemStack(rush),
				new Object[] {
					"MMM",
					"MMM",
					"MMM",
					'M', material
				}
			);
		}
	}

    public static void registerSmelting() {

    	//たまごっちメモ：(精錬前のアイテムまたはブロック)、（精錬後のアイテムまたはブロック）、精錬時に出る経験値
    	GameRegistry.addSmelting(BlockInit.copperore, new ItemStack(ItemInit.copperingot), 1.5F);
    	GameRegistry.addSmelting(BlockInit.silverore, new ItemStack(ItemInit.silveringot), 1.7F);
    	GameRegistry.addSmelting(BlockInit.rubyore, new ItemStack(ItemInit.ruby), 3F);
    	GameRegistry.addSmelting(BlockInit.redberylore, new ItemStack(ItemInit.redberyl), 2F);
    	GameRegistry.addSmelting(BlockInit.smokyquartzore, new ItemStack(ItemInit.smokyquartz), 3F);
    	GameRegistry.addSmelting(BlockInit.fluoriteore, new ItemStack(ItemInit.fluorite), 2.5F);
    	GameRegistry.addSmelting(BlockInit.starrosequartzore, new ItemStack(ItemInit.starrosequartz), 4F);
    	GameRegistry.addSmelting(BlockInit.t_ore, new ItemStack(ItemInit.tamagotti), 2F);
    	GameRegistry.addSmelting(BlockInit.t_neoore, new ItemStack(ItemInit.tamagottineo), 2.75F);
    	GameRegistry.addSmelting(Items.ROTTEN_FLESH, new ItemStack(Items.LEATHER), 0.2F);
    	GameRegistry.addSmelting(Blocks.BONE_BLOCK, new ItemStack(Blocks.WOOL), 0.5F);
    	GameRegistry.addSmelting(ItemInit.nc_omurice, new ItemStack(ItemInit.omurice), 1.5F);
    	GameRegistry.addSmelting(ItemInit.nc_soysoup, new ItemStack(ItemInit.soysoup), 1.1F);
    	GameRegistry.addSmelting(ItemInit.nc_hotcake, new ItemStack(ItemInit.hotcake), 1.5F);
    	GameRegistry.addSmelting(ItemInit.nc_toast, new ItemStack(ItemInit.toast), 0.5F);
    	GameRegistry.addSmelting(ItemInit.mochi, new ItemStack(ItemInit.yakimochi), 0.5F);
    	GameRegistry.addSmelting(ItemInit.soymilk, new ItemStack(ItemInit.tofu), 0.3F);
    	GameRegistry.addSmelting(ItemInit.watercup, new ItemStack(ItemInit.salt, 16, 0), 0.2F);
    	GameRegistry.addSmelting(Items.POTIONITEM, new ItemStack(ItemInit.salt, 4, 0), 0.35F);
    	GameRegistry.addSmelting(ItemInit.nc_pizza, new ItemStack(ItemInit.pizza), 2.0F);
    	GameRegistry.addSmelting(ItemInit.nc_gratin, new ItemStack(ItemInit.gratin), 1.1F);
    	GameRegistry.addSmelting(ItemInit.nc_kiritanpo, new ItemStack(ItemInit.kiritanpo), 0.8F);
    	GameRegistry.addSmelting(ItemInit.unknown_meat, new ItemStack(ItemInit.tamagottibull), 0.5F);
    	GameRegistry.addSmelting(ItemInit.nc_pork_cutlet, new ItemStack(ItemInit.pork_cutlet), 1.2F);
    	GameRegistry.addSmelting(ItemInit.nc_df_chicken, new ItemStack(ItemInit.df_chicken), 1.0F);
    	GameRegistry.addSmelting(ItemInit.nc_hamburger_steak, new ItemStack(ItemInit.hamburger_steak), 1.5F);
    	GameRegistry.addSmelting(ItemInit.nc_h_udon, new ItemStack(ItemInit.h_udon), 1.5F);
    	GameRegistry.addSmelting(ItemInit.nc_h_soba, new ItemStack(ItemInit.h_soba), 1.8F);
    	GameRegistry.addSmelting(ItemInit.e_plant, new ItemStack(ItemInit.yakinasu), 0.5F);
    	GameRegistry.addSmelting(ItemInit.nc_pork_soup, new ItemStack(ItemInit.pork_soup), 1.2F);
    	GameRegistry.addSmelting(ItemInit.nc_oyakodon, new ItemStack(ItemInit.oyakodon), 1.2F);
    	GameRegistry.addSmelting(ItemInit.nc_f_shrimp, new ItemStack(ItemInit.f_shrimp), 1.0F);
    	GameRegistry.addSmelting(ItemInit.shrimp, new ItemStack(ItemInit.yakiebi), 0.8F);
    	GameRegistry.addSmelting(ItemInit.bambooi, new ItemStack(ItemInit.bamboo_charcoal), 0.33F);
    	GameRegistry.addSmelting(ItemInit.nc_meat_bun, new ItemStack(ItemInit.meat_bun), 0.85F);
    	GameRegistry.addSmelting(Items.DIAMOND_PICKAXE, new ItemStack(Items.DIAMOND), 0.75F);
    	GameRegistry.addSmelting(Items.DIAMOND_HOE, new ItemStack(Items.DIAMOND), 0.75F);
    	GameRegistry.addSmelting(Items.DIAMOND_AXE, new ItemStack(Items.DIAMOND), 0.75F);
    	GameRegistry.addSmelting(Items.DIAMOND_SHOVEL, new ItemStack(Items.DIAMOND), 0.75F);
    	GameRegistry.addSmelting(Items.DIAMOND_SWORD, new ItemStack(Items.DIAMOND), 0.75F);
    	GameRegistry.addSmelting(Items.DIAMOND_HORSE_ARMOR, new ItemStack(Items.DIAMOND), 0.75F);
    	GameRegistry.addSmelting(ItemInit.gyoza_tane, new ItemStack(ItemInit.gyoza), 0.8F);
    }

    public static Multimap<String,IRecipe> recipeMultimap = HashMultimap.create();

    public static void addRecipe(String key, IRecipe value) {
        addRecipe(key, value, value instanceof DummyRecipeBase);
    }

    public static void addRecipe(String key, IRecipe value, boolean isDummy) {
        if(!isDummy) {
            if(value.getRegistryName() == null)
				value.setRegistryName(new ResourceLocation(value.getGroup()));
            ForgeRegistries.RECIPES.register(value);
        }
        recipeMultimap.put(key, value);
    }
}
