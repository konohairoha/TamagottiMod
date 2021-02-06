package tamagotti.init.tile.slot;

import java.util.function.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockSapling;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.oredict.OreDictionary;
import tamagotti.init.ItemInit;
import tamagotti.init.items.tool.tamagotti.TLink;

public class SlotPredicates {

	// 燃焼アイテムか
    public static final Predicate<ItemStack> FURNACE_FUEL = input -> !input.isEmpty() && (TileEntityFurnace.isItemFuel(input));

    // かまどレシピ
    public static final Predicate<ItemStack> SMELTABLE = input -> !input.isEmpty() && (!FurnaceRecipes.instance().getSmeltingResult(input).isEmpty());

	// たまごっちリンクか
    public static final Predicate<ItemStack> TLINK = input -> !input.isEmpty() && input.getItem() instanceof TLink;

	// たまごっちリンクか
    public static final Predicate<ItemStack> BOOKNEO = input -> !input.isEmpty() && !(input.getItem() == ItemInit.tamagotti_book_neo);

	// 全てのアイテム
    public static final Predicate<ItemStack> ALLITEM = input -> !input.isEmpty();

	// 作物のみ
    public static final Predicate<ItemStack> PLANT = input -> !input.isEmpty() && input.getItem() instanceof IPlantable || isSapling(input);

    public static boolean isSapling (ItemStack stack) {

    	Item item = stack.getItem();
    	if (item instanceof ItemBlock) {

    		Block block = ((ItemBlock) item).getBlock();
    		return block instanceof BlockSapling || isOreSapring(block);
    	}
    	return false;
    }

    public static boolean isOreSapring (Block block) {

    	// 鉱石辞書のidを入れる
		int[] oreIds = OreDictionary.getOreIDs(new ItemStack(block));
		if(oreIds.length == 0 || !(block instanceof BlockBush)) { return false; }

		//　鉱石辞書の名前を初期化
		String oreName = OreDictionary.getOreName(oreIds[0]);

		return oreName.equals("treesapling");
    }
}
