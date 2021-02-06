package tamagotti.init.items.tool.tamagotti;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TRepair extends TItem {

	public int tickTime = 0;

	public TRepair (String name) {
		super(name);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) {

		if (!(entity instanceof EntityPlayer)) { return; }

		this.tickTime++;
		if (this.tickTime % 600 != 0) { return; }

		this.tickTime = 0;
		EntityPlayer player = (EntityPlayer) entity;
		IItemHandler inv = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		int size = inv.getSlots();

		for (int i = 0; i < size; i++) {

			ItemStack reStack = inv.getStackInSlot(i);

			if (reStack.isEmpty() || !reStack.getItem().isRepairable()) { continue; }

			if (reStack.getItemDamage() != 0) {
				reStack.setItemDamage(reStack.getItemDamage() - 2);
			}
		}
	}

	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format(TextFormatting.GREEN + "インベントリ内のアイテムを耐久値を少しづつ回復"));
  	}
}
