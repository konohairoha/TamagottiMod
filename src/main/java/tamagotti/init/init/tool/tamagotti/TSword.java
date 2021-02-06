package tamagotti.init.items.tool.tamagotti;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.ItemInit;

public class TSword extends ItemSword {

	public double speed;
	public final int damage;

	public TSword(String name, ToolMaterial material, Double atack, int dame) {
		super(material);
		setUnlocalizedName(name);
        setRegistryName(name);
		this.damage = dame;
        this.speed = atack;
		ItemInit.itemList.add(this);
	}

	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
        Multimap<String, AttributeModifier> map = super.getItemAttributeModifiers(slot);
		if (slot == EntityEquipmentSlot.MAINHAND) {
			map.removeAll(SharedMonsterAttributes.ATTACK_SPEED.getName());
			map.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4D + this.speed, 0));
		}
        return map;
    }

	//敵に攻撃したら固定ダメージ
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {

		// 無敵時間をなくす
		target.hurtResistantTime = 0;
  		stack.damageItem(1, attacker);

  		// たまごっちソードシリーズシリーズかつ攻撃力がHPを超えないなら
  		if (this.damage > 0) {

  			int dame = this.damage + EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack);
  			if (target.getHealth() - dame > 0) {
  				target.setHealth(target.getHealth() - dame);
  			}

			else if (attacker instanceof EntityPlayer) {
				target.onDeath(DamageSource.causePlayerDamage((EntityPlayer) attacker));
				target.setHealth(0);
  			}
  		}

  		// ルビーソードなら
  		if(stack.getItem() == ItemInit.rubysword && target.getHealth() <= 0) {
			Random rnd = new Random();
			int rand = rnd.nextInt(60) + 1 + (EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, stack) * 5);
			target.world.spawnEntity(new EntityXPOrb(target.getEntityWorld(), attacker.posX, attacker.posY, attacker.posZ, rand));
  		}
  		return true;
  	}

  	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
  		if(stack.getItem() == ItemInit.rubysword) {
  	  		int low = 1 + (EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, stack) * 5);
  	  		int max = 60 + (EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, stack) * 5);
  	  		tooltip.add(I18n.format(TextFormatting.YELLOW + "モブを倒したときに " + low + " ～ " + max + " の経験値を多くドロップ"));
  		}
  		if(this.damage > 0) {
  			int dama = this.damage + EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack);
  	  		tooltip.add(I18n.format(TextFormatting.YELLOW + "固定ダメージ:" + dama));
  		}
  	}
}
