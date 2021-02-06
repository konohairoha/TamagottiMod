package tamagotti.init.items.armor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tamagotti.init.ItemInit;

public class BArmor extends ItemArmor {

	private final int data;

	public BArmor(String name, int render, EntityEquipmentSlot slot, int meta) {
		super(TArmorMaterial.bettyu, render, slot);
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

	//クラフト時にエンチャント
	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
		switch(data) {
		case 0:
			stack.addEnchantment(Enchantments.AQUA_AFFINITY, 3);
	    	stack.addEnchantment(Enchantments.PROJECTILE_PROTECTION, 6);
			break;
		case 1:
			stack.addEnchantment(Enchantments.BLAST_PROTECTION, 6);
	    	stack.addEnchantment(Enchantments.THORNS, 6);
			break;
		case 2:
			stack.addEnchantment(Enchantments.PROTECTION, 6);
	    	stack.addEnchantment(Enchantments.FEATHER_FALLING, 6);
			break;
		case 3:
			stack.addEnchantment(Enchantments.DEPTH_STRIDER, 5);
	    	stack.addEnchantment(Enchantments.FROST_WALKER, 4);
			break;
		}
    }

	//アイテムにダメージを与える処理を無効
	@Override
	public void setDamage(ItemStack stack, int damage) {
		return;
	}
}