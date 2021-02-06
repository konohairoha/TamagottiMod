package tamagotti.init.event;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tamagotti.init.ItemInit;

public class TDeathEvent {

	// モブドロップイベント
	@SubscribeEvent
	public void onDrop(LivingHurtEvent event) {

		EntityLivingBase living = event.getEntityLiving();
		DamageSource source = event.getSource();
		float dam = event.getAmount();
		if (living == null || source.getTrueSource() != null || !(source.getTrueSource() instanceof EntityPlayer)) { return; }

		if (!living.world.isRemote && dam >= living.getMaxHealth()) {

			double x = living.posX;
			double y = living.posY;
			double z = living.posZ;
			World world = living.world;
			Random rand = world.rand;

			//一撃必殺か
			//		if (dam >= living.getMaxHealth()) {}
			//ずんだ弓の特殊キル効果
			//ここの条件：ずんだ弓で最大チャージした後、相手の体力が8以下であり敵にヒットしたときずんだ餅に変える
			/*if (!living.world.isRemote && EntityZunda.zunHit && dam >= living.getHealth()) {
				if (source.getTrueSource() != null && source.getTrueSource() instanceof EntityPlayer) {
					EntityItem drop = new EntityItem(living.world, living.posX, living.posY, living.posZ,
							new ItemStack(ItemInit.zunda));
					living.world.spawnEntity(drop);
					living.setHealth(0); //防具着てようがうるせーーーーーしらねーーーーーーーー！！
					EntityZunda.zunHit = false;
				}
			} else {
				EntityZunda.zunHit = false;
			}*/

			/**
			 * 		おまけ　EntityLivingタイプのEntityの座標から経験値をスポーンさせたいとき、
			 * 		このメソッドのliving (※ここでは倒している相手Entityを取得している) から基本的に変数を取得することを前提に、
			 * 		与える 「EntityXPOrb()」 の変数は　"world, posX, posY, posZ, int"　(int は整数型の経験値量)となる。
			 *		Entity exp = new EntityXPOrb(living.getEntityWorld(),living.posX,living.posY, living.posZ, 25);
			 *		living.world.spawnEntity(exp);
			 */

			//5分の1、クリーパー
			if (living instanceof EntityCreeper && rand.nextInt(5) == 0) {
				world.spawnEntity(new EntityItem(world, x, y, z, new ItemStack(ItemInit.tamagotti)));
				if (rand.nextInt(5) == 0) {
					world.spawnEntity(new EntityItem(world, x, y, z, new ItemStack(ItemInit.tamagotticustom)));
				} //当たりが出たらたまごっちカスタム
			}

			//2分の1、エンダーマン
			if (living instanceof EntityEnderman && rand.nextBoolean()) {
				EntityItem drop = new EntityItem(world, x, y, z, new ItemStack(ItemInit.telepo_book));
				world.spawnEntity(drop);
				if (rand.nextBoolean()) { // 当たりが出たらもう1個
					world.spawnEntity(drop);
				}
			}

			if (rand.nextInt(6) == 0) {

				//6分の1、ゾンビ
				if (living instanceof EntityZombie) {
					world.spawnEntity(new EntityItem(world, x, y, z, new ItemStack(ItemInit.copperingot)));
					if (rand.nextBoolean()) { // 当たりが出たら謎肉
						world.spawnEntity(new EntityItem(world, x, y, z, new ItemStack(ItemInit.unknown_meat)));
					}
				}

				// スケルトン
				if (living instanceof EntitySkeleton) {
					EntityItem drop = new EntityItem(world, x, y, z, new ItemStack(ItemInit.silveringot));
					world.spawnEntity(drop);
					if (rand.nextBoolean()) { // 当たりが出たらもう1個
						world.spawnEntity(drop);
					}
				}

				// ブレイズ
				if (living instanceof EntityBlaze) {
					EntityItem drop = new EntityItem(world, x, y, z, new ItemStack(ItemInit.ruby));
					world.spawnEntity(drop);
					if (rand.nextBoolean()) { // 当たりが出たらもう1個
						world.spawnEntity(drop);
					}
				}

				// ウィザスケ
				if (living instanceof EntityWitherSkeleton) {
					EntityItem drop = new EntityItem(world, x, y, z, new ItemStack(ItemInit.smokyquartz));
					world.spawnEntity(drop);
					if (rand.nextBoolean()) { // 当たりが出たらもう1個
						world.spawnEntity(drop);
					}
				}
			}
		}
	}
}
