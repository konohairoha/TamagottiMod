package tamagotti.init.items.tool.cook;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.ItemInit;
import tamagotti.init.items.tool.tamagotti.TSword;

public class Knife extends TSword {

	public Knife(String name, ToolMaterial material, Double atack) {
		super(name, material, atack, 0);
	}

	@Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

	// クラフト後の処理
	@Override
	public ItemStack getContainerItem(ItemStack stack) {

		// 残り耐久値とダメージが一致したときは、アイテムが消える
		if (stack.getMaxDamage() == stack.getItemDamage()) {
			return ItemStack.EMPTY;
		} else {
			// それ以外は、ダメージを与えたアイテムを返す
			ItemStack newstack = stack.copy();
			newstack.setItemDamage(stack.getItemDamage() + 1);
			return newstack;
		}
	}

	//特定のアイテムで修復可能に
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return repair.getItem() == ItemInit.copperingot ? true : super.getIsRepairable(toRepair, repair);
	}

	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
  		tooltip.add(I18n.format(TextFormatting.GOLD + "クラフトしても帰って来る"));
  	}
}