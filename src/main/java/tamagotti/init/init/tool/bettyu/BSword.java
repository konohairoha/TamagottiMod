package tamagotti.init.items.tool.bettyu;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tamagotti.init.items.tool.tamagotti.TSword;

public class BSword extends TSword {

	public BSword(String name, ToolMaterial material) {
		super(name, material, 0.0, 0);
	}

	//クラフト時にエンチャント
	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
    	stack.addEnchantment(Enchantments.LOOTING, 7);
    }

	//アイテムにダメージを与える処理を無効
	@Override
	public void setDamage(ItemStack stack, int damage) {
		return;
	}
}