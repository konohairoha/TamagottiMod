package tamagotti.init.items.tool.tamagotti;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.TamagottiMod;
import tamagotti.handlers.TGuiHandler;
import tamagotti.init.base.BaseReturnItem;
import tamagotti.init.event.TSoundEvent;

public class TBook extends BaseReturnItem {

	public TBook(String name) {
		super(name);
	}

	@Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote) {
			player.openGui(TamagottiMod.INSTANCE, TGuiHandler.TBOOK_GUI, player.getEntityWorld(), hand == EnumHand.MAIN_HAND ? 0 : 1, -1, -1);
		}
		world.playSound(player, new BlockPos(player), TSoundEvent.PAGE, SoundCategory.AMBIENT, 0.5F, 1.0F);
		return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack,world,tooltip,advanced);
  		tooltip.add(I18n.format(TextFormatting.GOLD + "右クリックでクラフト画面"));
  	}
}