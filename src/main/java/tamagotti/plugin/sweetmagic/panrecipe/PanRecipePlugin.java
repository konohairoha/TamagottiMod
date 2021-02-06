package tamagotti.plugin.sweetmagic.panrecipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import sweetmagic.api.recipe.pan.IPanRecipePlugin;
import sweetmagic.api.recipe.pan.PanRecipes;
import sweetmagic.api.recipe.pan.SMPanRecipePlugin;
import sweetmagic.util.OreItems;
import tamagotti.init.ItemInit;

@SMPanRecipePlugin(priority = EventPriority.LOW)
public class PanRecipePlugin implements IPanRecipePlugin {

	@Override
	public void registerPanRecipe(PanRecipes recipe) {

		// ハンバーグ
		recipe.addRecipe(new PanRecipes(
			"listAllbeefraw",
			new Object[] {"cropCarrot", "cropTomato", "dustSalt", new ItemStack(ItemInit.breadcrumbs), "foodOil"},
			new ItemStack[] { new ItemStack(ItemInit.hamburger_steak, 3)}
		));

		// オムライス
		recipe.addRecipe(new PanRecipes(
			"cropRice",
			new Object[] {"egg", "cropTomato", "cropOnion", "listAllchikenraw", "dustSalt", "foodOil", "foodButter"},
			new ItemStack[] { new ItemStack(ItemInit.omurice, 3)}
		));

		// 餃子
		recipe.addRecipe(new PanRecipes(
			"listAllporkraw",
			new Object[] {"bucketWater", "dustSugar",
			new ItemStack(ItemInit.cabbage), new ItemStack(ItemInit.negi), "foodFlour", "foodButter"},
			new ItemStack[] { new ItemStack(ItemInit.gyoza, 4)}
		));

		// ホットケーキ
		recipe.addRecipe(new PanRecipes(
			new OreItems("foodButter", 2),
			new Object[] {"bucketWater", "foodButter", "bucketMilk", new ItemStack(ItemInit.kinako)},
			new ItemStack[] { new ItemStack(ItemInit.hotcake, 3)}
		));

		// 親子丼
		recipe.addRecipe(new PanRecipes(
			"cropRice",
			new Object[] {"egg", "dustSugar", "cropOnion", "listAllchikenraw", new ItemStack(ItemInit.dasi), "foodOil"},
			new ItemStack[] { new ItemStack(ItemInit.oyakodon, 3)}
		));
	}
}
