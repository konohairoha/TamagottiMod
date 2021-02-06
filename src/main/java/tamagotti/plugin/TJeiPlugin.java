package tamagotti.plugin;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import net.minecraft.item.ItemStack;
import tamagotti.init.BlockInit;

@JEIPlugin
public class TJeiPlugin implements IModPlugin {

	@Override
	public void register(IModRegistry registry) {

		//BlackListにブロックなどを登録する　JEIで見せたくないアイテム用
		List<ItemStack> stackList = new ArrayList<ItemStack>();

		stackList.add( new ItemStack(BlockInit.quablock));
		stackList.add( new ItemStack(BlockInit.redblock));
		stackList.add( new ItemStack(BlockInit.lapisblock));
		stackList.add( new ItemStack(BlockInit.goldblock));
		stackList.add( new ItemStack(BlockInit.ironblock));
		stackList.add( new ItemStack(BlockInit.strawberryblock));
		stackList.add( new ItemStack(BlockInit.j_radishblock));
		stackList.add( new ItemStack(BlockInit.e_plantblock));
		stackList.add( new ItemStack(BlockInit.negiblock));
		stackList.add( new ItemStack(BlockInit.lettuceblock));
		stackList.add( new ItemStack(BlockInit.cabbageblock));
		stackList.add( new ItemStack(BlockInit.onionblock));
		stackList.add( new ItemStack(BlockInit.tomatoblock));
		stackList.add( new ItemStack(BlockInit.sobablock));
		stackList.add( new ItemStack(BlockInit.soyblock));
		stackList.add( new ItemStack(BlockInit.canolablock));
		stackList.add( new ItemStack(BlockInit.azukiblock));
		stackList.add( new ItemStack(BlockInit.riceblock));
		stackList.add( new ItemStack(BlockInit.bamboo));
		stackList.add( new ItemStack(BlockInit.sakuradoor0));
		stackList.add( new ItemStack(BlockInit.sakuradoor1));
		stackList.add( new ItemStack(BlockInit.sakuradoor2));
		stackList.add( new ItemStack(BlockInit.akari));
		stackList.add( new ItemStack(BlockInit.kogen_s));
		stackList.add( new ItemStack(BlockInit.frypan_1));
		stackList.add( new ItemStack(BlockInit.tdisplay_1));
		stackList.add( new ItemStack(BlockInit.pot_1));
		stackList.add( new ItemStack(BlockInit.potneo_1));
		stackList.add( new ItemStack(BlockInit.potcustom_1));
		stackList.add( new ItemStack(BlockInit.troom));
		stackList.add( new ItemStack(BlockInit.tchair_top));

		//ItemStackをArrayListに入れてブラックリストに入れていく。
		this.setBlackListItemStack(registry.getJeiHelpers().getIngredientBlacklist(), stackList);
	}

	private void setBlackListItemStack(IIngredientBlacklist blacklist, List<ItemStack> item) {
		for (ItemStack list : item) {
			blacklist.addIngredientToBlacklist(list);
		}
	}
}
