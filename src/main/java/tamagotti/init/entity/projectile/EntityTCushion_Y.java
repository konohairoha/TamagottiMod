package tamagotti.init.entity.projectile;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import tamagotti.init.ItemInit;

public class EntityTCushion_Y extends EntityTCushion_A {

	public EntityTCushion_Y(World world) {
		super(world);
		this.setSize(0.4F, 0.25F);
		this.setSide(EnumFacing.DOWN);
	}

	@Override
	public double getMountedYOffset() {
		return -0.125D;
	}

	public EntityTCushion_Y(World world, double posX, double posY, double posZ) {
		this(world);
		this.setPosition(posX, posY, posZ);
	}

	public EntityTCushion_Y(World world, double posX, double posY, double posZ, @Nullable EntityPlayer player) {
		this(world, posX, posY, posZ);
		if (player != null)
			this.rotationYaw = player.rotationYaw;
	}

	@Override
	protected ItemStack drops() {
		return new ItemStack(ItemInit.tcushion_y);
	}
}
