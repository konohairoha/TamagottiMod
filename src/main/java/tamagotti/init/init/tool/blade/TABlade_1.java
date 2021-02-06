package tamagotti.init.items.tool.blade;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.util.ParticleHelper;

public class TABlade_1 extends TABlade_0 {

	public TABlade_1(String name, ToolMaterial material, Double atack, int data, double moveSpeed) {
		super(name, material, data, atack, moveSpeed);
        this.speed = atack;
    }

	/**
	 * 0 = 真
	 * 1 = 覚醒
	 * 2 = ゆかあか
	 */

	@Override
	public void setDamage(ItemStack stack, int damage) {
		return;
	}

	//クラフト時にエンチャント
    @Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
    	if(this.data == 1) {
        	stack.addEnchantment(Enchantments.SMITE, 5);
        	stack.addEnchantment(Enchantments.LOOTING, 5);
            stack.addEnchantment(Enchantments.SWEEPING, 5);
    	} else if(this.data == 2) {
    		stack.addEnchantment(Enchantments.FIRE_ASPECT, 5);
        	stack.addEnchantment(Enchantments.LOOTING, 5);
            stack.addEnchantment(Enchantments.SHARPNESS, 5);
    	}
    }

    //敵に攻撃したら固定ダメージ
  	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {

  		if (attacker instanceof EntityPlayer) {

  			World world = attacker.world;
  			int dame = this.data == 0 ? 3 : 5;

  	        Vec3d vec3d = attacker.getLookVec().normalize().scale(this.data);

  	        // 座標取得
  	        BlockPos pos = new BlockPos(attacker.posX + vec3d.x, attacker.posY + vec3d.y, attacker.posZ + vec3d.z);

  	        AxisAlignedBB aabb = new AxisAlignedBB(pos.add(-dame, -dame, -dame), pos.add(dame, dame, dame));
  			List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);

			for (EntityLivingBase entity : list) {
				if (!(entity instanceof IMob)) { continue; }

				entity.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) attacker), dame);
				entity.hurtResistantTime = 0;
				ParticleHelper.spawnHeal(entity, EnumParticleTypes.SWEEP_ATTACK, 2, 1);
			}

//			for (int i = 1; i < dame + 2; i++) {
//
//	  	        Vec3d vec = attacker.getLookVec().normalize().scale(i);
//
//	  	        // 座標取得
//	  	        BlockPos p = new BlockPos(attacker.posX + vec.x, attacker.posY + vec.y, attacker.posZ + vec.z);
//
//	  	        AxisAlignedBB aa = new AxisAlignedBB(p.add(-0.75, -0.75, -0.75), p.add(0.75, 0.75, 0.75));
//	  			List<EntityLivingBase> entityList = world.getEntitiesWithinAABB(EntityLivingBase.class, aa);
//
//	  			for (EntityLivingBase liv : entityList) {
//	  				if (!(liv instanceof IMob)) { continue; }
//
//	  				liv.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) attacker), dame);
//					liv.hurtResistantTime = 0;
//					ParticleHelper.spawnHeal(liv, EnumParticleTypes.SWEEP_ATTACK, 4, dame);
//	  			}
//			}
  		}
    	return true;
    }

    //手に持った時にポーション効果を付ける
    public void func_77663_a(ItemStack stack, World world, Entity entity, int off, boolean main) {
    	EntityLivingBase living = (EntityLivingBase) entity;
		if (main || off == 0) {
			if (this.data == 1 || this.data == 2) {
				living.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 200, 1));
			}
	    }
	}

    //ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
  		if (stack.hasDisplayName()) {
			tooltip.add(I18n.format(TextFormatting.GREEN + "特殊効果：範囲攻撃(攻撃力4)"));
  		}
  		tooltip.add(I18n.format(TextFormatting.GOLD + "剣ガードが可能"));
    }
}
