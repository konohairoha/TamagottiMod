package tamagotti.plugin.sweetmagic.potrecipe;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import sweetmagic.api.recipe.pot.IPotRecipePlugin;
import sweetmagic.api.recipe.pot.PotRecipes;
import sweetmagic.api.recipe.pot.SMPotRecipePlugin;
import sweetmagic.util.OreItems;
import tamagotti.init.ItemInit;

@SMPotRecipePlugin(priority = EventPriority.LOW)
public class PotRecipePlugin implements IPotRecipePlugin {

	@Override
	public void registerPotRecipe(PotRecipes recipe) {

		// 出汁
		recipe.addRecipe(new PotRecipes(
			"bucketWater",
			new Object[] { new ItemStack(Items.FISH), new ItemStack(ItemInit.negi)},
			new ItemStack[] { new ItemStack(ItemInit.dasi, 4)}
		));

		// ざるそば
		recipe.addRecipe(new PotRecipes(
			new ItemStack(ItemInit.sobapowder),
			new Object[] { new ItemStack(ItemInit.sobapowder), "bucketWater"},
			new ItemStack[] { new ItemStack(ItemInit.soba, 2)}
		));

		// ざるうどん
		recipe.addRecipe(new PotRecipes(
			new OreItems("foodButter", 2),
			new Object[] { "bucketWater", "dustSalt"},
			new ItemStack[] { new ItemStack(ItemInit.udon, 2)}
		));

		// そば
		recipe.addRecipe(new PotRecipes(
			new ItemStack(ItemInit.soba),
			new Object[] { new ItemStack(ItemInit.dasi), new ItemStack(ItemInit.negi)},
			new ItemStack[] { new ItemStack(ItemInit.h_soba, 2)}
		));

		// うどん
		recipe.addRecipe(new PotRecipes(
			new ItemStack(ItemInit.udon),
			new Object[] { new ItemStack(ItemInit.dasi), new ItemStack(ItemInit.negi)},
			new ItemStack[] { new ItemStack(ItemInit.h_udon, 2)}
		));

		// 豚汁
		recipe.addRecipe(new PotRecipes(
			"listAllporkraw",
			new Object[] { "cropCarrot", new ItemStack(ItemInit.negi), new ItemStack(ItemInit.tofu) , "cropOnion", "bucketWater", new ItemStack(ItemInit.miso)},
			new ItemStack[] { new ItemStack(ItemInit.pork_soup, 4)}
		));

		// 味噌汁
		recipe.addRecipe(new PotRecipes(
			new ItemStack(ItemInit.tofu),
			new Object[] { new ItemStack(ItemInit.negi), new ItemStack(ItemInit.e_plant) , "bucketWater", new ItemStack(ItemInit.miso)},
			new ItemStack[] { new ItemStack(ItemInit.soysoup, 3)}
		));

		// とんかつ
		recipe.addRecipe(new PotRecipes(
			"listAllporkraw",
			new Object[] { "egg", new ItemStack(ItemInit.cabbage), new ItemStack(ItemInit.breadcrumbs) , "dustFlour", "foodOil"},
			new ItemStack[] { new ItemStack(ItemInit.pork_cutlet, 4) }
		));

		// 唐揚げ
		recipe.addRecipe(new PotRecipes(
			"listAllchikenraw",
			new Object[] { "egg", new ItemStack(ItemInit.cabbage), new ItemStack(ItemInit.breadcrumbs), "dustFlour", "foodOil"},
			new ItemStack[] { new ItemStack(ItemInit.df_chicken, 4) }
		));

		// きりたんぽ
		recipe.addRecipe(new PotRecipes(
			"cropRice",
			new Object[] { "dustSalt", "bucketWater", new ItemStack(ItemInit.miso)},
			new ItemStack[] { new ItemStack(ItemInit.kiritanpo, 3) }
		));

		// エビフライ
		recipe.addRecipe(new PotRecipes(
			new ItemStack(ItemInit.shrimp),
			new Object[] { "egg", new ItemStack(ItemInit.cabbage), new ItemStack(ItemInit.breadcrumbs), "dustFlour", "foodOil"},
			new ItemStack[] { new ItemStack(ItemInit.f_shrimp, 4) }
		));

	}
}
