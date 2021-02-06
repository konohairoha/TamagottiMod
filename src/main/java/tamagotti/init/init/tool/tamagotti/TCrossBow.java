package tamagotti.init.items.tool.tamagotti;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.TamagottiMod;
import tamagotti.init.ItemInit;
import tamagotti.init.base.BaseBow;
import tamagotti.init.entity.projectile.EntityTCross;
import tamagotti.init.event.TSoundEvent;
import tamagotti.util.ItemHelper;
import tamagotti.util.TUtil;

public class TCrossBow extends BaseBow {

	public static final String TAG_ACTIVE = "Active";
	public static final String TAG_MODE = "Mode";
	protected static final ResourceLocation ACTIVE_NAME = new ResourceLocation(TamagottiMod.MODID, "active");
	protected static final IItemPropertyGetter ACTIVE_GETTER = (stack, world, entity) -> stack.hasTagCompound() && stack.getTagCompound().getBoolean(TAG_ACTIVE) ? 1F : 0F;

	public TCrossBow(String name) {
        super(name);
        setMaxDamage(1024);
        this.addPropertyOverride(ACTIVE_NAME, ACTIVE_GETTER);
		this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter() {
			@Override
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
				if (entityIn == null) {
					return 0.0F;
				} else {
                    return entityIn.getActiveItemStack().getItem() != ItemInit.tcrossbow ? 0.0F : (stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0F;
                }
            }
        });
        this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter() {
            @Override
			@SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
    }

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return TUtil.shouldCauseReequipAnimation(oldStack, newStack, slotChanged, TAG_ACTIVE, TAG_MODE);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 20 - (EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) * 10);
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase living) {
		super.onItemUseFinish(stack, world, living);
		int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack);
		EntityPlayer player = (EntityPlayer) living;
		if (!world.isRemote) {
			player.getCooldownTracker().setCooldown(this, 16 - k * 6);
		}
		if (!ItemHelper.getNBT(stack).getBoolean(TAG_ACTIVE)) {
			stack.getTagCompound().setBoolean(TAG_ACTIVE, true);
		}
		world.playSound(player, new BlockPos(player), TSoundEvent.RELOAD, SoundCategory.AMBIENT, 0.3F, 1.0F);
		return stack;
	}

	//右クリックをした際の処理
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		ItemStack stack = player.getHeldItem(hand);

		// リロード済みの動き
		if (ItemHelper.getNBT(stack).getBoolean(TAG_ACTIVE)) {

			int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
			int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
			int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack);

			EntityTCross arrow = new EntityTCross(world, player);
			arrow.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.0F, 0.0F);
			arrow.shoot(arrow.motionX, arrow.motionY, arrow.motionZ, 4.0F + j, 0);
			arrow.setDamage(arrow.getDamage() + 12.5 + i);

			if (!world.isRemote) {
				world.spawnEntity(arrow);
			}

			if (k > 0) {
				arrow.setFire(100);
			}

			world.playSound(player, player.getPosition(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			stack.damageItem(1, player);

			if (!player.isSneaking()) {
				stack.getTagCompound().setBoolean(TAG_ACTIVE, false);
			} else {
				player.getCooldownTracker().setCooldown(this, 12 - k * 4);
				world.playSound(player, new BlockPos(player), TSoundEvent.RELOAD, SoundCategory.AMBIENT, 0.3F, 1.0F);
			}

			return new ActionResult(EnumActionResult.FAIL, stack);

		// 未リロードのときの動き
		} else {
			player.setActiveHand(hand);
			return new ActionResult(EnumActionResult.SUCCESS, stack);
		}
	}

	//右クリックをした際の挙動を弓に
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.NONE;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) {
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if (player.getHealth() <= 6 && !player.isPotionActive(MobEffects.REGENERATION)) {
				player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 2));
				stack.damageItem(50, player);
			}
		}
	}

	//ツールチップの表示
  	@Override
	@SideOnly(Side.CLIENT)
  	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
  		int damage = 12 + EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
  		int shot = 4 + (EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack));
  		int reload = 20 - (EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) * 10);
  		tooltip.add(I18n.format(TextFormatting.GREEN + "固有魔法：治癒の能力"));
  		tooltip.add(I18n.format(TextFormatting.GREEN + "体力6以下で耐久値を減らし再生能力を付加"));
		if (Keyboard.isKeyDown(42)) {
			tooltip.add(I18n.format(TextFormatting.GOLD + "射撃時に、スニークでクイックリロード"));
			tooltip.add(I18n.format(TextFormatting.YELLOW + "威力    ：" + damage));
			tooltip.add(I18n.format(TextFormatting.YELLOW + "射撃速度：" + shot));
			tooltip.add(I18n.format(TextFormatting.YELLOW + "リロード：" + reload));
		} else {
			tooltip.add(I18n.format(TextFormatting.RED + "左シフトで詳しく表示"));
		}
  	}
}
