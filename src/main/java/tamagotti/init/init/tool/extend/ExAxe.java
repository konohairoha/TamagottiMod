package tamagotti.init.items.tool.extend;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.items.tool.tamagotti.TAxe;
import tamagotti.init.items.tool.tamagotti.iitem.ITool;
import tamagotti.util.WorldHelper;

public class ExAxe extends TAxe implements ITool {

	public ExAxe(String name, ToolMaterial material, float damage, float speed) {
		super(name, material,damage,speed);
	}

	//エンティティに攻撃を与えるとそのエンティティにポーション効果を与える
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
    	stack.damageItem(1, attacker);
  		target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 80, 10));
        return true;
    }

    //手に持った時にポーション効果を付ける
    public void func_77663_a(ItemStack stack, World world, Entity entity, int off, boolean main) {
    	if((main || off < 1) && entity instanceof EntityPlayer) {
        	EntityPlayer player = (EntityPlayer) entity;
    		WorldHelper.ExAxe(world, player.getEntityBoundingBox().grow(7D, 3D, 7D));
		}
	}

	// ダメージを受けるときの処理
	@Override
	public float reduceDamage(DamageSource src, ItemStack stack) {
		return 1.0F;
	}

    // ダメージ倍率
	@Override
	public float increaceDamage(EntityLivingBase target, ItemStack stack) {
		float add = (float) (EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack) * 0.25);
		return 1.25F + add;
	}

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    //ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
  		tooltip.add(I18n.format(TextFormatting.GOLD + "半径7のプレイヤーに攻撃と採掘を付加"));
  		tooltip.add(I18n.format(TextFormatting.GOLD + "オフハンドに持つと攻撃力上昇"));
    }
}
