package tamagotti.handlers;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.oredict.OreDictionary;
import tamagotti.init.BlockInit;
import tamagotti.init.ItemInit;

public class OreDictionaryHandler {

	//鉱石辞書にある素材名と自分のMODの素材を紐付けする
	public static void registerOreDictionary() {

        OreDictionary.registerOre("treeSapling", BlockInit.sakurasapling);
        OreDictionary.registerOre("plankWood", BlockInit.sakuraplanks0);
        OreDictionary.registerOre("plankWood", BlockInit.sakuraplanks1);
        OreDictionary.registerOre("plankWood", BlockInit.bambooplanks);
        OreDictionary.registerOre("oreCoal", ItemInit.bamboo_charcoal);
        OreDictionary.registerOre("logWood", BlockInit.sakuralog);
        OreDictionary.registerOre("treeLeaves",BlockInit.sakuraleave);
        OreDictionary.registerOre("treesapling",BlockInit.sakurasapling);
        OreDictionary.registerOre("doorWood", ItemInit.sakuradoori_0);
        OreDictionary.registerOre("doorWood", ItemInit.sakuradoori_1);
        OreDictionary.registerOre("doorWood", ItemInit.sakuradoori_2);
        OreDictionary.registerOre("ingotCopper", ItemInit.copperingot);
        OreDictionary.registerOre("ingotSilver", ItemInit.silveringot);
        OreDictionary.registerOre("oreCopper", BlockInit.copperore);
        OreDictionary.registerOre("oreSilver", BlockInit.silverore);
        OreDictionary.registerOre("oreStarrosequartz", BlockInit.starrosequartzore);
        OreDictionary.registerOre("oreRedberyl", BlockInit.redberylore);
        OreDictionary.registerOre("oreFluorite", BlockInit.fluoriteore);
        OreDictionary.registerOre("oreSmokyquartz", BlockInit.smokyquartzore);
        OreDictionary.registerOre("oreTamagotti", BlockInit.t_ore);
        OreDictionary.registerOre("oreTamagottineo", BlockInit.t_neoore);
        OreDictionary.registerOre("oreRuby", BlockInit.rubyore);
        OreDictionary.registerOre("blockCopper", BlockInit.copperblock);
        OreDictionary.registerOre("blockRuby", BlockInit.rubyblock);
        OreDictionary.registerOre("blockSilver", BlockInit.silverblock);

        // スイートマジック連携用
        OreDictionary.registerOre("hopper", BlockInit.thopper);
        OreDictionary.registerOre("hopper", BlockInit.thopper_f);
        OreDictionary.registerOre("hopper", BlockInit.thopper_n);
        OreDictionary.registerOre("hopper", BlockInit.thopper_s);

        OreDictionary.registerOre("hopper", Blocks.HOPPER);
        OreDictionary.registerOre("recipeBook", ItemInit.b_tamagotti_book);
        OreDictionary.registerOre("recipeBook", ItemInit.tamagotti_book_neo);
        OreDictionary.registerOre("recipeBook", ItemInit.bettyubook);
        OreDictionary.registerOre("milkBucket", ItemInit.milk_pack);
        OreDictionary.registerOre("milkBucket", ItemInit.soymilk);
        OreDictionary.registerOre("milkBucket", Items.MILK_BUCKET);
        OreDictionary.registerOre("waterBucket", ItemInit.watercup);
        OreDictionary.registerOre("waterBucket", Items.WATER_BUCKET);
        OreDictionary.registerOre("riceSeed", ItemInit.riceseed);
        OreDictionary.registerOre("salt", ItemInit.salt);
        OreDictionary.registerOre("flourPowder", ItemInit.flowerpowder);
        OreDictionary.registerOre("flourPowder", Items.WHEAT);
        OreDictionary.registerOre("breadCrumbs", ItemInit.breadcrumbs);
        OreDictionary.registerOre("cheese", ItemInit.cheese);
        OreDictionary.registerOre("butter", ItemInit.butter);
        OreDictionary.registerOre("strawberry", ItemInit.strawberry);
        OreDictionary.registerOre("strawberry", ItemInit.w_strawberries);
        OreDictionary.registerOre("oil", ItemInit.canolaoil);
        OreDictionary.registerOre("azuki", ItemInit.azukiseed);

        OreDictionary.registerOre("whippingCream", ItemInit.whipping_cream);
        OreDictionary.registerOre("soyBean", ItemInit.soyseed);
        OreDictionary.registerOre("edamame", ItemInit.edamame);
        OreDictionary.registerOre("rice", ItemInit.rice);
        OreDictionary.registerOre("kinako", ItemInit.kinako);
        OreDictionary.registerOre("ine", ItemInit.ine);

        OreDictionary.registerOre("blockGlass", BlockInit.copperglass);
        OreDictionary.registerOre("blockGlass", BlockInit.copperlightglass);

        OreDictionary.registerOre("tWood", BlockInit.sakuraplanks0);
        OreDictionary.registerOre("tWood", BlockInit.sakuraplanks1);

        OreDictionary.registerOre("tomato", ItemInit.tomato);
        OreDictionary.registerOre("eggplant", ItemInit.e_plant);
        OreDictionary.registerOre("lettuce", ItemInit.lettuce);
        OreDictionary.registerOre("j_radish", ItemInit.j_radish);
        OreDictionary.registerOre("cabbage", ItemInit.cabbage);
        OreDictionary.registerOre("onion", ItemInit.onion);

		OreDictionary.registerOre("bucketWater", ItemInit.watercup);
		OreDictionary.registerOre("cropStrawberry", ItemInit.strawberry);
		OreDictionary.registerOre("cropRice", ItemInit.rice);
		OreDictionary.registerOre("dustFlour", ItemInit.flowerpowder);
		OreDictionary.registerOre("foodFlour", ItemInit.flowerpowder);
		OreDictionary.registerOre("foodButter", ItemInit.butter);
		OreDictionary.registerOre("cropTomato", ItemInit.tomato);
		OreDictionary.registerOre("cropOnion", ItemInit.onion);
		OreDictionary.registerOre("cropRadish", ItemInit.j_radish);
		OreDictionary.registerOre("cropEggplant", ItemInit.e_plant);
		OreDictionary.registerOre("cropCabbage", ItemInit.cabbage);
		OreDictionary.registerOre("cropGreenSoybeans", ItemInit.edamame);
		OreDictionary.registerOre("cropSoybean", ItemInit.soyseed);
		OreDictionary.registerOre("cropSoy", ItemInit.soyseed);
		OreDictionary.registerOre("seedSoybean", ItemInit.soyseed);
		OreDictionary.registerOre("listAllwater", ItemInit.watercup);
		OreDictionary.registerOre("cropBean", ItemInit.azukiseed);
		OreDictionary.registerOre("foodOil", ItemInit.canolaoil);
		OreDictionary.registerOre("foodCheese", ItemInit.cheese);
		OreDictionary.registerOre("bucketMilk", ItemInit.milk_pack);
		OreDictionary.registerOre("foodCream", ItemInit.whipping_cream);
		OreDictionary.registerOre("bread", ItemInit.toast);
		OreDictionary.registerOre("dustSalt", ItemInit.salt);
		OreDictionary.registerOre("dustSugar", Items.SUGAR);
		OreDictionary.registerOre("listAllbeefraw", Items.BEEF);
		OreDictionary.registerOre("listAllmeetraw", Items.BEEF);
		OreDictionary.registerOre("listAllmeetraw", Items.CHICKEN);
		OreDictionary.registerOre("listAllchikenraw", Items.CHICKEN);
		OreDictionary.registerOre("listAllmeatraw", Items.PORKCHOP);
		OreDictionary.registerOre("listAllporkraw", Items.PORKCHOP);
    }
}
