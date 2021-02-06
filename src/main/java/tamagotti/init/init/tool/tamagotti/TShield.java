package tamagotti.init.items.tool.tamagotti;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.BlockDispenser;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.util.EventUtil;
import tamagotti.util.TUtil;
import tamagotti.util.WorldHelper;

public class TShield extends TItem {

	public final int data;

	public TShield(String name, int damage, int meta) {
        super(name);
		setMaxStackSize(1);
        setMaxDamage(damage);
        this.data = meta;
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

	// 敵に攻撃したら武器と防具を破壊
  	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		stack.damageItem(1, attacker);

		if (this.data < 1) { return true; }

		DamageSource src = DamageSource.causePlayerDamage((EntityPlayer) attacker);
		ItemStack item = target.getHeldItemMainhand();
		target.attackEntityFrom(src, item.getMaxDamage() / 50);
		item.damageItem(256 * 256, target);

		ArrayList<ItemStack> damagea = new ArrayList<ItemStack>();
		Iterable<ItemStack> armor = target.getArmorInventoryList();
		if (armor != null && !armor.equals(Collections.<ItemStack> emptyList())) {
			for (ItemStack piece : target.getArmorInventoryList()) {
				if (!piece.isEmpty() && piece.isItemStackDamageable()) {
					damagea.add(piece);
				}
			}

			if (!damagea.isEmpty()) {
				ItemStack targetPiece = damagea.get(target.getRNG().nextInt(damagea.size()));
				target.attackEntityFrom(src, targetPiece.getMaxDamage() / 50);
				targetPiece.damageItem(256 * 256, target);
			}
		}
		return true;
	}

  	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack) {
		return this.data == 2 ? true : stack.isItemEnchanted();
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
					liv.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 300, 0));
				}
			}

			if (this.data == 2) {
				WorldHelper.ShieldBarrier(world, player.getEntityBoundingBox().grow(2D, 2D, 2D), player);
			}
		}
	}

  	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {

		tooltip.add(I18n.format(TextFormatting.GREEN + "固有魔法：挑発の能力"));
		if (this.data < 1) { return; }

		if (Keyboard.isKeyDown(42)) {
			tooltip.add(I18n.format(TextFormatting.GOLD + "攻撃時に相手に耐久値分のダメージ"));
			if (this.data >= 2) {
				tooltip.add(I18n.format(TextFormatting.GOLD + "ガード時に半径５の射撃エンティティを消去"));
			}
		} else {
			tooltip.add(I18n.format(TextFormatting.RED + "左シフトで詳しく表示"));
		}
	}
}
