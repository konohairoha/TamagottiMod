package tamagotti.init.items.tool.wand;

import com.google.common.collect.Multimap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tamagotti.init.ItemInit;
import tamagotti.init.entity.projectile.EntityTArrow;
import tamagotti.util.TUtil;

public class TWand extends ItemSword {

	private final int data;

	public TWand(String name, ToolMaterial material, int meta) {
		super(material);
		setUnlocalizedName(name);
        setRegistryName(name);
        this.data = meta;
		ItemInit.itemList.add(this);
	}

	//クラフト時にエンチャント
	 @Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
		if (this.data == 0) {
			stack.addEnchantment(Enchantments.POWER, 7);
			stack.addEnchantment(Enchantments.FORTUNE, 5);
			stack.addEnchantment(Enchantments.LOOTING, 7);
		 } else {
			stack.addEnchantment(Enchantments.POWER, 9);
			stack.addEnchantment(Enchantments.FORTUNE, 7);
			stack.addEnchantment(Enchantments.LOOTING, 9);
		 }
	 }

	//アイテムにダメージを与える処理を無効化
	@Override
	public void setDamage(ItemStack stack, int damage) {
		return;
	}

	//壊すブロックの採掘速度を変更
	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		return this.data == 0 ? 12F : 18F;
	}

	//全てのブロック（マテリアル）を破壊可能に
	@Override
	public boolean canHarvestBlock(IBlockState blockIn) {
		return true;
	}

	//右クリックチャージをやめたときに矢を消費せずに矢を射る
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {

		EntityPlayer player = (EntityPlayer) living;
		float f = TUtil.getArrowVelocity((this.getMaxItemUseDuration(stack) - timeLeft), 1F);
        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
		EntityTArrow arrow = new EntityTArrow(world, player);
        arrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, f * 3.0F, 1.0F);

		if (!world.isRemote) {
			world.spawnEntity(arrow);
		}

		arrow.setIsCritical(f == 1F);

		if (j > 0) {
            arrow.setDamage(arrow.getDamage() + j * 0.5D + 0.5D);
        }

        world.playSound(player, new BlockPos(player), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
		player.addStat(StatList.getObjectUseStats(this));
	}

	//右クリックをした際の処理
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		player.setActiveHand(hand);
		return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	//最大１分間出来るように
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	//右クリックをした際の挙動を弓に
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	//攻撃スピードなどの追加
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
		Multimap<String, AttributeModifier> map = super.getItemAttributeModifiers(slot);
		if (slot == EntityEquipmentSlot.MAINHAND) {
			map.removeAll(SharedMonsterAttributes.ATTACK_SPEED.getName());
			map.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2, 0));
			map.put(EntityPlayer.REACH_DISTANCE.getName(), new AttributeModifier(TUtil.TOOL_REACH, "Weapon modifier", 6 + (this.data * 2), 0));
		}
		return map;
	}
}
