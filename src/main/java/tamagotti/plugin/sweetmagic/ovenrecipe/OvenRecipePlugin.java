package tamagotti.plugin.sweetmagic.ovenrecipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import sweetmagic.api.recipe.oven.IOvenRecipePlugin;
import sweetmagic.api.recipe.oven.OvenRecipes;
import sweetmagic.api.recipe.oven.SMOvenRecipePlugin;
import tamagotti.init.ItemInit;

@SMOvenRecipePlugin(priority = EventPriority.LOW)
public class OvenRecipePlugin implements IOvenRecipePlugin {

	@Override
	public void registerOvenRecipe(OvenRecipes recipe) {

		// グラタン
		recipe.addRecipe(new OvenRecipes(
			new ItemStack(ItemInit.onion),
			new Object[] {"cropPotato", "dustSugar", "foodCheese", "bucketMilk", "listAllchikenraw"},
			new ItemStack[] { new ItemStack(ItemInit.gratin, 3)}
		));

		// ピザ
		recipe.addRecipe(new OvenRecipes(
			new ItemStack(ItemInit.pizza_cloth),
			new Object[] {"cropPotato", "foodCheese", "cropTomato", "listAllmeetraw"},
			new ItemStack[] { new ItemStack(ItemInit.pizza, 3)}
		));
	}
}
