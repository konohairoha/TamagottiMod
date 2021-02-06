package tamagotti.init.items.tool.blade.voiceroid;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.base.BaseBlade;
import tamagotti.init.items.tool.tamagotti.iitem.ITool;

public class AncientSwordRemodel extends BaseBlade implements ITool {

	public AncientSwordRemodel(String name, ToolMaterial material, Double atack) {
		super(name, material, atack);
        this.speed = atack;
    }

	//右クリックをした際の処理
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        player.setActiveHand(hand);

		AxisAlignedBB aabb = player.getEntityBoundingBox().grow(32D, 16D, 32D);
		List<EntityLiving> list = player.world.getEntitiesWithinAABB(EntityLiving.class, aabb);
		if (list.isEmpty()) { return new ActionResult(EnumActionResult.PASS, player.getHeldItem(hand)); }

		for (EntityLiving liv : list) {
			if (liv instanceof IMob) {
	    		world.createExplosion(player, liv.posX, liv.posY + 0.5D, liv.posZ, 4.0F, false);
		        world.addWeatherEffect(new EntityLightningBolt(world, liv.posX, liv.posY, liv.posZ, false));
			}
		}
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
		tooltip.add(I18n.format(TextFormatting.GOLD + "範囲にいる敵モブに落雷と爆発を起こす"));
		tooltip.add(I18n.format(TextFormatting.GOLD + "メインハンドに持ってると爆破無効、燃焼無効"));
	}

	//特定のアイテムで修復可能に
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return repair.getItem() == Item.getItemFromBlock(Blocks.OBSIDIAN) ? true : super.getIsRepairable(toRepair, repair);
	}

	// ダメージを受けるときの処理
	@Override
	public float reduceDamage(DamageSource src, ItemStack stack) {
		float damage = 1F;
		if (src.isExplosion() || src.isFireDamage()) {
			damage = 0F;
		}
		return damage;
	}

    // ダメージ倍率
	@Override
	public float increaceDamage(EntityLivingBase target, ItemStack stack) {
		return 1.0F;
	}

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return true;
    }
}
