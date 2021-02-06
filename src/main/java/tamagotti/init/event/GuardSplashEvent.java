package tamagotti.init.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tamagotti.util.EventUtil;

public class GuardSplashEvent {

	// スプラッシュポーションをガードする
    @SubscribeEvent
	public void deleteSplashPotion(ProjectileImpactEvent.Throwable event) {

        // ポーション以外は処理しない
        if (!(event.getThrowable() instanceof EntityThrowable)) { return; }

        // EntityLivingに当たった
        RayTraceResult result = event.getRayTraceResult();
        if (result == null || !(result.entityHit instanceof EntityLivingBase)) { return; }

        // ガード中
        EntityLivingBase living = (EntityLivingBase)result.entityHit;
		if (!EventUtil.isGuard(living)) { return; }

        EntityThrowable throwable = event.getThrowable();
        Vec3d motionP = new Vec3d(throwable.motionX, 0, throwable.motionZ);

        // ガードしてるEntityのガード可能範囲内で、スプラッシュポーションがEntityに当たった。
		if (living.getLookVec().dotProduct(motionP) < 0.0D) {
            throwable.setDead();
            event.setCanceled(true);

            // ポーションなら、瓶の割れる音と、盾へのダメージ
			if (throwable instanceof EntityPotion) {
                living.playSound(SoundEvents.ENTITY_SPLASH_POTION_BREAK, 1.0F, living.getEntityWorld().rand.nextFloat() * 0.1F + 0.9F);
            }
            living.playSound(SoundEvents.ITEM_SHIELD_BLOCK, 1.0F, 0.8F + living.getEntityWorld().rand.nextFloat() * 0.4F);
        }
    }
}