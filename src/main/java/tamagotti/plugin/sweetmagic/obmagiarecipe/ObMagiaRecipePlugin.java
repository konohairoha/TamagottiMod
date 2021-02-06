package tamagotti.plugin.sweetmagic.obmagiarecipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import sweetmagic.api.recipe.obmagia.IObMagiaRecipePlugin;
import sweetmagic.api.recipe.obmagia.ObMagiaRecipes;
import sweetmagic.api.recipe.obmagia.SMObMagiaRecipePlugin;
import sweetmagic.init.ItemInit;

@SMObMagiaRecipePlugin(priority = EventPriority.LOW)
public class ObMagiaRecipePlugin implements IObMagiaRecipePlugin {

	@Override
	public void registerObMagiaRecipe(ObMagiaRecipes recipe) {

		// たまごっち魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(tamagotti.init.ItemInit.tamagotti),
			new ItemStack[] { new ItemStack(tamagotti.init.ItemInit.ruby), new ItemStack(ItemInit.mysterious_page),
					new ItemStack(ItemInit.sugarbell), new ItemStack(ItemInit.sticky_stuff_petal), new ItemStack(ItemInit.prizmium),
					new ItemStack(ItemInit.sannyflower_petal), new ItemStack(ItemInit.fire_nasturtium_petal), new ItemStack(ItemInit.blank_magic) },
			new ItemStack[] { new ItemStack(ItemInit.magic_blast) }
		));

		// エーテルライフル
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(tamagotti.init.ItemInit.ruby, 8),
			new ItemStack[] { new ItemStack(ItemInit.aether_crystal, 16), new ItemStack(ItemInit.mf_sbottle, 4), new ItemStack(tamagotti.init.ItemInit.yukaribarrel),
			new ItemStack(tamagotti.init.ItemInit.yukaristock), new ItemStack(tamagotti.init.ItemInit.yukaritank), new ItemStack(tamagotti.init.ItemInit.yukariscorp)},
			new ItemStack[] { new ItemStack(tamagotti.init.ItemInit.aether_rifle) }
		));
	}
}
