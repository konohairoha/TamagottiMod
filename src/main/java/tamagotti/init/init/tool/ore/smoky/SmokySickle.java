package tamagotti.init.items.tool.ore.smoky;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStem;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.BlockInit;
import tamagotti.init.items.tool.tamagotti.TShovel;
import tamagotti.util.TUtil;
import tamagotti.util.WorldHelper;

public class SmokySickle extends TShovel {

	private final int cycle;

	public SmokySickle(String name, ToolMaterial material, int size) {
		super(name, material);
        this.cycle = size;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase living) {

		if (!world.isRemote) {
			EntityPlayer player = (EntityPlayer) living;
			int area = this.cycle + EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
			List<ItemStack> drop = new ArrayList<>();
			int silk = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack);
			boolean canSilk = silk > 0;
			int FOURTUNE = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);

			 for (int x = -area; x <= area; x++) {
				for (int z = -area; z <= area; z++) {
					for (int y = 0; y <= area; y++) {

						BlockPos p1 = pos.add(x, y, z);
						IBlockState target = world.getBlockState(p1);
						Block block = target.getBlock();

						//空気ブロックとたいるえんちちーなら何もしない
						if (block == Blocks.AIR || block.hasTileEntity(target)) { continue; }

						if (living.isSneaking()) {
							stack = new ItemStack(Items.SHEARS);
						}

						if (getDestroySpeed(stack, target) >= 5.0F && !(block instanceof BlockStem)) {

							boolean flag = true;
							if (block instanceof IGrowable) {
								flag = ((IGrowable) block).canGrow(world, p1, target, false);
							}

							if (block instanceof IShearable) {
								flag = !((IShearable) block).isShearable(stack, world, p1);
							}

							if (block == Blocks.PUMPKIN || block == Blocks.MELON_BLOCK) {
								flag = false;
							}

							if (block == Blocks.REEDS || block == BlockInit.bamboo) {
								Block under = world.getBlockState(p1.down()).getBlock();
								if (under == Blocks.REEDS || under == BlockInit.bamboo) {
									flag = false;
								}
							}

							if (!flag) {

								//リストに入れる
								drop.addAll(WorldHelper.getBlockDrops(world, player, target, block, p1, canSilk, FOURTUNE));
								world.setBlockToAir(p1);
							}
						}
					}
				}
			}
			WorldHelper.createLootDrop(drop, world, player.posX, player.posY, player.posZ);
		}
		stack.damageItem(1, living);
		return true;
	}

	// 毛刈り
	@Override
	public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
		if (entity.world.isRemote) { return false; }
		if (player.isSneaking() && entity instanceof IShearable) {
			IShearable target = (IShearable) entity;
			BlockPos pos = new BlockPos(entity.posX, entity.posY, entity.posZ);
			if (target.isShearable(itemStack, entity.world, pos)) {
				List<ItemStack> drops = target.onSheared(itemStack, entity.world, pos, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemStack));
				Random rand = Item.itemRand;
				for (ItemStack stack : drops) {
					EntityItem ent = entity.entityDropItem(stack, 1.0F);
					ent.motionY += rand.nextFloat() * 0.05F;
					ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
					ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
				}
				player.world.playSound(player, entity.getPosition(), SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.PLAYERS, 1.5F, 1.5F);
				itemStack.damageItem(1, entity);
			}
			return true;
		}
		return false;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		Material material = state.getMaterial();
		return material != Material.PLANTS && material != Material.VINE && material != Material.CORAL && material != Material.LEAVES && material != Material.GOURD ? 1.0F : 15.0F;
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn) {
		return true;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float par8, float par9, float par10) {

		if (world.isRemote || !player.canPlayerEdit(pos, facing, player.getHeldItem(hand))) {
			return EnumActionResult.FAIL;
		}

		if (player.isSneaking()) {

			int cutStack = 0;
			if (player.getHeldItem(hand).hasDisplayName()) {
				cutStack -= 2;
			}

			//骨粉がインベントリにあるかどうか
			Object[] obj = TUtil.getStackFromInventory(player.inventory.mainInventory, Items.DYE, 15, 4 + cutStack);

			if (obj == null) { return EnumActionResult.FAIL; }

			ItemStack boneMeal = (ItemStack) obj[1];

			if (!boneMeal.isEmpty() && this.useBoneMeal(world, pos)) {
				player.inventory.decrStackSize((Integer) obj[0], 4 + cutStack);
				player.inventoryContainer.detectAndSendChanges();
				return EnumActionResult.SUCCESS;
			}
			return EnumActionResult.FAIL;
		}
		player.world.playSound(null, new BlockPos(player),SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.PLAYERS, 1.0F, 1.0F);
		return this.plantSeeds(world, player, pos, hand) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
	}

	private boolean useBoneMeal(World world, BlockPos pos) {

		boolean result = false;
		for (BlockPos bpos : BlockPos.getAllInBoxMutable(pos.add(-2, 0, -2), pos.add(2, 0, 2))) {

			//ブロックを取得するための定義
			IBlockState state = world.getBlockState(bpos);
			Block crop = state.getBlock();

			if (crop instanceof IGrowable) {
				IGrowable growable = (IGrowable) crop;

				if (growable.canUseBonemeal(world, world.rand, bpos, state)) {
					if (!result) {
						result = true;
					}
					growable.grow(world, world.rand, bpos.toImmutable(), state);
				}
			}
		}
		return result;
	}

	//種植える
	private boolean plantSeeds(World world, EntityPlayer player, BlockPos pos, EnumHand hand) {

        boolean result = false;
        ItemStack stack = player.getHeldItem(hand);
        int barea = this.cycle + EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
        List<StackWithSlot> seeds = getAllSeeds(player.inventory.mainInventory);

		if (seeds.isEmpty()) { return false; }

		for (BlockPos currentPos : BlockPos.getAllInBox(pos.add(-barea, 0, -barea), pos.add(barea, 0, barea))) {
            IBlockState state = world.getBlockState(currentPos);
            if (world.isAirBlock(currentPos)) { continue; }
            IPlantable plant;

			for (int i = 0; i < seeds.size(); i++) {
                StackWithSlot s = seeds.get(i);
				if (s.stack.getItem() instanceof IPlantable) {
                    plant = (IPlantable) s.stack.getItem();
                } else {
                    plant = (IPlantable) Block.getBlockFromItem(s.stack.getItem());
                }
                if (state.getBlock().canSustainPlant(state, world, currentPos, EnumFacing.UP, plant) && world.isAirBlock(currentPos.up())) {
                    world.setBlockState(currentPos.up(), plant.getPlant(world, currentPos.up()));
                    player.inventory.decrStackSize(s.slot, 1);
                    player.inventoryContainer.detectAndSendChanges();
                    s.stack.shrink(1);

					if (s.stack.isEmpty()) {
                        seeds.remove(i);
                    }

					if (!result) {
                        result = true;
                    }
                }
            }
        }
        stack.damageItem(1, player);
        return result;
    }

	private List<StackWithSlot> getAllSeeds(NonNullList<ItemStack> inv) {
		List<StackWithSlot> result = new ArrayList<>();
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.get(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof IPlantable) {
					result.add(new StackWithSlot(stack, i));
					continue;
				}
				Block block = Block.getBlockFromItem(stack.getItem());
				if (block != null && block instanceof IPlantable) {
					result.add(new StackWithSlot(stack, i));
				}
			}
		}
		return result;
	}


	private static class StackWithSlot {

		public final int slot;
		public final ItemStack stack;

		public StackWithSlot(ItemStack stack, int slot) {
			this.stack = stack.copy();
			this.slot = slot;
		}
	}

	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {

		int area = this.cycle + + EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
		if (stack.hasDisplayName()) {
			tooltip.add(I18n.format(TextFormatting.GREEN + "特殊効果：必要骨粉量 -2"));
		}

		if (Keyboard.isKeyDown(42)) {
			tooltip.add(I18n.format(TextFormatting.GOLD + "骨粉を持ってスニーク右クリックで範囲骨粉成長"));
			tooltip.add(I18n.format(TextFormatting.GOLD + "種を持って右クリックで範囲種植え"));
			tooltip.add(I18n.format(TextFormatting.GOLD + "羊に右クリックで羊毛回収"));
			tooltip.add(I18n.format(""));
			tooltip.add(I18n.format(TextFormatting.BLUE + "種植え範囲(半径) ：" + area));
			tooltip.add(I18n.format(TextFormatting.BLUE + "作物破壊範囲(半径) ：" + area));
		} else {
			tooltip.add(I18n.format(TextFormatting.RED + "左シフトで詳しく表示"));
		}
    }
}
