package tamagotti.init.blocks.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import tamagotti.init.base.BaseModelBlock;

public class TPot extends BaseModelBlock {

	public TPot(String name) {
		super(Material.WOOD, name);
        setSoundType(SoundType.STONE);
        setHardness(1F);
    }
}
