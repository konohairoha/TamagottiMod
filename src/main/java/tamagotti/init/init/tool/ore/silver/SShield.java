package tamagotti.init.items.tool.ore.silver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.BlockDispenser;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.items.tool.tamagotti.TItem;
import tamagotti.util.EventUtil;
import tamagotti.util.WorldHelper;

public class SShield extends TItem {

	public final int data;

	public SShield(String name, int meta) {
        super(name);
		this.setMaxStackSize(1);
		setMaxDamage(512);
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

	//最大１分間出来るように
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	// 右クリックをした時の処理
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		player.setActiveHand(hand);
		player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 6, 9));
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

	// 右クリックチャージをやめたときに矢を消費せずに矢を射る
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {
		if (living instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) living;
			player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 3, 9));
			player.getCooldownTracker().setCooldown(this, 6);
		}
	}

	// ガードで攻撃を跳ね返したときの処理
	@Override
	public boolean isShield(ItemStack stack, @Nullable EntityLivingBase entity) {
		return true;
	}

	// 手に持った時にポーション効果を付ける
	public void func_77663_a(ItemStack stack, World world, Entity entity, int off, boolean main) {

		EntityLivingBase living = (EntityLivingBase) entity;
		EntityPlayer player = (EntityPlayer) living;

		//スニーク時に
		if (player.isSneaking() && EventUtil.isGuard(living)) {
			if (this.data == 0) {
				WorldHelper.SShiled(world, player.getEntityBoundingBox().grow(10D, 5D, 10D));
			} else {
				WorldHelper.ShieldBarrier(world, player.getEntityBoundingBox().grow(2D, 1D, 2D), player);
			}
		}
	}

    // 敵に攻撃したら自分または相手にポーション効果
  	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {

  		if (this.data == 0) {

			stack.damageItem(4, attacker);
			EntityPlayer player = (EntityPlayer) attacker;
			player.getCooldownTracker().setCooldown(this, 60);
			DamageSource src = DamageSource.causePlayerDamage(player);
			ItemStack item = target.getHeldItemMainhand();
			target.attackEntityFrom(src, item.getMaxDamage() / 100);
			item.damageItem(256 * 256, target);

			ArrayList<ItemStack> damagea = new ArrayList<ItemStack>();
			Iterable<ItemStack> armor = target.getArmorInventoryList();
			if (armor != null && !armor.equals(Collections.<ItemStack> emptyList())) {
				for (ItemStack piece : target.getArmorInventoryList()) {
					if (!piece.isEmpty() && piece.isItemStackDamageable()) {
						damagea.add(piece);
						ItemStack targetPiece = damagea.get(target.getRNG().nextInt(damagea.size()));
						target.attackEntityFrom(src, targetPiece.getMaxDamage() / 200);
						targetPiece.damageItem(256 * 256, target);
					}
				}
			}
		}

		stack.damageItem(1, attacker);
		return true;
	}

  	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
  	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
  		if (this.data == 0) {
			if (Keyboard.isKeyDown(42)) {
				tooltip.add(I18n.format(TextFormatting.GOLD + "スニークガードで半径10のプレイヤーに耐久を付加"));
				tooltip.add(I18n.format(TextFormatting.GOLD + "攻撃時に相手にアイテムの耐久値分のダメージ"));
			} else {
				tooltip.add(I18n.format(TextFormatting.RED + "左シフトで詳しく表示"));
  			}
		} else {
  			tooltip.add(I18n.format(TextFormatting.GOLD + "スニークガードで半径2の射撃エンティティを消去"));
		}
  	}
}
