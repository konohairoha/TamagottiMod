package tamagotti.init.blocks.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import tamagotti.init.BlockInit;

public class SakuraPlank extends Block {

    public SakuraPlank(String name) {
        super(Material.WOOD);
        setUnlocalizedName(name);
        setRegistryName(name);
        setHardness(1.0F);
        setResistance(4.0F);
        setSoundType(SoundType.WOOD);
		BlockInit.blockList.add(this);
    }
}