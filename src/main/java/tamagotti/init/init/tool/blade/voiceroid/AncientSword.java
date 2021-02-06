package tamagotti.init.items.tool.blade.voiceroid;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.base.BaseBlade;

public class AncientSword extends BaseBlade {

	public AncientSword(String name, ToolMaterial material, Double atack) {
		super(name, material, atack);
        this.speed = atack;
    }

	//右クリックをした際の処理
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        player.setActiveHand(hand);
        Vec3d vec3d = player.getLookVec();
        vec3d = vec3d.normalize().scale(10);
        world.addWeatherEffect(new EntityLightningBolt(world, player.posX + vec3d.x, player.posY + vec3d.y, player.posZ + vec3d.z, false));
		return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {
		stack.damageItem(1, living);
	}

	//ツールチップの表示
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, world,tooltip, advanced);
		tooltip.add(I18n.format(TextFormatting.BLUE + "向いた方向の10ブロック先に雷を発生"));
	}

	//特定のアイテムで修復可能に
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return repair.getItem() == Item.getItemFromBlock(Blocks.OBSIDIAN) ? true : super.getIsRepairable(toRepair, repair);
	}
}