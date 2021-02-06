package tamagotti.init.items.armor;

import com.google.common.collect.Multimap;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import tamagotti.init.ItemInit;

public class FArmor extends ItemArmor {

	private final int data;
    AttributeModifier moveSpeed;

	public FArmor(String name, int render, EntityEquipmentSlot slot, int meta) {
		super(TArmorMaterial.fl, render, slot);
        setUnlocalizedName(name);
        setRegistryName(name);
        this.data = meta;
        this.moveSpeed = new AttributeModifier("ArmorSpeed", 0.2D, 2);
		ItemInit.itemList.add(this);
	}

	/**
	 * 0 = ヘルメット
	 * 1 = チェストプレート
	 * 2 = レギンス
	 * 3 = ブーツ
	 */

	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
		Multimap<String, AttributeModifier> map = super.getItemAttributeModifiers(slot);
		if ((this.data == 0 && slot == EntityEquipmentSlot.HEAD) ||
				(this.data == 1 && slot == EntityEquipmentSlot.CHEST) ||
				(this.data == 2 && slot == EntityEquipmentSlot.LEGS) ||
				(this.data == 3 && slot == EntityEquipmentSlot.FEET)) {
			map.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), this.moveSpeed);
		}
		return map;
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair){
		return repair.getItem() == ItemInit.fluorite ? true : super.getIsRepairable(toRepair, repair);
    }
}
