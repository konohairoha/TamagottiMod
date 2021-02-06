package tamagotti.init.items.tool.bettyu;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tamagotti.init.items.tool.tamagotti.TAxe;

public class BAxe extends TAxe {

	public BAxe(String name, ToolMaterial material, float damage, float speed) {
		super(name, material,damage,speed);
	}

	//クラフト時にエンチャント
	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
    	stack.addEnchantment(Enchantments.SMITE, 7);
    	stack.addEnchantment(Enchantments.BANE_OF_ARTHROPODS, 7);
    }

	//アイテムにダメージを与える処理を無効
	@Override
	public void setDamage(ItemStack stack, int damage) {
		return;
	}
}