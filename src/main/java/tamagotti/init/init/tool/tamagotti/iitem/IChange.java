package tamagotti.init.items.tool.tamagotti.iitem;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IChange {

	static final String TAG_ACTIVE = "Active";

	// 右クリックでモード切替
	boolean changeMode(@Nonnull EntityPlayer player, @Nonnull ItemStack stack);

	// tick処理
	void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5);
}
