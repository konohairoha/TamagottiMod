package tamagotti.init.items.tool.extend;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.base.BaseBlade;

public class ExSword extends BaseBlade {

	public ExSword(String name, ToolMaterial material, Double atack) {
		super(name, material, atack);
		this.speed = atack;
	}

	//エンティティに攻撃を与えるとそのエンティティにポーション効果を与える
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
    	stack.damageItem(1, attacker);
    	EntityPlayer player = (EntityPlayer) attacker;

		// 無敵時間をなくす
    	target.hurtResistantTime = 0;
    	this.attackAOE(stack, player, false, 10F + EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack), 0);	//与えるダメージ
        return true;
    }

	protected void attackAOE(ItemStack stack, EntityPlayer player, boolean slayAll, float damage, int emcCost) {
		World world = player.getEntityWorld();
		if (world.isRemote) { return; }

		float factor = 2.5F * (5 + (EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING, stack)) * 2);//ダメージを与える範囲
		AxisAlignedBB aabb = player.getEntityBoundingBox().grow(factor);
		List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
		if (list.isEmpty()) { return; }

		DamageSource src = DamageSource.causePlayerDamage(player);
		src.setDamageBypassesArmor();
		for (EntityLivingBase entity : list) {
			if (entity instanceof IMob) {

				// 無敵時間をなくす
				entity.hurtResistantTime = 0;
				entity.attackEntityFrom(src, damage);
				if (entity.getHealth() - damage <= 0) {
					int random = 25 + (EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, stack) * 5);
					world.spawnEntity(new EntityXPOrb(world, player.posX, player.posY, player.posZ, random));
				}
			}
		}
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    //ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		super.addInformation(stack, world, tooltip, advanced);
		if (Keyboard.isKeyDown(42)) {
			int range = 5 + (EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING, stack)) * 2;
			int damage = 10 + EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack);
			int xp = 25 + (EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, stack) * 5);
			tooltip.add(I18n.format(TextFormatting.BLUE + "攻撃時に範囲の敵にダメージ"));
			tooltip.add(I18n.format(TextFormatting.YELLOW + "攻撃範囲  ：" + range));
			tooltip.add(I18n.format(TextFormatting.YELLOW + "範囲攻撃力：" + damage));
			tooltip.add(I18n.format(TextFormatting.YELLOW + "モブを倒したときに " + xp + " の経験値を多くドロップ"));
		} else {
			tooltip.add(I18n.format(TextFormatting.RED + "左シフトで詳しく表示"));
		}
    }
}
