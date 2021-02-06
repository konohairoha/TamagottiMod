package tamagotti.init.items.tool.blade;

import com.google.common.collect.Multimap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import tamagotti.init.items.tool.tamagotti.TSword;
import tamagotti.util.TUtil;

public class ABlade_0 extends TSword {

	public double speed;
	private final int data;

	public ABlade_0(String name, ToolMaterial material, Double atack, int meta) {
		super(name, material, atack, 0);
		this.speed = atack;
        this.data = meta;
    }

	//攻撃スピードなどの追加
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(slot);
		if (slot == EntityEquipmentSlot.MAINHAND) {
			multimap.removeAll(SharedMonsterAttributes.ATTACK_SPEED.getName());
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
					new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", 99995.0D + this.speed, 0));
			multimap.put(EntityPlayer.REACH_DISTANCE.getName(),
					new AttributeModifier(TUtil.TOOL_REACH, "Weapon modifier", 99999, 0));
		}
		return multimap;
	}

	@Override
	public void setDamage(ItemStack stack, int damage) {
		return;
	}

	//手に持った時にポーション効果を付ける
    @Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int off, boolean main) {
    	if(main|| off < 1) {
        	EntityLivingBase living = (EntityLivingBase) entity;
    		living.addPotionEffect(new PotionEffect(MobEffects.SPEED, 0, 4));
	     	living.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 0, 9));
	    }
	}

	//エンティティに攻撃を与えるとそのエンティティにポーション効果を与える
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
    	if(this.data == 1) {
    		target.onDeath(DamageSource.causePlayerDamage((EntityPlayer)attacker));
    		target.setHealth(0);
    	}
        return true;
    }
}