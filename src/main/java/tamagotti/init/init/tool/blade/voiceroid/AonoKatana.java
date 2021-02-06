package tamagotti.init.items.tool.blade.voiceroid;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.items.tool.tamagotti.TSword;

public class AonoKatana extends TSword {

	public AonoKatana(String name, ToolMaterial material, Double atack) {
		super(name, material, atack, 0);
	}

	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
  		tooltip.add(I18n.format(TextFormatting.GOLD + "移動速度アップ付加、スニーク状態で透明"));
    }

    //手に持った時にポーション効果を付ける
    public void func_77663_a(ItemStack stack, World world, Entity entity, int off, boolean main) {
    	if(main|| off < 1) {
        	EntityLivingBase living = (EntityLivingBase) entity;
	    	if(living.isSneaking()) {
	    		living.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 20, 1));
			} else {
				living.addPotionEffect(new PotionEffect(MobEffects.SPEED, 20, 2));
			}
		}
	}

	//敵に攻撃したら自分にポーション効果
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
  		stack.damageItem(1, attacker);
  		target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 300, 1));
  		return true;
  	}

  	//特定のアイテムで修復可能に
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
  		return repair.getItem() == Item.getItemFromBlock(Blocks.PACKED_ICE) ? true : super.getIsRepairable(toRepair, repair);
  	}
}