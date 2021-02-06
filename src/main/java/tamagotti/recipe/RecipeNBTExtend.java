package tamagotti.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;
import tamagotti.init.items.tool.wand.RedberylWand;

public class RecipeNBTExtend extends ShapedOreRecipe {

	public static String SLOTCOUNT = "slotCount";

    public RecipeNBTExtend(ResourceLocation loc, ItemStack result, Object... recipe) {
        super(loc,result, recipe);
    }

    // クラフト時のリザルト
    @Override
    public ItemStack getCraftingResult(InventoryCrafting var1) {

    	// クラフト結果
        ItemStack result = super.getCraftingResult(var1);

		// 作業台のクラフトスロット分回す
		for (int idx = 0; idx < var1.getSizeInventory(); idx++) {

			// スロットのアイテムを受け取り
            ItemStack stack = var1.getStackInSlot(idx);

            // 空欄か剣ガードできる剣以外は次へ
            if(stack.isEmpty() || !stack.hasTagCompound()) { continue; }

    		NBTTagCompound tags = stack.getTagCompound();
    		result.setTagCompound(tags);

    		// レッドベリルかつスロットのNBTを持っていれば
    		if (result.getItem() instanceof RedberylWand && result.getTagCompound().hasKey(SLOTCOUNT)) {

    			RedberylWand resultItem = (RedberylWand) result.getItem();

    			// スロットを増やす
        		result.getTagCompound().setInteger(SLOTCOUNT, resultItem.slot);
    		}
			break;
        }
        return result;
    }
}
