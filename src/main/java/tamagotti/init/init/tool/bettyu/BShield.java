package tamagotti.init.items.tool.bettyu;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.BlockDispenser;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.items.tool.tamagotti.TItem;
import tamagotti.util.EventUtil;
import tamagotti.util.TUtil;

public class BShield extends TItem {

	public BShield(String name) {
        super(name);
		setMaxStackSize(1);
        setMaxDamage(1024);
		this.addPropertyOverride(new ResourceLocation("blocking"), new IItemPropertyGetter() {
			@Override
			public float apply(ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
				return entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1.0F : 0.0F;
			}
		});
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR);
    }

	// 右クリックをした際の挙動を弓に
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BLOCK;
	}

	// 最大１分間出来るように
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	// 右クリックをした時の処理
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		player.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

	// ガードで攻撃を跳ね返したときの処理
	@Override
	public boolean isShield(ItemStack stack, @Nullable EntityLivingBase entity) {
		return true;
	}

	//アイテムにダメージを与える処理を無効
	@Override
	public void setDamage(ItemStack stack, int damage) {
		return;
	}

  	public void func_77663_a(ItemStack stack, World world, Entity entity, int off, boolean main) {

		EntityPlayer player = (EntityPlayer) entity;
		if ((main || off < 1) && EventUtil.isGuard(player)) {

			AxisAlignedBB aabb = player.getEntityBoundingBox().grow(10D, 5D, 10D);
			List<EntityLiving> list = player.world.getEntitiesWithinAABB(EntityLiving.class, aabb);
			if (list.isEmpty()) { return; }
			for (EntityLiving liv : list) {
				if (liv instanceof IMob) {
					TUtil.tameAIAnger(liv, player);
				}
			}
		}
	}

  	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format(TextFormatting.GOLD + "ガード時にモンスターのターゲットを集める"));
  	}
}
