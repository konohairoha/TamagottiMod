package tamagotti.init.items.tool.tamagotti;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
import tamagotti.init.event.TSoundEvent;

public class TBag extends TItem{

	public TBag (String name) {
		super(name);
		this.setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		if (!player.isSneaking() && hand == EnumHand.MAIN_HAND) {
			if (!world.isRemote) {
				player.openGui(TamagottiMod.INSTANCE, TGuiHandler.TPACK_GUI, world, hand == EnumHand.MAIN_HAND ? 0 : 1, -1, -1);
			}
			world.playSound(player, new BlockPos(player), TSoundEvent.TBAG, SoundCategory.AMBIENT, 0.5F, 1.0F);
		} else {

			ItemStack stack = player.getHeldItem(hand);
			NBTTagCompound tags = stack.getTagCompound();

			if (tags != null && tags.getInteger("y") > 0) {
				if (player.dimension != tags.getInteger("dim")) {
					player.changeDimension(tags.getInteger("dim"));
				}
				player.setPositionAndUpdate(tags.getInteger("x") + 0.5F, tags.getInteger("y") + 1F, tags.getInteger("z") + 0.5F);
				player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1, 1);
			}
		}
		return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack,world,tooltip,advanced);
  		tooltip.add(I18n.format(TextFormatting.GOLD + "右クリックでたまごっちリンクを収納するGUIを開く"));

		NBTTagCompound tags = stack.getTagCompound();
		if (tags == null) {
			tooltip.add(I18n.format(TextFormatting.RED + "座標未登録"));
		} else {
	  		tooltip.add(I18n.format(TextFormatting.GOLD + "シフト右クリックでテレポート"));
			tooltip.add(I18n.format(TextFormatting.GREEN + "登録した座標: " + tags.getInteger("x") + ", " + tags.getInteger("y") + ", " + tags.getInteger("z")));
			tooltip.add(I18n.format(TextFormatting.GREEN + "登録したディメンションID: " + tags.getInteger("dim")));
		}
  	}
}
