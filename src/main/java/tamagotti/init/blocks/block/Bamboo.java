package tamagotti.init.blocks.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import tamagotti.init.base.BaseModelBlock;

public class Bamboo extends BaseModelBlock {

	public Bamboo(String name) {
		super(Material.WOOD, name);
		setSoundType(SoundType.WOOD);
	}
}