package tamagotti.init.items.armor;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import tamagotti.init.ItemInit;

public class RedberylArmor extends ItemArmor {

	public RedberylArmor(String name, int render, EntityEquipmentSlot slot) {
		super(TArmorMaterial.redberyl, render, slot);
        setUnlocalizedName(name);
        setRegistryName(name);
		ItemInit.itemList.add(this);
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair){
        return repair.getItem() == ItemInit.redberyl ? true : super.getIsRepairable(toRepair, repair);
    }
}