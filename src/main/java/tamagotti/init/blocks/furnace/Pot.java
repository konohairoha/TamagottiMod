package tamagotti.init.blocks.furnace;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.TamagottiMod;
import tamagotti.handlers.TGuiHandler;
import tamagotti.init.BlockInit;
import tamagotti.init.base.BaseFurnaceBlock;
import tamagotti.init.tile.TileBaseFurnace;
import tamagotti.init.tile.furnace.TileTDisplay;

public class Pot extends BaseFurnaceBlock {

	public final int data;

	public Pot(String name, int data, List<Block> list) {
		super(name);
		setSoundType(SoundType.METAL);
		this.data = data;
		list.add(this);
	}

	/**
	 * 0 = 無印
	 * 1 = スモーキィー
	 * 2 = たまごっち
	 * 3 = ディスプレイ
	 */

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		if (!world.isRemote) {

			int GUIID = 0;

			switch (this.data) {
			case 0:
				GUIID = TGuiHandler.POT_GUI;
				break;
			case 1:
				GUIID = TGuiHandler.POT_N_GUI;
				break;
			case 2:
				GUIID = TGuiHandler.POT_C_GUI;
				break;
			case 3:
				GUIID = TGuiHandler.TDISPLAY_GUI;
				break;
			}
			player.openGui(TamagottiMod.INSTANCE, GUIID, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
    }

    public void setState(boolean active, World world, BlockPos pos) {

        IBlockState state = world.getBlockState(pos);
        TileEntity tile = world.getTileEntity(pos);
        keepInventory = true;

        IBlockState state2 = null;

		if (active) {

			switch (this.data) {
			case 0:
				state2 = BlockInit.pot_1.getDefaultState();
				break;
			case 1:
				state2 = BlockInit.potneo_1.getDefaultState();
				break;
			case 2:
				state2 = BlockInit.potcustom_1.getDefaultState();
				break;
			case 3:
				state2 = BlockInit.tdisplay_1.getDefaultState();
				break;
			}

		} else {

			switch (this.data) {
			case 0:
				state2 = BlockInit.pot_0.getDefaultState();
				break;
			case 1:
				state2 = BlockInit.potneo_0.getDefaultState();
				break;
			case 2:
				state2 = BlockInit.potcustom_0.getDefaultState();
				break;
			case 3:
				state2 = BlockInit.tdisplay_0.getDefaultState();
				break;
			}

		}

		world.setBlockState(pos, state2.withProperty(FACING, state.getValue(FACING)), 2);

		keepInventory = false;
		if (tile != null) {
            tile.validate();
            world.setTileEntity(pos, tile);
        }
    }

    @Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {

		switch (this.data) {
		case 0:
	        return new TileBaseFurnace(100, 10000);
		case 1:
	        return new TileBaseFurnace(20, 20000);
		case 2:
	        return new TileBaseFurnace(5, 30000);
		case 3:
	        return new TileTDisplay();
		}

		return null;
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {

		String name = "";

		switch (this.data) {
		case 0:
			name = "かまどの2倍の性能";
			break;
		case 1:
			name = "かまどの5倍の性能";
			break;
		case 2:
			name = "かまどの20倍の性能";
			break;
		case 3:
			name = "かまどの100倍の性能";
			break;
		}

		tooltip.add(I18n.format(TextFormatting.GOLD + name));
	}
}
