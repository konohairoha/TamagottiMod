package tamagotti.init.entity.projectile;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import tamagotti.util.EventUtil;

public class EntityRubyShot extends EntityRitter {

	private int rockTime;

	public EntityRubyShot(World worldIn) {
		super(worldIn);
	}

	public EntityRubyShot(World worldIn, EntityLivingBase throwerIn, int rockTime) {
		super(worldIn, throwerIn);
		this.rockTime = rockTime;
	}

	public EntityRubyShot(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

    // 自然消滅までの時間 30tick + this.plusTickAir
	@Override
	protected int plusTickAir() {
		return 10;
	}

	// 水中での速度減衰
	@Override
	protected float inWaterSpeed() {
		return 1F;
	}

	//えんちちーに当たった時の処理
	@Override
	protected void entityHit(EntityLivingBase living) {

		if (!(living instanceof EntityPlayer)) {
			// 敵を動かなくさせる
			EventUtil.tameAIDonmov((EntityLiving)living, this.rockTime);
		}
	}
}
