package tamagotti.init.blocks.block;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tamagotti.init.ItemInit;

public class TOre extends TOre_N {

	Random rand = new Random();
	private final int data;

	public TOre(String name, float hardness, float resistance, int harvestLevel, int meta) {
		super(name, hardness, resistance, harvestLevel);
        this.data = meta;
    }

	/**
	 * 0 = レッドベリル
	 * 1 = スモーキー
	 * 2 = フローライト
	 * 3 = たまごっち
	 * 4 = ネオ
	 * 5 = ルビー
	 * 6 = スターローズ
	 * 7 = ヒーローたまごっち
	 */

	//ドロップさせるアイテム
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		switch (this.data) {
		case 0:
			return ItemInit.redberyl;
		case 1:
			return ItemInit.smokyquartz;
		case 2:
			return ItemInit.fluorite;
		case 3:
			return ItemInit.tamagotti;
		case 4:
			return ItemInit.tamagottineo;
		case 5:
			return ItemInit.ruby;
		case 6:
			int rand0 = rand.nextInt(100);
			if (rand0 <= 2) {
				return ItemInit.srq_ex;
			} else {
				return ItemInit.starrosequartz;
			}
		case 7:
			int rand1 = rand.nextInt(8);
			if (rand1 == 0) {
				return ItemInit.tamagotti;
			} else if (rand1 == 1) {
				return ItemInit.tamagottineo;
			} else if (rand1 == 2) {
				return ItemInit.tamagotticustom;
			} else if(rand1 == 3) {
				return ItemInit.tamagottilink;
			} else if(rand1 == 4) {
				return ItemInit.tamagotticollaboration;
			} else if(rand1 == 5) {
				return ItemInit.tamagottisorella;
			} else if(rand1 == 6) {
				return ItemInit.tamagottinecro;
			} else {
				return ItemInit.h_tama;
			}
		default:
			return ItemInit.redberyl;
		}
	}

	//ドロップさせる数
	@Override
	public int quantityDropped(Random random) {
		return 1;

	}

	//幸運のエンチャントによる加算
    @Override
    public int quantityDroppedWithBonus(int fortune, Random random) {
    	if (fortune > 0 && Item.getItemFromBlock(this) != this.getItemDropped(this.getBlockState().getValidStates().iterator().next(), random, fortune)) {
    		int i = random.nextInt(fortune + 2) - 1;
			if (i < 0) {
				i = 0;
			}
			return this.quantityDropped(random) * (i + 1);
		} else {
			return this.quantityDropped(random);
		}
    }

    //経験値ドロップ
    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
    	Random rand = world instanceof World ? ((World)world).rand : new Random();
    	return MathHelper.getInt(rand, 3, 6);
    }
}
