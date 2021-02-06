package tamagotti.init.items.tool.tamagotti;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class TSeed extends TFoodItem implements IPlantable {

	public final IBlockState state;

	public TSeed(String name, Block block) {
        super(name);
        this.state = block.getDefaultState();
    }

    @Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing face,float hitX, float hitY, float hitZ) {

		ItemStack stack = player.getHeldItem(hand);
		if (!player.canPlayerEdit(pos.offset(face), face, stack) || face != EnumFacing.UP || !world.isAirBlock(pos.up()) ) { return EnumActionResult.FAIL; }

		IBlockState target = world.getBlockState(pos);
		Block b = target.getBlock();

		if ( b == Blocks.FARMLAND || b.canSustainPlant(target, world, pos, face, this) ) {

			if (!player.capabilities.isCreativeMode) { stack.shrink(1); }
			world.setBlockState(pos.up(), this.state);
			return EnumActionResult.SUCCESS;
		}

        return EnumActionResult.PASS;
	}

    @Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return EnumPlantType.Crop;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
		return this.state;
	}
}