package tamagotti.init.items.tool.tamagotti;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.event.TSoundEvent;
import tamagotti.util.EventUtil;
import tamagotti.util.PlayerHelper;
import tamagotti.util.TUtil;
import tamagotti.util.WorldHelper;

public class THammer extends TAxe {

	public THammer(String name, ToolMaterial material, float damage, float speed) {
		super(name, material,damage,speed);
	}

	//敵に攻撃したら時の処理
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
  		stack.damageItem(1, attacker);
		EventUtil.tameAIDonmov((EntityLiving)target, 4);	// 敵を動かなくさせる
		TUtil.tameAIAnger((EntityLiving)target, null);	// タゲをnullに書き換え
  		return true;
  	}

	//壊すブロックの採掘速度を変更
	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		return 8.0F;
	}

	//全てのブロック（マテリアル）を破壊可能に
	@Override
	public boolean canHarvestBlock(IBlockState blockIn) {
		return true;
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

	//右クリックをした際の処理
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		player.setActiveHand(hand);
		return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	//右クリックチャージをやめたときに矢を消費せずに矢を射る
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {

		EntityPlayer player = (EntityPlayer) living;
		float f = TUtil.getArrowVelocity((this.getMaxItemUseDuration(stack) - timeLeft), 1F);

		if (f >= 1F) {
			float damage = 10 + EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack);
			int range = 2 + EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
			this.rangeAttack(stack, world, player, damage, range);
			PlayerHelper.swingItem(player, player.getActiveHand());
			player.getCooldownTracker().setCooldown(this, 60);
			world.playSound(null, new BlockPos(player), TSoundEvent.DAGEKI, SoundCategory.NEUTRAL, 0.5F, 1.0F);
		}
	}

	// 範囲攻撃
	public void rangeAttack(ItemStack stack, World world, EntityPlayer player, float damage, int area) {

		stack.damageItem(1, player);
		player.getCooldownTracker().setCooldown(this, 12);

		// 範囲攻撃 + タゲリセット
		AxisAlignedBB aabb = player.getEntityBoundingBox().grow(3D + area, 2.5D, 3D + area);
		List<EntityLiving> list = player.world.getEntitiesWithinAABB(EntityLiving.class, aabb);
		if (list.isEmpty()) { return; }
		DamageSource src = DamageSource.causePlayerDamage(player);

		for (EntityLiving liv : list) {
			if (!(liv instanceof EntityPigZombie)) {
				liv.attackEntityFrom(src, damage);

				EventUtil.tameAIDonmov(liv, 2); // 敵を動かなくさせる
				TUtil.tameAIAnger(liv, null); // タゲをnullに書き換え
			}
		}
	}


	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase living) {

		if (state.getBlockHardness(world, pos) > 0D && !world.isRemote) {

			EntityPlayer player = (EntityPlayer) living;
			EnumFacing sideHit = rayTrace(world, player, false).sideHit;
			int xa = 0, za = 0;	//向きに合わせて座標を変えるための変数
			int area = 2 + EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
			int silk = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack);
			boolean canSilk = silk > 0;
			int FOURTUNE = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);

			//上と下以外は採掘する座標を変える
            switch (sideHit) {
                case UP:
                case DOWN:
                	break;
                case NORTH:
                	za = area;
                	break;
                case SOUTH:
                	za = -area;
                	break;
                case EAST:
                	xa = -area;
                	break;
                case WEST:
                	xa = area;
                	break;
            }

    		//リストの作成（めっちゃ大事）
    		List<ItemStack> drop = new ArrayList<>();

			for (BlockPos p : BlockPos.getAllInBox(pos.add(-area + xa, 0, -area + za), pos.add(area + xa, area * 2, area + za))) {

				//ブロックを取得するための定義
				IBlockState target = world.getBlockState(p);
				Block block = target.getBlock();

				//空気ブロックとたいるえんちちーなら何もしない
				if (block == Blocks.AIR || block.hasTileEntity(target)) { continue; }

				drop.addAll(WorldHelper.getBlockDrops(world, player, target, block, p, canSilk, FOURTUNE));
				world.setBlockToAir(p);
			}

			//リストに入れたアイテムをドロップさせる
			WorldHelper.createLootDrop(drop, world, player.posX, player.posY, player.posZ);
			stack.damageItem(1, living);
		}
		return true;
	}

	//ツールチップの表示
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format(TextFormatting.GREEN + ("固有魔法：忘却の力")));
		if (Keyboard.isKeyDown(42)) {
			int area = 2 * 2 + 1 + EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack) * 2;
			tooltip.add(I18n.format(TextFormatting.GREEN + ("攻撃時に相手をスタン + タゲを失わせる")));
			tooltip.add(I18n.format(TextFormatting.GOLD + ("１秒右クリック溜めで範囲攻撃")));
			if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING, stack) < 1) {
				tooltip.add(I18n.format(TextFormatting.RED + "効率増加のエンチャントで採掘範囲、攻撃範囲増加"));
			}
			tooltip.add(I18n.format(TextFormatting.BLUE + (area + "×" + area + "×" + area + "を採掘")));
			tooltip.add(I18n.format(TextFormatting.BLUE
					+ ("攻撃範囲：" + (5 + EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack)))));
		} else {
			tooltip.add(I18n.format(TextFormatting.RED + "左シフトで詳しく表示"));
		}
	}
}
