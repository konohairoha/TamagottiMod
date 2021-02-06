package tamagotti.init.items.tool.hheld;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.BlockInit;
import tamagotti.init.base.BaseChestBlock;
import tamagotti.init.base.BaseFaceBlock;
import tamagotti.init.base.BaseFaceContainer;
import tamagotti.init.blocks.block.Lanp;
import tamagotti.init.items.tool.tamagotti.TItem;
import tamagotti.init.items.tool.tamagotti.iitem.IHeld;

public class H_House_1 extends TItem implements IHeld {

	//原木の向きに必要
	public static final PropertyEnum<BlockLog.EnumAxis> LOG_AXIS = PropertyEnum.<BlockLog.EnumAxis>create("axis", BlockLog.EnumAxis.class);

	public H_House_1(String name) {
        super(name);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		if (!player.canPlayerEdit(pos.offset(facing), facing, stack)) {
			return EnumActionResult.FAIL;
		} else {
			IBlockState state = world.getBlockState(pos);
			if (facing == EnumFacing.UP && world.isAirBlock(pos.up())) {
				if (state.getBlockHardness(world, pos) > 0.0D) {
					if (!player.capabilities.isCreativeMode){
						stack.shrink(1);
					}
					if (!world.isRemote) {
						player.setPositionAndUpdate(player.posX, player.posY + 1.1, player.posZ);

						// たまごっちハウス生成
						H_House_1.genTHouse(world, pos);
					}
					world.playSound(player, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}
			}
			return EnumActionResult.SUCCESS;
		}
	}

	public static void genTHouse (World world, BlockPos pos) {

		int b = 1;
		Random rand = new Random();
		IBlockState planks = Blocks.PLANKS.getDefaultState();
		IBlockState air = Blocks.AIR.getDefaultState();
		IBlockState stairs = Blocks.OAK_STAIRS.getDefaultState();
		IBlockState brickS = Blocks.BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH);
		IBlockState brickN = Blocks.BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH);
		IBlockState log = Blocks.LOG.getDefaultState();
		IBlockState fence = Blocks.OAK_FENCE.getDefaultState();
		IBlockState glass = BlockInit.copperglass.getDefaultState();
		IBlockState stone = Blocks.COBBLESTONE.getDefaultState();
		IBlockState grass = Blocks.GRASS.getDefaultState();
		IBlockState shelf = Blocks.BOOKSHELF.getDefaultState();

		// 土体を土で埋める
		for (BlockPos p : BlockPos.getAllInBox(pos.add(-6, -1 + b, -6), pos.add(5, 11 + b, 7))) {
			world.setBlockState(p, air, 2);
		}

		// 土体を土で埋める
		for (BlockPos p : BlockPos.getAllInBox(pos.add(-6, -1 + b, -6), pos.add(5, -1 + b, 7))) {
			world.setBlockState(p, grass, 2);
		}

		// 丸石で床を埋める
		for (BlockPos p : BlockPos.getAllInBox(pos.add(-5, b, -5), pos.add(4, b, 6))) {
			world.setBlockState(p, stone, 2);

		}

		// 木材で床を埋める
		for (BlockPos p : BlockPos.getAllInBox(pos.add(-4, b, -4), pos.add(3, b, 5))) {
			world.setBlockState(p, planks, 2);
		}

		// 木材の壁
		for (BlockPos p : BlockPos.getAllInBox(pos.add(-5, 1 + b, -5), pos.add(4, 8 + b, 6))) {
			world.setBlockState(p, planks, 2);
		}

		//１階の中を空洞に
		for (BlockPos p : BlockPos.getAllInBox(pos.add(-4, 1 + b, -4), pos.add(3, 8 + b, 5))) {
			world.setBlockState(p, air, 2);
		}

