package tamagotti.init.items.tool.tamagotti;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tamagotti.init.entity.projectile.EntityTClystal;
import tamagotti.init.entity.projectile.EntityTCushion_A;
import tamagotti.init.entity.projectile.EntityTCushion_Y;

public class TCushion extends TItem {

	public final int data;

	public TCushion(String name, int meta) {
		super(name);
		this.data = meta;
	}

	public Entity getPlacementEntity(World world, EntityPlayer player, double x, double y, double z, ItemStack item) {
		return new EntityTCushion_A(world, x, y, z, player);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float x, float y, float z) {
		ItemStack stack = player.getHeldItem(hand);
		if (!player.capabilities.isCreativeMode) { stack.shrink(1); }
		if (!world.isRemote) {
			BlockPos offPos = pos.offset(facing);
			Entity entity = null;
			if(this.data == 0) {
				entity = new EntityTCushion_A(world, x, y, z, player);
			} else if (this.data == 1) {
				entity = new EntityTCushion_Y(world, x, y, z, player);
			} else {
				entity = new EntityTClystal(world, x, y, z);
			}
			entity.setLocationAndAngles(offPos.getX() + 0.5F, offPos.getY() + 0.5F, offPos.getZ() + 0.5F, player.rotationYaw, 0.0F);
	    	world.spawnEntity(entity);
		}
		return EnumActionResult.SUCCESS;
	}
}