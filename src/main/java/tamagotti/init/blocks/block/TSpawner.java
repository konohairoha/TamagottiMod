package tamagotti.init.blocks.block;

import javax.annotation.Nonnull;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tamagotti.init.base.BaseFaceBlock;
import tamagotti.init.tile.TileTSpawner;

public class TSpawner extends BaseFaceBlock {

	public TSpawner(String name) {
		super(Material.WOOD, name);
		setSoundType(SoundType.METAL);
		setHardness(0.5F);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileTSpawner();
	}
}