package tamagotti.init.items.tool.bettyu;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tamagotti.init.items.tool.tamagotti.TPick;

public class BPick extends TPick {

	public BPick(String name, ToolMaterial material) {
		super(name, material);
	}

	//クラフト時にエンチャント
	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
    	stack.addEnchantment(Enchantments.FORTUNE, 5);
    }

	//アイテムにダメージを与える処理を無効
	@Override
	public void setDamage(ItemStack stack, int damage) {
		return;
	}
}