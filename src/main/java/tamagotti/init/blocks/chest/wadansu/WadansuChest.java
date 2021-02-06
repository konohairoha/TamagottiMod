package tamagotti.init.blocks.chest.wadansu;

import javax.annotation.Nonnull;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tamagotti.TamagottiMod;
import tamagotti.handlers.TGuiHandler;
import tamagotti.init.base.BaseChestBlock;
import tamagotti.init.blocks.chest.kago.TileKagoChest;
import tamagotti.init.blocks.chest.tbshelf.TileTBSChest;
import tamagotti.init.blocks.chest.tchest.TileTChest;
import tamagotti.init.blocks.chest.tzon.TileTZonChest;

public class WadansuChest extends BaseChestBlock {

	public final int data ;

	public WadansuChest(String name, int data, SoundType type) {
		super(name);
		setSoundType(type);
		this.data = data;
	}

	/**
	 * 0 = 和ダンス
	 * 1 = 籠入りだな
	 * 2 = 金庫
	 * 3 = たまぞん
	 * 4 = たまごっち本棚
	 */

	// ブロックの処理
	@Override
	public void actionBlock(World world, BlockPos pos, EntityPlayer player) {

		int GUIID = 0;
		switch (this.data ) {
		case 0:
			GUIID = TGuiHandler.WADANSU_GUI;
			break;
		case 1:
			GUIID = TGuiHandler.KAGO_GUI;
			break;
		case 2:
			GUIID = TGuiHandler.TCHEST_GUI;
			break;
		case 3:
			GUIID = TGuiHandler.TZCHEST_GUI;
			break;
		case 4:
			GUIID = TGuiHandler.TBSHELF_GUI;
			break;
		}

		player.openGui(TamagottiMod.INSTANCE, GUIID, world, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {

		switch (this.data ) {
		case 0:
			return new TileWadasu();
		case 1:
			return new TileKagoChest();
		case 2:
			return new TileTChest();
		case 3:
			return new TileTZonChest();
		case 4:
			return new TileTBSChest();
		}
		return null;
	}
}
