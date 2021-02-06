package tamagotti.init.items.tool.blade;

import java.util.List;

import com.google.common.collect.Multimap;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import tamagotti.init.ItemInit;
import tamagotti.init.base.BaseBlade;
import tamagotti.util.ParticleHelper;

public class TABlade_0 extends BaseBlade {

    public final AttributeModifier moveSpeed;
    public final int data;

	public TABlade_0(String name, ToolMaterial material, int data, Double atack, double speed) {
		super(name, material, atack);
		this.data = data;
		this.speed = atack;
        this.moveSpeed = new AttributeModifier("ArmorSpeed", speed, 2);
	}

	/**
	 * 0 = あかねブレード
	 * 1 = TA Ⅰ
	 * 2 = TA Ⅱ
	 * 3 = TA Ⅲ
	 */

	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
		Multimap<String, AttributeModifier> map = super.getItemAttributeModifiers(slot);
		if (slot == EntityEquipmentSlot.OFFHAND || slot == EntityEquipmentSlot.MAINHAND) {
			map.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), this.moveSpeed);
		}
		return map;
	}

    //特定のアイテムで修復
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == ItemInit.tamagotti ? true : super.getIsRepairable(toRepair, repair);
    }


    //敵に攻撃したら固定ダメージ
  	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {

  		if (this.data != 0 && attacker instanceof EntityPlayer) {

  			int dame = this.data;

  	        Vec3d vec3d = attacker.getLookVec();
  	        vec3d = vec3d.normalize().scale(this.data);

  	        // 座標取得
  	        BlockPos pos = new BlockPos(attacker.posX + vec3d.x, attacker.posY + vec3d.y, attacker.posZ + vec3d.z);

  	        AxisAlignedBB aabb = new AxisAlignedBB(pos.add(-this.data, -this.data, -this.data), pos.add(this.data, this.data, this.data));
  			List<EntityLivingBase> list = attacker.world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);

			for (EntityLivingBase entity : list) {
				if (!(entity instanceof IMob)) { continue; }

				entity.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) attacker), dame);
				entity.hurtResistantTime = 0;
				ParticleHelper.spawnHeal(entity, EnumParticleTypes.SWEEP_ATTACK, 4, dame);
			}
  		}

  		return super.hitEntity(stack, target, attacker);
  	}
}