package tamagotti.init.blocks.block;

import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import tamagotti.init.BlockInit;

public class SakuraLog extends BlockLog {

    public static final PropertyEnum<EnumAxis> LOG_AXIS = PropertyEnum.create("axis", SakuraLog.EnumAxis.class);

    public SakuraLog(String name) {
        setRegistryName(name);
        setUnlocalizedName(name);
        setDefaultState(this.blockState.getBaseState().withProperty(LOG_AXIS, SakuraLog.EnumAxis.Y));
        setHarvestLevel("axe", 1);
		BlockInit.blockList.add(this);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(LOG_AXIS, SakuraLog.EnumAxis.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(LOG_AXIS).ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LOG_AXIS);
    }

    public int getItemBurnTime(ItemStack stack){
		return 1600;
	}
}