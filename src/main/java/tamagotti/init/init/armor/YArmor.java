package tamagotti.init.items.armor;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
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

public class YArmor extends ItemArmor {

	private final int data;
    AttributeModifier moveSpeed;

	public YArmor(String name, int render, EntityEquipmentSlot slot, int meta) {
		super(TArmorMaterial.yukari, render, slot);
        setUnlocalizedName(name);
        setRegistryName(name);
        this.data = meta;
        if (this.data == 2) {
            this.moveSpeed = new AttributeModifier("ArmorSpeed", 0.4D, 2);
        }
		ItemInit.itemList.add(this);
	}

	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
		Multimap<String, AttributeModifier> map = super.getItemAttributeModifiers(slot);
		if (this.data == 2 && slot == EntityEquipmentSlot.LEGS) {
			map.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), this.moveSpeed);
		}
		return map;
	}

	/**
	 * 0 = ヘルメット
	 * 1 = チェストプレート
	 * 2 = レギンス
	 * 3 = ブーツ
	 */

	//ポーション効果を付ける処理
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack){
		switch(data) {
		case 0:
			player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 200, 1, true, false));
			break;
		case 1:
			player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 200, 0, true, false));
			break;
		case 3:
			player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 200, 1, true, false));
			break;
		}

		if (player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof YArmor &&
				player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof YArmor &&
				player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() instanceof YArmor &&
				player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() instanceof YArmor) {
			player.addPotionEffect(new PotionEffect(PotionInit.yukari, 201, 0, true, false));
		}
	}

	//特定のアイテムで修復可能に
    @Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair){
        return repair.getItem() == ItemInit.yukaricrystal ? true : super.getIsRepairable(toRepair, repair);
    }

    //ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
  	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag){
  		if(this.data == 0) {
  			tooltip.add(I18n.format(TextFormatting.BLUE + "火炎耐性"));
  		} else if(this.data == 1) {
  			tooltip.add(I18n.format(TextFormatting.BLUE + "耐性"));
  		} else if(this.data == 2) {
  			tooltip.add(I18n.format(TextFormatting.BLUE + "移動速度アップ"));
  		} else if(this.data == 3) {
  			tooltip.add(I18n.format(TextFormatting.BLUE + "攻撃力アップ"));
  		}
  	}
}
