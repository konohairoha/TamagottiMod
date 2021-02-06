package tamagotti.init.items.tool.tamagotti;

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
import tamagotti.init.entity.projectile.EntityBettyu;
import tamagotti.init.entity.projectile.EntityTamagotti;
import tamagotti.util.TUtil;

public class TRush extends BaseBow {

	private final int data;

	public TRush(String name, int meta) {
		super(name);
		setMaxDamage(128);
        this.data = meta;
	}

	/**
	 * 0 = 無印
	 * 1 = ネオ
	 * 2 = カスタム
	 * 3 = リンク
	 * 4 = マダー
	 * 5 = リペア
	 * 6 = ベッチュー
	 */

	//右クリックチャージをやめたときに矢を消費せずに矢を射る
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {

		if (!(living instanceof EntityPlayer)) { return; }

		EntityPlayer player = (EntityPlayer) living;
		float f = TUtil.getArrowVelocity((this.getMaxItemUseDuration(stack) - timeLeft), 1F);
		float explode = 4;

		for(int i = 0; i <= this.data ; i++) {
			if(i != 0) {
				explode *= 4;
			}
		}

		if (stack.hasDisplayName()) { explode *= 1.25; }

		if (!world.isRemote) {
			if(this.data == 6 || this.data == 5) {
				int c = 0;
				if(this.data == 6) {
					c = 3;
				} else {
					c = 6;
				}
				EntityBettyu bettyu = new EntityBettyu(world, player, c);
	            bettyu.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, f * 3.0F, 1.0F);
	            world.spawnEntity(bettyu);
			} else if(this.data < 5) {
				EntityTamagotti tamagotti = new EntityTamagotti(world, player, explode);
			    tamagotti.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, f * 3.0F, 1.0F);
			    world.spawnEntity(tamagotti);
				stack.damageItem(1, player);
			}
			if (!player.capabilities.isCreativeMode) {
				player.getCooldownTracker().setCooldown(this, 30);
			}
		}

		world.playSound(player, new BlockPos(player), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
		player.addStat(StatList.getObjectUseStats(this));
	}

    //ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
  		if (stack.hasDisplayName()) {
  			tooltip.add(I18n.format(TextFormatting.GREEN + "特殊効果：爆発範囲増加 × 1.25"));
  		}
    }
}
