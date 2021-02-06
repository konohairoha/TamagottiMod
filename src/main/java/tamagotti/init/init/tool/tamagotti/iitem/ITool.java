package tamagotti.init.items.tool.tamagotti.iitem;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public interface ITool {

	float reduceDamage(DamageSource src, ItemStack stack);

	float increaceDamage(EntityLivingBase target, ItemStack stack);
}
