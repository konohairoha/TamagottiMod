package tamagotti.init.items.tool.blade;

import com.google.common.collect.Multimap;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import tamagotti.init.base.BaseBlade;
import tamagotti.util.EventUtil;
import tamagotti.util.TUtil;

public class TABlade_2 extends BaseBlade {

	public TABlade_2(String name, ToolMaterial material, Double atack) {
		super(name, material, atack);
        this.speed = atack;
    }

	@Override
	public void setDamage(ItemStack stack, int damage) {
		return;
	}

    //エンティティに攻撃を与えるとそのエンティティにポーション効果を与える
    @Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {

		int a = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack) * 4;
		stack.damageItem(1, attacker);
		EntityPlayer player = (EntityPlayer) attacker;

		if (target instanceof EntityPlayer) { return true; }

		// 無敵時間をなくす
  		target.hurtResistantTime = 0;

		//一定以上の体力があると一定体力の体力にする
		if (target.getHealth() >= 50 - a) {
			target.setHealth(50 - a);
			player.getCooldownTracker().setCooldown(this, 400);
		}

		// 敵を動かなくさせる
		EventUtil.tameAIDonmov((EntityLiving)target, 1);
    	return true;
    }

    //右クリックでチャージした量で射程を伸ばす
  	public static float getArrowVelocity(int charge) {
  		float f = charge / 20.0F;
  		f = (f * f + f * 2.0F) / 3.0F * 20F;
  		return f;
  	}

  	//右クリックチャージをやめたときに矢を消費せずに矢を射る
  	@Override
  	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {
  		EntityPlayer player = (EntityPlayer) living;
  		int i = this.getMaxItemUseDuration(stack) - timeLeft;
		float f = getArrowVelocity(i);
		player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, (int) f * 5, 3));
  	}

	//攻撃スピードなどの追加
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
		Multimap<String, AttributeModifier> map = super.getItemAttributeModifiers(slot);
		if (slot == EntityEquipmentSlot.MAINHAND) {
			map.put(EntityPlayer.REACH_DISTANCE.getName(), new AttributeModifier(TUtil.TOOL_REACH, "Weapon modifier", 4, 0));
		}
		return map;
	}
}
