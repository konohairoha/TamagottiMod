package tamagotti.init.base;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStem;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.BlockInit;
import tamagotti.init.items.tool.tamagotti.TPick;
import tamagotti.util.WorldHelper;

public class BaseRangeBreak extends TPick {

	private final int cycle;

	public BaseRangeBreak(String name, ToolMaterial material, int size) {
		super(name, material);
        this.cycle = size;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,float hitX, float hitY, float hitZ) {

		// クリエ以外は行なわない
		if (!player.capabilities.isCreativeMode || !player.isSneaking()) { return EnumActionResult.FAIL; }

		ItemStack stack = player.getHeldItem(hand);
		EnumFacing sideHit = this.rayTrace(world, player, false).sideHit;
		int xa = 0, za = 0;	//向きに合わせて座標を変えるための変数
		int area = this.cycle + EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
		player.getCooldownTracker().setCooldown(this, 8);

		if (stack.hasDisplayName()) { area += 2; }

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

		for (int y = 0; y <= area * 2; y++) {
			for (int x = -area + xa; x <= area + xa; x++) {
				for (int z = -area + za; z <= area + za; z++) {

					//ブロックを取得するための定義
					BlockPos p = pos.add(x, y, z);
					IBlockState target = world.getBlockState(p);
					Block block = target.getBlock();

					//空気ブロックとたいるえんちちーなら何もしない
					if (block == Blocks.AIR || block.hasTileEntity(target)) { continue; }
					world.setBlockToAir(p);
				}
			}
		}

		return EnumActionResult.SUCCESS;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase living) {

		if (state.getBlockHardness(world, pos) > 0.0D && !world.isRemote) {

			EntityPlayer player = (EntityPlayer) living;
			EnumFacing sideHit = this.rayTrace(world, player, false).sideHit;
			int xa = 0, za = 0;	//向きに合わせて座標を変えるための変数
			int area = this.cycle + EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
			int silk = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack);
			boolean canSilk = silk > 0;
			int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
			player.getCooldownTracker().setCooldown(this, 8);

			if (stack.hasDisplayName()) { area += 2; }

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

			for (int y = 0; y <= area * 2; y++) {
				for (int x = -area + xa; x <= area + xa; x++) {
					for (int z = -area + za; z <= area + za; z++) {

						//ブロックを取得するための定義
						BlockPos p = pos.add(x, y, z);
						IBlockState target = world.getBlockState(p);
						Block block = target.getBlock();

						//空気ブロックとたいるえんちちーなら何もしない
						if (block == Blocks.AIR || block.hasTileEntity(target)) { continue; }

						drop.addAll(WorldHelper.getBlockDrops(world, player, target, block, p, canSilk, fortune));
						world.setBlockToAir(p);
					}
				}
			}

			//リストに入れたアイテムをドロップさせる
			WorldHelper.createLootDrop(drop, world, player.posX, player.posY, player.posZ);
			stack.damageItem(1, player);
		}
		return true;
	}

	public static void xToolBreak(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityPlayer player, EnumFacing face, NBTTagCompound tags, int a) {

		if (state.getBlockHardness(world, pos) > 0.0D && !world.isRemote) {

			int xa = 0, za = 0; //向きに合わせて座標を変えるための変数
			int area = (int) ((1 << tags.getInteger("level")) - Math.pow(tags.getInteger("level") - 1, 2));

			//上と下以外は採掘する座標を変える
			switch (face) {
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

			//ブロックを取得するための定義
			IBlockState target = world.getBlockState(pos.add(0, 0, 0));
			Block block = target.getBlock();

			if (a == 0 && target.getMaterial() == Material.ROCK) {
				//今進んでる座標と終わりの座標、壊すブロックを渡す
				BaseRangeBreak.xBreakBlock(world, stack, player, block, -area + xa, 0, -area + za, area, xa, za, pos);
			} else if (a == 1 ) {
				if (target.getMaterial() == Material.SAND || target.getMaterial() == Material.GROUND|| target.getMaterial() == Material.GRASS) {
					//今進んでる座標と終わりの座標、壊すブロックを渡す
					BaseRangeBreak.xBreakBlock(world, stack, player, block, -area + xa, 0, -area + za, area, xa, za, pos);
				}
			} else {
				if (target.getMaterial() == Material.WOOD) {
					//今進んでる座標と終わりの座標、壊すブロックを渡す
					BaseRangeBreak.xBreakBlock(world, stack, player, block, -area + xa, 0, -area + za, area, xa, za, pos);
				}
			}
        	stack.damageItem(1, player);
		}
	}

	// xツールシリーズ用
	public static void xBreakBlock(World world, ItemStack stack, EntityPlayer player, Block block, int getX, int getY, int getZ, int range, int xa, int za, BlockPos pos) {

		//リストの作成（めっちゃ大事）
		List<ItemStack> drop = new ArrayList<>();
		int silk = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack);
		boolean canSilk = silk > 0;
		int FOURTUNE = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);

		//渡された座標から再開
		for (BlockPos p : BlockPos.getAllInBox(pos.add(getX, getY, getZ), pos.add(range + xa, range * 2, range + za))) {

			//ブロックを取得するための定義
			IBlockState state = world.getBlockState(p);
			Block blocks = state.getBlock();

			//onBlockDestroyedから渡されたブロックと同じなら壊す
			if (blocks == block) {

				//リストに入れる
				List<ItemStack> bdrop = WorldHelper.getBlockDrops(world, player, state, blocks, p, canSilk, FOURTUNE);
				drop.addAll(bdrop);
				world.setBlockToAir(p);
			}
		}

		//リストに入れたアイテムをドロップさせる
		WorldHelper.createLootDrop(drop, world, player.posX, player.posY, player.posZ);
	}

	// 作物鎌用
	public static void sickleBreak(World world, ItemStack stack, EntityPlayer player, Block block, int getX, int getY, int getZ, int range, int xa, int za, BlockPos pos) {

		//リストの作成（めっちゃ大事）
		List<ItemStack> drop = new ArrayList<>();
		int silk = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack);
		boolean canSilk = silk > 0;
		int FOURTUNE = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);

		//渡された座標から再開
		for(int x = getX; x <= range + xa; x++) {
			for(int z = getZ; z <= range + za; z++) {
				for(int y = getY; y <= range * 2; y++) {

					//ブロックを取得するための定義
					BlockPos p1 = pos.add(x, y, z);
					IBlockState state = world.getBlockState(p1);
					Block blocks = state.getBlock();

					//空気ブロックとたいるえんちちーなら何もしない
					if(blocks == Blocks.AIR || blocks.hasTileEntity(state)){ continue; }

					boolean flag = true;

					if (blocks instanceof IGrowable) {
						flag = ((IGrowable) blocks).canGrow(world, p1, state, false);
					}

					if (blocks instanceof IShearable) {
						flag = !((IShearable) blocks).isShearable(stack, world, p1);
					}

					if (blocks == Blocks.PUMPKIN || blocks == Blocks.MELON_BLOCK) {
						flag = false;
					}

					if (blocks == Blocks.REEDS || blocks == BlockInit.bamboo) {
						Block under = world.getBlockState(p1.down()).getBlock();
						if (under == Blocks.REEDS || under == BlockInit.bamboo) {
							flag = false;
						}
					}

					//onBlockDestroyedから渡されたブロックと同じなら壊す
					if(blocks == block && !(blocks instanceof BlockStem) && !flag && !(blocks instanceof IShearable)) {

						//リストに入れる
						List<ItemStack> bdrop = WorldHelper.getBlockDrops(world, player, state, blocks, p1, canSilk, FOURTUNE);
						drop.addAll(bdrop);
						world.setBlockToAir(p1);
					}
				}
			}
		}

		//リストに入れたアイテムをドロップさせる
		WorldHelper.createLootDrop(drop, world, player.posX, player.posY, player.posZ);
	}

	//ツールチップの表示
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
		int area = this.cycle * 2 + 1 + EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack) * 2;
		if (stack.hasDisplayName()) {
			area += 4;
			tooltip.add(I18n.format(TextFormatting.GREEN + "特殊効果：範囲増加 + 4"));
		}
		if (EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack) < 1) {
			tooltip.add(I18n.format(TextFormatting.RED + "効率のエンチャントで採掘範囲増加"));
		}
		tooltip.add(I18n.format(TextFormatting.BLUE + (area + "×" + area + "×" + area + "を採掘")));
	}
}
