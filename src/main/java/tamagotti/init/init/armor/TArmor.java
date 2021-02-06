package tamagotti.init.items.armor;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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

public class TArmor extends ItemArmor implements ISpecialArmor{

	private final int data;

	public TArmor(String name, int render, EntityEquipmentSlot slot, int meta) {
		super(TArmorMaterial.tamagotti, render, slot);
        setUnlocalizedName(name);
        setRegistryName(name);
		this.data = meta;
		ItemInit.itemList.add(this);
	}

	//特定のアイテムで修復可能に
    @Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair){
        return repair.getItem() == ItemInit.tamagotti ? true : super.getIsRepairable(toRepair, repair);
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, @Nonnull ItemStack armor, DamageSource source, double damage, int slot){
    	if (source.isExplosion()){
			if (this.data == 0) {
				return new ArmorProperties(1, 1.0D, 32);
			} else {
				return new ArmorProperties(1, 1.0D, 1024);
			}
		}
    	return new ArmorProperties(0, 0.3D, 0);
  	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		EntityEquipmentSlot type = ((TArmor) armor.getItem()).armorType;
		return (type == EntityEquipmentSlot.HEAD || type == EntityEquipmentSlot.CHEST|| type == EntityEquipmentSlot.LEGS|| type == EntityEquipmentSlot.FEET) ? 4 : 6;
	}

	//ポーション効果を付ける処理
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
		if (player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof TArmor &&
				player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof TArmor &&
				player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() instanceof TArmor &&
				player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() instanceof TArmor) {
			player.addPotionEffect(new PotionEffect(PotionInit.tamagotti, 201, this.data, true, false));
			player.addPotionEffect(new PotionEffect(PotionInit.concentration, 201, this.data, true, false));
		}
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {}

	//ツールチップの表示
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		if (this.data == 0) {
			tooltip.add(I18n.format(TextFormatting.BLUE + "爆破無効（たまごっち）"));
		} else {
			tooltip.add(I18n.format(TextFormatting.BLUE + "爆破無効（カスタム）"));
		}
	}
}