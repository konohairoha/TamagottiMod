package tamagotti.init.blocks.chest.tzon;

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

public class TZon extends BaseChestBlock{

	public TZon(String name) {
		super(name);
		setSoundType(SoundType.CLOTH);
	}

	// ブロックの処理
	@Override
	public void actionBlock (World world, BlockPos pos, EntityPlayer player) {
		player.openGui(TamagottiMod.INSTANCE, TGuiHandler.TZCHEST_GUI, world, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileTZonChest();
	}
}
