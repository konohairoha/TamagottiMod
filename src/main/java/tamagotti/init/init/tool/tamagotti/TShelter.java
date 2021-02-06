package tamagotti.init.items.tool.tamagotti;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.BlockDispenser;
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
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
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
import tamagotti.init.ItemInit;
import tamagotti.init.base.BaseBow;
import tamagotti.init.entity.projectile.EntityShotter;
import tamagotti.util.TUtil;

public class TShelter extends BaseBow {

	private final int data;

	public TShelter(String name, int meta, int value) {
		super(name);
        setMaxDamage(value);
        this.data = meta;
		this.addPropertyOverride(new ResourceLocation("blocking"), new IItemPropertyGetter() {
			@Override
			public float apply(ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
                return entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR);
    }

	//右クリックチャージをやめたときの処理
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {

		if (!(living instanceof EntityPlayer)) { return; }

		EntityPlayer player = (EntityPlayer) living;
		player.getEntityWorld().playSound(player, new BlockPos(player), SoundEvents.BLOCK_CLOTH_BREAK, SoundCategory.NEUTRAL, 1.0F, 1.0F);
		player.addStat(StatList.getObjectUseStats(this));

		if (stack.getItemDamage() != this.getMaxDamage(stack)) {
			stack.damageItem(1, player);
			if (!player.capabilities.isCreativeMode) {
	        	player.getCooldownTracker().setCooldown(this, 6 - this.data * 3);
	        }
		}
	}

	//右クリックをした際の処理
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		world.playSound(player, new BlockPos(player), SoundEvents.BLOCK_CLOTH_PLACE, SoundCategory.NEUTRAL, 1.0F, 1.0F);
		player.setActiveHand(hand);
		ItemStack stack = player.getHeldItem(hand);

        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
		int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
		int l = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack);
		if (stack.hasDisplayName()) { l++; }

		if (stack.getItemDamage() < this.getMaxDamage(stack)) {
			EntityShotter arrow[] = new EntityShotter[8 + (l * 4)];
			for(int i = 0; i < arrow.length; i++) {
				arrow[i] = new EntityShotter(world, player);
				arrow[i].setDamage(arrow[i].getDamage() + 2 + (this.data) + j * 0.25);
				arrow[i].setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 2.5F, 2.5F, 2.5F);
				if (!world.isRemote) {
					arrow[i].shoot(arrow[i].motionX, arrow[i].motionY, arrow[i].motionZ, (float) (2.5F + k + (this.data) * 0.75), 20);
					world.spawnEntity(arrow[i]);
				}
				if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
					arrow[i].setFire(60);
				}
			}
        } else {
        	// 耐久回復(消費アイテム、データ値、個数、回復量)
        	TUtil.itemRecovery(player, stack, ItemInit.tamagotti, 0, 1, this.getMaxDamage(stack));
        }
        return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	//右クリックをした際の挙動を盾に
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BLOCK;
	}

	//手に持った時にポーション効果を付ける
    public void func_77663_a(ItemStack stack, World world, Entity entity, int off, boolean main) {
    	if((main|| off < 1) && this.data == 1) {
	    	EntityLivingBase living = (EntityLivingBase) entity;
			living.addPotionEffect(new PotionEffect(MobEffects.SPEED, 0, 1));
		}
	}

	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {

		int l = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack);
		if (stack.hasDisplayName()) {
			l++;
  			tooltip.add(I18n.format(TextFormatting.GREEN + "特殊効果：射撃弾数増加 + 4"));
		}

  		int damage = (int) ((2 + (this.data) + EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack) * 0.25) * (8 + (l * 4)));
  		float shot = (float) (2.5 + (this.data) * 0.75 + EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack));
		if (stack.getItemDamage() == this.getMaxDamage(stack)) {
  			tooltip.add(I18n.format(TextFormatting.RED + "たまごっちを消費して耐久回復"));
  		}
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "最大威力：" + damage));
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "射撃速度：" + shot));
    }
}
