package tamagotti.init.items.tool.tamagotti.iitem;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public interface IHeld {

	AxisAlignedBB getRange (BlockPos pos);
}
