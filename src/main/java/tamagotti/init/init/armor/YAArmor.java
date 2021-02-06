package tamagotti.init.items.armor;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Multimap;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.ItemInit;
import tamagotti.init.PotionInit;

public class YAArmor extends ItemArmor implements ISpecialArmor{

	private final int data;
	public int tickTime = 0;
    AttributeModifier maxHealth;

	public YAArmor(String name, int render, EntityEquipmentSlot slot, int meta) {
		super(TArmorMaterial.yukaaka, render, slot);
        setUnlocalizedName(name);
        setRegistryName(name);
        this.data = meta;
        this.maxHealth = new AttributeModifier("ArmorHealth", 5D, 0);
		ItemInit.itemList.add(this);
	}

	/**
	 * 0 = ヘルメット
	 * 1 = チェストプレート
	 * 2 = レギンス
	 * 3 = ブーツ
	 */

	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
		Multimap<String, AttributeModifier> map = super.getItemAttributeModifiers(slot);
		if ((this.data == 0 && slot == EntityEquipmentSlot.HEAD) ||
				(this.data == 1 && slot == EntityEquipmentSlot.CHEST) ||
				(this.data == 2 && slot == EntityEquipmentSlot.LEGS) ||
				(this.data == 3 && slot == EntityEquipmentSlot.FEET)) {
			map.put(SharedMonsterAttributes.MAX_HEALTH.getName(), this.maxHealth);
		}
		return map;
	}

	//ポーション効果を付ける処理
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {

		if (this.data == 0&& player.isInWater()) {
			player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 300, 1, true, false));
			player.setAir(300);
			if (player.moveForward < 0) {
				player.motionX *= 0.9;
				player.motionZ *= 0.9;
			}  else if (player.moveForward > 0 && player.motionX * player.motionX
					+ player.motionY * player.motionY + player.motionZ * player.motionZ < 5) {
				player.motionX *= 1.175;
				player.motionZ *= 1.175;
			} else {
				player.motionY *= 1.175d;
			}
		} else if (this.data == 2) {
			player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 10, 1, true, false));
			if (player.isInLava()) {
				tickTime++;
				if (tickTime >= 70) {
					tickTime = 0;
					player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 60, 1, true, false));
				}
			}

			player.setFire(0);
		} else if (this.data == 3 && !world.isRemote) {
				EntityPlayerMP playerMP = ((EntityPlayerMP) player);
				playerMP.fallDistance = 0;
		}

		player.removePotionEffect(MobEffects.HUNGER);
		player.removePotionEffect(MobEffects.POISON);
		player.removePotionEffect(MobEffects.WITHER);
		player.removePotionEffect(MobEffects.SLOWNESS);
		player.removePotionEffect(MobEffects.MINING_FATIGUE);
		player.removePotionEffect(MobEffects.NAUSEA);
		player.removePotionEffect(MobEffects.BLINDNESS);
		player.removePotionEffect(MobEffects.WEAKNESS);


		if (player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof YAArmor &&
				player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof YAArmor &&
				player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() instanceof YAArmor &&
				player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() instanceof YAArmor) {
			player.addPotionEffect(new PotionEffect(PotionInit.akane, 201, 2, true, false));
			player.addPotionEffect(new PotionEffect(PotionInit.yukari, 201, 2, true, false));
		}
	}

	@Override
    public ArmorProperties getProperties(EntityLivingBase player, @Nonnull ItemStack armor, DamageSource source, double damage, int slot){
		if(this.data == 1 && source.isExplosion()){
			return new ArmorProperties(1, 1D, 1024);
		}
    	return new ArmorProperties(2, 1.5D, 4);
  	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		EntityEquipmentSlot type = ((YAArmor) armor.getItem()).armorType;
		return (type == EntityEquipmentSlot.HEAD || type == EntityEquipmentSlot.CHEST|| type == EntityEquipmentSlot.LEGS|| type == EntityEquipmentSlot.FEET) ? 4 : 6;
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {}

	//特定のアイテムで修復可能に
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == ItemInit.starrosequartz ? true : super.getIsRepairable(toRepair, repair);
    }

    //ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
  	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag){
  		if(this.data == 0) {
  			tooltip.add(I18n.format(TextFormatting.BLUE + "水中呼吸"));
  		} else if(this.data == 1) {
  			tooltip.add(I18n.format(TextFormatting.BLUE + "爆破無効"));
  		} else if(this.data == 2) {
  			tooltip.add(I18n.format(TextFormatting.BLUE + "燃焼無効"));
  		} else if(this.data == 3) {
  			tooltip.add(I18n.format(TextFormatting.BLUE + "落下無効"));
  		}
  	}
}
