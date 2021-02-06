package tamagotti.init.items.tool.blade.voiceroid;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.ItemInit;
import tamagotti.init.base.BaseBow;
import tamagotti.init.entity.projectile.EntityKiritan;
import tamagotti.init.items.tool.tamagotti.iitem.IRelode;
import tamagotti.key.ClientKeyHelper;
import tamagotti.key.TKeybind;
import tamagotti.util.PlayerHelper;
import tamagotti.util.TUtil;

public class KiritanHo extends BaseBow implements IRelode {

	public KiritanHo(String name) {
		super(name);
		this.setMaxDamage(512);
	}

	//右クリックチャージをやめたときに矢を消費せずに矢を射る
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {

		EntityPlayer player = (EntityPlayer) living;
		int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
		int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
		int amount = 0; // 弾の個数
		float fin = 0; // 弾の端の座標
		float val = 0; // 弾の間隔

		if (stack.getItemDamage() < this.getMaxDamage(stack)) {

			if (player.isSneaking()) {

				amount = 9;
				fin = 1.2F;
				val = 4.8F;

				if (!player.capabilities.isCreativeMode) {
					player.getCooldownTracker().setCooldown(this, 30);
				}

			} else {

				amount = 2;
				fin = 0.6F;
				val = 0.3F;

				if (!player.capabilities.isCreativeMode) {
					player.getCooldownTracker().setCooldown(this, 5);
				}
			}

			EntityKiritan arrow[] = new EntityKiritan[amount];
			for (int i = 0; i < arrow.length; i++) {

				float width = i * fin;
				arrow[i] = new EntityKiritan(world, player);
				arrow[i].shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 3.0F + k, 1.0F);
				arrow[i].posX += (val - width);
				arrow[i].posZ -= (val - width);
				arrow[i].setDamage(arrow[i].getDamage() + j * 0.5D + 1D);

				if (!world.isRemote && stack.getItemDamage() + 8 < this.getMaxDamage(stack)) {
					world.spawnEntity(arrow[i]);
					stack.damageItem(1, player);
				}

				if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
					arrow[i].setFire(100);
				}
			}

			world.playSound(player, new BlockPos(player), SoundEvents.ENTITY_ARROW_SHOOT,
					SoundCategory.NEUTRAL, 0.5F, 2.0F);
		} else {

        	// 耐久回復(消費アイテム、データ値、個数、回復量)
        	TUtil.itemRecovery(player, stack, ItemInit.miso, 0, 4, this.getMaxDamage(stack));
		}
	}

	//右クリックをした際の挙動を弓に
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.NONE;
	}


	@Override
	public void doRelode(EntityPlayer player, ItemStack stack) {

		if (stack.getItemDamage() == 0) { return; }

		//インベントリ内のアイテム消費
		if (TUtil.getStackFromInventory(player.inventory.mainInventory, ItemInit.zundaarrow, 0, 1) != null) {

			if (stack.getItemDamage() == this.getMaxDamage(stack)) {

				// 耐久回復(消費アイテム、データ値、個数、回復量)
	        	TUtil.itemRecovery(player, stack, ItemInit.miso, 0, 4, this.getMaxDamage(stack));
			} else {

				// 耐久回復(消費アイテム、データ値、個数、回復量)
				TUtil.itemRecovery(player, stack, ItemInit.miso, 0, 1, 16);
			}
		}
		PlayerHelper.swingItem(player, player.getActiveHand());
	}

	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		tooltip.add(I18n.format(TextFormatting.GOLD + "スニーク右クリックで一斉射"));
		if (stack.getItemDamage() == this.getMaxDamage(stack)) {
			tooltip.add(I18n.format(TextFormatting.RED + ClientKeyHelper.getKeyName(TKeybind.RELODE) + "キーで味噌を4つ消費して耐久を全回復"));
  		} else {
			tooltip.add(I18n.format(TextFormatting.RED + ClientKeyHelper.getKeyName(TKeybind.RELODE) + "キーで味噌を1つ消費して耐久を16回復"));
  		}
    }
}
