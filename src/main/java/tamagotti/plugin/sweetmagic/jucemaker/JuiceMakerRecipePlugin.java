package tamagotti.plugin.sweetmagic.jucemaker;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import sweetmagic.api.recipe.juicemaker.IJuiceMakerRecipePlugin;
import sweetmagic.api.recipe.juicemaker.JuiceMakerRecipes;
import sweetmagic.api.recipe.juicemaker.SMJuiceMakerRecipePlugin;
import tamagotti.init.ItemInit;

@SMJuiceMakerRecipePlugin(priority = EventPriority.LOW)
public class JuiceMakerRecipePlugin implements IJuiceMakerRecipePlugin {

	@Override
	public void registerJuiceMakerRecipe(JuiceMakerRecipes recipe) {

		// ホットココア
		recipe.addRecipe(new JuiceMakerRecipes(
			new ItemStack(Items.DYE, 1, 3),
			new Object[] { new ItemStack(Items.DYE, 1, 3), "bucketWater", "bucketMilk"},
			new ItemStack[] { new ItemStack(ItemInit.hot_cocoa, 3)}
		));
	}

}
