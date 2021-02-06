package tamagotti.init.items.tool.blade.voiceroid;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.base.BaseBow;
import tamagotti.init.entity.projectile.EntitySeika;
import tamagotti.util.TUtil;

public class SeikaRod extends BaseBow {

	private int area = 0;

	public SeikaRod(String name, ToolMaterial material){
		super(name);
	}

	//右クリックチャージをやめたときに矢を消費せずに矢を射る
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {

		if (!(living instanceof EntityPlayer)) { return; }

		EntityPlayer player = (EntityPlayer) living;
		int i = this.getMaxItemUseDuration(stack) - timeLeft;
		float f = TUtil.getArrowVelocity(i, 1F);

		if (stack.getItemDamage() < this.getMaxDamage(stack)) {
			EntitySeika arrow = new EntitySeika(world, player);
			arrow.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.0F, 0.0F);
			arrow.shoot(arrow.motionX, arrow.motionY, arrow.motionZ, 2.0F, 4);
			if (!world.isRemote) {
				world.spawnEntity(arrow);
				arrow.setDamage(arrow.getDamage() + 2 * f);
				stack.damageItem(1, player);
			}
			world.playSound(player, new BlockPos(player), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1F, 1F);
		}
		player.addStat(StatList.getObjectUseStats(this));
	}

	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
  		tooltip.add(I18n.format(TextFormatting.GOLD + "右クリックで作物を骨粉と同じように育てれる"));
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "効果半径：" + area));
    }
}
