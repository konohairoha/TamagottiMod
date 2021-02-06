package tamagotti.init.items.armor;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.ItemInit;
import tamagotti.init.PotionInit;

public class AArmor extends ItemArmor {

	private final int data;

	public AArmor(String name, int render, EntityEquipmentSlot slot, int meta) {
		super(TArmorMaterial.akane, render, slot);
        setUnlocalizedName(name);
        setRegistryName(name);
        this.data = meta;
		ItemInit.itemList.add(this);
	}

	/**
	 * 0 = ヘルメット
	 * 1 = チェストプレート
	 * 2 = レギンス
	 * 3 = ブーツ
	 */

	//ポーション効果を付ける処理
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
		switch(this.data) {
		case 0:
			player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 200, 1, true, false));
			break;
		case 1:
			player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 200, 1, true, false));
			break;
		case 2:
			player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 200, 1, true, false));
			if (!player.capabilities.isFlying) {
				if (player.moveForward < 0) {
					player.motionX *= 0.9;
					player.motionZ *= 0.9;
				}  else if (player.moveForward > 0 && player.motionX * player.motionX
						+ player.motionY * player.motionY + player.motionZ * player.motionZ < 3) {
					player.motionX *= 1.1;
					player.motionZ *= 1.1;
				}
			}
			break;
		case 3:
			player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 210, 1, true, false));
			break;
		}

		if (player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof AArmor &&
				player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof AArmor &&
				player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() instanceof AArmor &&
				player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() instanceof AArmor) {
			player.addPotionEffect(new PotionEffect(PotionInit.akane, 201, 0, true, false));
		}
	}

	//特定のアイテムで修復可能に
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == ItemInit.akanecrystal ? true : super.getIsRepairable(toRepair, repair);
    }

    //ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		if (this.data == 0) {
			tooltip.add(I18n.format(TextFormatting.BLUE + "水中呼吸"));
		} else if (this.data == 1) {
			tooltip.add(I18n.format(TextFormatting.BLUE + "採掘速度アップ"));
		} else if (this.data == 2) {
			tooltip.add(I18n.format(TextFormatting.BLUE + "跳躍力上昇"));
		} else if (this.data == 3) {
			tooltip.add(I18n.format(TextFormatting.BLUE + "暗視"));
		}
  	}
}
