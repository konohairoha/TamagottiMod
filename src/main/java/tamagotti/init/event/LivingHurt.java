package tamagotti.init.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tamagotti.init.PotionInit;
import tamagotti.init.items.tool.tamagotti.iitem.ITool;
import tamagotti.init.items.tool.xtool.XSword;

public class LivingHurt {

	// ダメージを受けたときのイベント
	@SubscribeEvent
	public void onHurt(LivingHurtEvent event) {

		if (event.getEntityLiving() == null) { return; }

		EntityLivingBase living = event.getEntityLiving();
		float prev = 1F;
		float newDam = event.getAmount();
		DamageSource src = event.getSource();

		// プレイヤーの場合
		if (living instanceof EntityPlayer) {

			// ダメージカット
			prev = this.cutDamage(living, src, prev);
		} else {

			// 攻撃したえんちちーがプレイヤーのとき
			if (src.getTrueSource() instanceof EntityPlayer) {

				// 追加ダメージ
				newDam = this.addDamage(living, src, newDam);
			}
		}

		if (src.getTrueSource() instanceof EntityLivingBase) {

			// 攻撃時ポーションイベント
			newDam = this.activePotionDamecut(living, src, event, newDam);

			// 追加ダメージ
			newDam = this.activePotionaddAttack(living, src, newDam);

		}

		// ダメージ処理を無効か
		if (prev == 0F) {
			event.setAmount(0F);
			event.setCanceled(true);
		} else {
			event.setAmount(newDam);
		}

	}


	// メインハンド持ってるアイテムでダメージカット
	public float cutDamage (EntityLivingBase living, DamageSource src, float prev) {

		EntityPlayer player = (EntityPlayer) living;

		// メインハンドに持ってるアイテムを取得
		Item item = player.getHeldItemMainhand().getItem();
		if (item != null && item instanceof ITool) {

			// ダメージを減らす処理
			ITool stack = (ITool) item;
			prev = stack.reduceDamage(src, player.getHeldItemMainhand());
			player.setFire(0);
		}
		return prev;
	}

	// メイン、オフハンドで持ってるアイテムによってダメージ増加
	public float addDamage (EntityLivingBase living, DamageSource src, float newDam) {

		EntityPlayer player = (EntityPlayer) src.getTrueSource();
		Item offStack = player.getHeldItemOffhand().getItem();

		if (offStack != null && offStack instanceof ITool) {
			ITool stack = (ITool) offStack;
			return newDam *= stack.increaceDamage(living, player.getHeldItemOffhand());
		}

		Item mainStack = player.getHeldItemMainhand().getItem();
		if (mainStack != null && mainStack instanceof XSword) {
			ITool stack = (ITool) mainStack;
			return newDam *= stack.increaceDamage(living, player.getHeldItemMainhand());
		}
		return newDam;
	}

	// ポーション服用時に受けるダメージを減らす
	public float activePotionDamecut (EntityLivingBase living, DamageSource src, LivingHurtEvent event, float newDam) {

		// たまごっちポーション
		// ダメージカット処理
		if (living.isPotionActive(PotionInit.tamagotti) && (src.isExplosion() || src.isMagicDamage())) {
			PotionEffect effect = living.getActivePotionEffect(PotionInit.tamagotti);
			newDam = newDam / ((effect.getAmplifier() + 1) * 8);
		}

		// ゆかりポーション
		// 確率ダメージ1/4カット
		if (living.isPotionActive(PotionInit.yukari)) {

			// ダメージ軽減処理
			PotionEffect effect = living.getActivePotionEffect(PotionInit.yukari);
			int randValue = 16 - ((effect.getAmplifier() - 1) * 4);
			randValue = randValue <= 0 ? 1 : randValue;
			int rand = living.getEntityWorld().rand.nextInt(randValue);
			if (rand == 0) {

				// ダメージ跳ね返し
				EntityLivingBase entity = (EntityLivingBase) src.getTrueSource();
				entity.attackEntityFrom(src, newDam);

				newDam /= 4;
			}
		}

		return newDam;
	}

	// ポーション服用時に与えるダメージ増加
	public float activePotionaddAttack (EntityLivingBase living, DamageSource src, float newDam) {

		EntityLivingBase entity = (EntityLivingBase) src.getTrueSource();

		// 茜ポーション
		// 確率ダメージ1/4カット
		if (entity != null && entity.isPotionActive(PotionInit.akane)) {

			PotionEffect effect = entity.getActivePotionEffect(PotionInit.akane);
			int randValue = 16 - ((effect.getAmplifier() - 1) * 4);
			randValue = randValue <= 0 ? 1 : randValue;
			int rand = entity.getEntityWorld().rand.nextInt(randValue);
			if (rand == 0) {
				newDam *= 4;
			}
		}
		return newDam;
	}
}