		//１階と２階の間に原木と木材
		for (BlockPos p : BlockPos.getAllInBox(pos.add(-5, 5 + b, -5), pos.add(4, 5 + b, 6))) {
			world.setBlockState(p, log.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z), 2);
		}
		for (BlockPos p : BlockPos.getAllInBox(pos.add(-4, 5 + b, -4), pos.add(3, 5 + b, 5))) {
			world.setBlockState(p, planks, 2);
		}

		// 原木の柱
		for (int y = 0; y <= 7; y++) {
			world.setBlockState(pos.add(-5, y + b, -5), log, 2);
			world.setBlockState(pos.add(-5, y + b, 6), log, 2);
			world.setBlockState(pos.add(4, y + b, -5), log, 2);
			world.setBlockState(pos.add(4, y + b, 6), log, 2);
		}

		for(int x = -6; x<= 5; x++) {
			world.setBlockState(pos.add(x, 5 + b, 6), log.withProperty(LOG_AXIS, BlockLog.EnumAxis.X), 2);
			world.setBlockState(pos.add(x, 5 + b, -5), log.withProperty(LOG_AXIS, BlockLog.EnumAxis.X), 2);
		}

		// フェンス設置
		world.setBlockState(pos.add(-6, 7, 6), fence, 2);
		world.setBlockState(pos.add(5, 7, 6), fence, 2);
		world.setBlockState(pos.add(-6, 7, -5), fence, 2);
		world.setBlockState(pos.add(5, 7, -5), fence, 2);


		// 松明設置
		world.setBlockState(pos.add(-6, 8, 6), Blocks.TORCH.getDefaultState(), 2);
		world.setBlockState(pos.add(5, 8, 6), Blocks.TORCH.getDefaultState(), 2);
		world.setBlockState(pos.add(-6, 8, -5), Blocks.TORCH.getDefaultState(), 2);
		world.setBlockState(pos.add(5, 8, -5), Blocks.TORCH.getDefaultState(), 2);

		// ランプを設置
		for (BlockPos p : BlockPos.getAllInBox(pos.add(0, 4 + b, 0), pos.add(1, 4 + b, 1))) {
			world.setBlockState(p, BlockInit.lamp.getDefaultState().withProperty(Lanp.FACING, EnumFacing.DOWN), 2);
		}

		//ﾄﾞｱ
		world.setBlockState(pos.add(0, 1 + b, -5), Blocks.DARK_OAK_DOOR.getDefaultState().withProperty(BlockDoor.FACING,
				EnumFacing.SOUTH).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.RIGHT), 2);
		world.setBlockState(pos.add(0, 2 + b, -5), Blocks.DARK_OAK_DOOR.getDefaultState().withProperty(BlockDoor.FACING,
				EnumFacing.SOUTH).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.RIGHT).withProperty(
						BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER), 2);
		world.setBlockState(pos.add(1, 1 + b, -5), Blocks.DARK_OAK_DOOR.getDefaultState().withProperty(BlockDoor.FACING,
				EnumFacing.SOUTH).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT), 2);
		world.setBlockState(pos.add(1, 2 + b, -5), Blocks.DARK_OAK_DOOR.getDefaultState().withProperty(BlockDoor.FACING,
				EnumFacing.SOUTH).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT).withProperty(
						BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER), 2);

		//玄関階段
		world.setBlockState(pos.add(0, 0 + b, -6), stairs.withProperty(BlockStairs.FACING,EnumFacing.SOUTH), 2);
		world.setBlockState(pos.add(1, 0 + b, -6), stairs.withProperty(BlockStairs.FACING,EnumFacing.SOUTH), 2);

		world.setBlockState(pos.add(-1, 0 + b, -6), planks, 2);
		world.setBlockState(pos.add(2, 0 + b, -6), planks, 2);
		world.setBlockState(pos.add(-1, 1 + b, -6), fence, 2);
		world.setBlockState(pos.add(2, 1 + b, -6), fence, 2);

		world.setBlockState(pos.add(3, 1 + b, -4), Blocks.CRAFTING_TABLE.getDefaultState(), 2);
		world.setBlockState(pos.add(3, 2 + b, -4), BlockInit.pot_0.getDefaultState().withProperty(BaseFaceContainer.FACING,EnumFacing.WEST), 2);

		world.setBlockState(pos.add(3, 1 + b, -2), BlockInit.wadansu.getDefaultState().withProperty(BaseChestBlock.FACING,EnumFacing.WEST), 2);
		world.setBlockState(pos.add(3, 1 + b, -3), BlockInit.wadansu.getDefaultState().withProperty(BaseChestBlock.FACING,EnumFacing.WEST), 2);

		//窓
		for (BlockPos p : BlockPos.getAllInBox(pos.add(4, 2 + b, -2), pos.add(4, 3 + b, -1))) {
			world.setBlockState(p, glass, 2);
		}
		for (BlockPos p : BlockPos.getAllInBox(pos.add(-3, 2 + b, -5), pos.add(-2, 3 + b, -5))) {
			world.setBlockState(p, glass, 2);
		}

		//木材で床を埋める
		for (BlockPos p : BlockPos.getAllInBox(pos.add(-2, b, 4), pos.add(-1, b, 5))) {
			world.setBlockState(p.add(0, 1, 0), planks, 2);
			world.setBlockState(p.add(0, 2, 0), planks, 2);
		}

		//階段
		for (int z = 4; z <= 5; z++) {
			world.setBlockState(pos.add(0, 1 + b, z), stairs.withProperty(BlockStairs.FACING,EnumFacing.WEST), 2);
			world.setBlockState(pos.add(-1, 2 + b, z), stairs.withProperty(BlockStairs.FACING,EnumFacing.WEST), 2);
			world.setBlockState(pos.add(-2, 3 + b, z), stairs.withProperty(BlockStairs.FACING,EnumFacing.WEST), 2);
		}

		//踊り場
		for(int x = -4;x <= -3; x++) {
			world.setBlockState(pos.add(x, 3 + b, 4), planks, 2);
			world.setBlockState(pos.add(x, 3 + b, 5), planks, 2);
		}

		//階段の上を空気に
		for(int x = -4;x <= 0; x++) {
			world.setBlockState(pos.add(x, 5 + b, 4), air, 2);
			world.setBlockState(pos.add(x, 5 + b, 5), air, 2);
		}

		//踊り場
		for(int x = -4;x <= -3; x++) {
			world.setBlockState(pos.add(x, 5 + b, 3), air, 2);
		}

		//踊り場
		for(int x = -4;x <= -3; x++) {
			world.setBlockState(pos.add(x, 4 + b, 3), stairs.withProperty(BlockStairs.FACING,EnumFacing.NORTH), 2);
			world.setBlockState(pos.add(x, 5 + b, 2), stairs.withProperty(BlockStairs.FACING,EnumFacing.NORTH), 2);
		}

		//階段にフェンス
		for (BlockPos p : BlockPos.getAllInBox(pos.add(-2, 1 + b, 3), pos.add(0, 4 + b, 3))) {
			world.setBlockState(p, fence, 2);
		}
		for (int x = -2; x <= 1; x++) {
			world.setBlockState(pos.add(x, 6 + b, 3), fence, 2);
		}

		world.setBlockState(pos.add(-2, 6 + b, 2), fence, 2);

		//ベッドの置き方
		for (int x = 2; x <= 3; x++) {
			world.setBlockState(pos.add(x, 6 + b, 5), Blocks.BED.getDefaultState().withProperty(BlockHorizontal.FACING,
					EnumFacing.SOUTH).withProperty(BlockBed.PART,BlockBed.EnumPartType.HEAD), 2);				//頭側
			world.setBlockState(pos.add(x, 6 + b, 4), Blocks.BED.getDefaultState().withProperty(BlockHorizontal.FACING,
					EnumFacing.SOUTH).withProperty(BlockBed.PART,BlockBed.EnumPartType.FOOT), 2);				//足側
		}

		//本棚
		for (int z = 4; z <= 5; z++) {
			world.setBlockState(pos.add(1, 6 + b, z), shelf, 2);
			world.setBlockState(pos.add(1, 7 + b, z), shelf, 2);
		}

		//２階ガラス
		for(int z = -3;z <= 4; z++) {
			for (int y = 7; y <= 8; y++) {
				world.setBlockState(pos.add(-5, y + b, z), glass, 2);
				world.setBlockState(pos.add(4, y + b, z), glass, 2);
			}
		}

		//２階ランプ
		world.setBlockState(pos.add(-1, 7 + b, 5), BlockInit.lamp.getDefaultState().withProperty(Lanp.FACING,EnumFacing.NORTH), 2);
		world.setBlockState(pos.add(1, 7 + b, -4), BlockInit.lamp.getDefaultState().withProperty(Lanp.FACING,EnumFacing.SOUTH), 2);
		world.setBlockState(pos.add(-2, 7 + b, -4), BlockInit.lamp.getDefaultState().withProperty(Lanp.FACING,EnumFacing.SOUTH), 2);

		// 屋根のフェンス
		for (int z = -2; z <= 3; z++) {
			for(int y = 10; y <=11; y++) {
				world.setBlockState(pos.add(-5, y + b, z), fence, 2);
				world.setBlockState(pos.add(4, y + b, z), fence, 2);
			}
		}

		//階段
		for (int x= -6; x <= 5; x++) {
			for (int y= 0; y <= 4; y++) {
				world.setBlockState(pos.add(x, 7 + y + b, -6 + y), brickS, 2);		//南
				world.setBlockState(pos.add(x, 7 + y + b, 7 - y), brickN, 2);		//北
			}
		}

		//横向き原木
		for (int z = -3; z <= 4; z++) {
			world.setBlockState(pos.add(-5, 9 + b, z), log.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z), 2);
			world.setBlockState(pos.add(4, 9 + b, z), log.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z), 2);
		}

		for (BlockPos p : BlockPos.getAllInBox(pos.add(-6, 12 + b, -1), pos.add(5, 12 + b, 2))) {
			world.setBlockState(p, Blocks.STONE_SLAB.getDefaultState()
					.withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.BRICK)
					.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM), 2);
		}

		world.setBlockState(pos.add(0, 1 + b, 0), BlockInit.akari.getDefaultState(), 2);

		// イスの設置
		world.setBlockState(pos.add(-2, 1 + b, 0), BlockInit.tchair_down.getDefaultState(), 2);
		world.setBlockState(pos.add(-3, 1 + b, 0), BlockInit.tchair_down.getDefaultState(), 2);
		world.setBlockState(pos.add(-2, 1 + b, -3), BlockInit.tchair_down.getDefaultState().withProperty(BaseFaceBlock.FACING,EnumFacing.SOUTH), 2);
		world.setBlockState(pos.add(-3, 1 + b, -3), BlockInit.tchair_down.getDefaultState().withProperty(BaseFaceBlock.FACING,EnumFacing.SOUTH), 2);

		world.setBlockState(pos.add(-2, 2 + b, 0), BlockInit.tchair_top.getDefaultState(), 2);
		world.setBlockState(pos.add(-3, 2 + b, 0), BlockInit.tchair_top.getDefaultState(), 2);
		world.setBlockState(pos.add(-2, 2 + b, -3), BlockInit.tchair_top.getDefaultState().withProperty(BaseFaceBlock.FACING,EnumFacing.SOUTH), 2);
		world.setBlockState(pos.add(-3, 2 + b, -3), BlockInit.tchair_top.getDefaultState().withProperty(BaseFaceBlock.FACING,EnumFacing.SOUTH), 2);

		// テーブルの設置
		for (BlockPos p : BlockPos.getAllInBox(pos.add(-2, 1 + b, -2), pos.add(-3, 1 + b, -1))) {
			world.setBlockState(p, BlockInit.ttable.getDefaultState(), 2);
		}

		// カーペットの設置
		for (BlockPos p : BlockPos.getAllInBox(pos.add(-3, 6 + b, -3), pos.add(2, 6 + b, 0))) {
			world.setBlockState(p, Blocks.CARPET.getDefaultState(), 2);
		}

		int rands = rand.nextInt(10);//乱数を呼び出す　rand.nextInt(3)の場合、引数の2を参照して0～2までの整数型で返ってくる　結果は0か1、2となる
    	if (rands == 0) {
    		world.setBlockState(pos.add(3, 1 + b, -1), Blocks.ENDER_CHEST.getDefaultState().withProperty(BlockEnderChest.FACING,EnumFacing.WEST), 2);
    	} else if(rands <= 7) {
    		world.setBlockState(pos.add(3, 1 + b, -1), Blocks.ANVIL.getDefaultState(), 2);
    	}

    	if (rand.nextInt(10) == 0) {

			//木材で埋める
			for (BlockPos p : BlockPos.getAllInBox(pos.add(-5, -4 + b, -5), pos.add(4, -1 + b, 6))) {
				world.setBlockState(p, planks, 2);
			}

			//壁を本棚に
			for (BlockPos p : BlockPos.getAllInBox(pos.add(-2, -3 + b, 0), pos.add(3, -1 + b, 6))) {
				world.setBlockState(p, shelf, 2);
			}

			//空洞に
			for (BlockPos p : BlockPos.getAllInBox(pos.add(-1, -3 + b, 1), pos.add(1, -1 + b, 5))) {
				world.setBlockState(p, air, 2);
			}

    		//１階の階段の下を空洞に
    		for (int z = 2; z <= 5; z++) {
				world.setBlockState(pos.add(-4, 0 + b, z), air, 2);
				world.setBlockState(pos.add(-3, 0 + b, z), air, 2);
    		}

    		//１階の階段の下を空洞に
    		for (int x = -4; x <= -1; x++) {
				for (int z = 4; z <= 5; z++) {
					for(int y = -1;y <= 0;y++) {
						world.setBlockState(pos.add(x, y + b, z), air, 2);
						world.setBlockState(pos.add(-2, y + 2 + b, z), air, 2);
						world.setBlockState(pos.add(-1, 1 + b, z), air, 2);
						world.setBlockState(pos.add(0, 0 + b, z), air, 2);
					}
				}
			}

    		//地下への階段
    		for (int z = 4; z <= 5; z++) {
				world.setBlockState(pos.add(-2, -2 + b, z), stairs.withProperty(BlockStairs.FACING,EnumFacing.WEST), 2);
				world.setBlockState(pos.add(-1, -3 + b, z), stairs.withProperty(BlockStairs.FACING,EnumFacing.WEST), 2);
    		}

    		//地下への階段
    		for (int x = -4; x <= -3; x++) {
				world.setBlockState(pos.add(x, 0 + b, 2), stairs.withProperty(BlockStairs.FACING,EnumFacing.NORTH), 2);
				world.setBlockState(pos.add(x, -1 + b, 3), stairs.withProperty(BlockStairs.FACING,EnumFacing.NORTH), 2);
    		}

    		//エンチャ台設置
			world.setBlockState(pos.add(0, -3 + b, 2), Blocks.ENCHANTING_TABLE.getDefaultState(), 2);
			world.setBlockState(pos.add(0, -1 + b, 2), BlockInit.kogen_s.getDefaultState(), 2);
    	}
	}

//	ツールチップの表示 langファイルのtip.Item.name で記述
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
		//xx_xx.langファイルから文字を取得する方法
		TextComponentTranslation langtext = new TextComponentTranslation("tip.house_1.name", new Object[0]);
		String text = langtext.getFormattedText();
		tooltip.add(I18n.format(TextFormatting.GOLD + text));
	}

	@Override
	public AxisAlignedBB getRange(BlockPos pos) {
		int range = 4;
		return new AxisAlignedBB(pos.add(-range - 1, -0.75, -range - 2), pos.add(range + 1, 0.25, range + 2));
	}
}
