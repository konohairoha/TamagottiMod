package tamagotti.init.items.tool.tamagotti;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.items.tool.tamagotti.iitem.IAmulet;

public class TAmulet extends TItem implements IAmulet{

	public TAmulet(String name) {
		super(name);
		setMaxStackSize(1);
    }

	@Override
	public boolean isShrink() {
		return true;
	}

	@Override
	public int needExp() {
		return 0;
	}

	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format(TextFormatting.GREEN + "インベントリ内にあると死んでも経験値、防具、アイテムを保持"));
  	}
}