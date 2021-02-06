package tamagotti.plugin.sweetmagic.freezerrecipe;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import sweetmagic.api.recipe.freezer.FreezerRecipes;
import sweetmagic.api.recipe.freezer.IFreezerRecipePlugin;
import sweetmagic.api.recipe.freezer.SMFreezerRecipePlugin;
import tamagotti.init.ItemInit;

@SMFreezerRecipePlugin(priority = EventPriority.LOW)
public class FreezerRecipePlugin implements IFreezerRecipePlugin {

	@Override
	public void registerFreezerRecipe(FreezerRecipes recipe) {

		// プリン
		recipe.addRecipe(new FreezerRecipes(
			"bucketMilk",
			new Object[] { "egg", "dustSugar" , new ItemStack(Blocks.ICE), "foodCream"},
			new ItemStack[] { new ItemStack(ItemInit.purin, 2)}
		));

		// チョコアイス
		recipe.addRecipe(new FreezerRecipes(
			new ItemStack(Items.DYE, 1, 3),
			new Object[] { "egg", "dustSugar", "bucketMilk" , new ItemStack(Blocks.ICE), "foodCream"},
			new ItemStack[] { new ItemStack(ItemInit.ice_choco, 2) }
		));

		// イチゴアイス
		recipe.addRecipe(new FreezerRecipes(
			"cropStrawberry",
			new Object[] { "egg", "dustSugar", "bucketMilk" , new ItemStack(Blocks.ICE), "foodCream"},
			new ItemStack[] { new ItemStack(ItemInit.ice_strawberries, 2) }
		));
	}
}
