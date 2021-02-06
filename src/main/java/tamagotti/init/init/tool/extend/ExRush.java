package tamagotti.init.items.tool.extend;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.base.BaseBow;
import tamagotti.init.entity.projectile.EntityTEx;
import tamagotti.util.TUtil;

public class ExRush extends BaseBow {

	public ExRush(String name) {
		super(name);
        setMaxDamage(6048);
	}

	// 右クリックチャージをやめたときに矢を消費せずに矢を射る
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {

		EntityPlayer player = (EntityPlayer) living;
		float f = TUtil.getArrowVelocity((this.getMaxItemUseDuration(stack) - timeLeft), 1F);

		if (!world.isRemote) {
			EntityTEx entity = new EntityTEx(world, player);
			entity.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, f * 3 + 3.0F, 1.0F);
			world.spawnEntity(entity);
			stack.damageItem(1, player);
		}

		if (!player.capabilities.isCreativeMode) {
			player.getCooldownTracker().setCooldown(this, 30);
		}

		world.playSound(player, new BlockPos(player), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
		player.addStat(StatList.getObjectUseStats(this));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack) {
		return true;
	}
}