package tamagotti.recipe;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;
import tamagotti.init.base.BaseRangeBreak;

public class RecipeEnchantExtend extends ShapedOreRecipe {

    public RecipeEnchantExtend(ResourceLocation loc, ItemStack result, Object... recipe) {
        super(loc,result, recipe);
    }

    // クラフト時のリザルト
    @Override
    public ItemStack getCraftingResult(InventoryCrafting var1) {

    	// クラフト結果
        ItemStack result = super.getCraftingResult(var1);

        // エンチャのMapの定義
        Map<Enchantment,Integer> all = Maps.newHashMap();

		// 作業台のクラフトスロット分回す
		for (int idx = 0; idx < var1.getSizeInventory(); idx++) {

			// スロットのアイテムを受け取り
            ItemStack stack = var1.getStackInSlot(idx);

            // 空欄か剣ガードできる剣以外は次へ
            if(stack.isEmpty() || (!(stack.getItem() instanceof ItemSword) && !(stack.getItem() instanceof BaseRangeBreak))
            		&& !(stack.getItem() instanceof ItemBow)) { continue; }

            // 現在のエンチャントを取得
            all.putAll(EnchantmentHelper.getEnchantments(stack));
        }

		// クラフト結果のアイテムにエンチャント
		EnchantmentHelper.setEnchantments(all, result);

        return result;
    }
}
