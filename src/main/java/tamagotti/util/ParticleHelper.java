package tamagotti.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class ParticleHelper {

	public static void spawnHeal(Entity entity, EnumParticleTypes type, int count, double speed, int... particleArgs) {
		if (entity.world instanceof WorldServer) {
			((WorldServer) entity.world).spawnParticle(type, entity.posX, entity.posY + entity.height * 0.5, entity.posZ, count, entity.width * 0.5, entity.height * 0.5, entity.width * 0.5, speed, particleArgs);
		}
	}

	public static void spawnBoneMeal(World world, BlockPos pos, EnumParticleTypes type) {
		if (world instanceof WorldServer) {
			((WorldServer) world).spawnParticle(type, pos.getX() + 0.5F, pos.getY() + 0.33F, pos.getZ() + 0.5F, 8, 0.25, 0.1, 0.25, 0, 0);
		}
	}
}
