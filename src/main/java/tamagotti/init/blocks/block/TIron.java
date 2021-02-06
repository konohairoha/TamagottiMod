package tamagotti.init.blocks.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import tamagotti.init.BlockInit;

public class TIron extends Block {

    public TIron(String name, float hardness, float resistance, int harvestLevel, float light) {
        super(Material.IRON);
        setUnlocalizedName(name);
        setRegistryName(name);
        setHardness(hardness);
        setResistance(resistance);
        setHarvestLevel("pickaxe", harvestLevel);
        setSoundType(SoundType.METAL);
        setLightLevel(light);
		BlockInit.blockList.add(this);
    }

    public TIron(Material materialIn) {
        super(materialIn);
		BlockInit.blockList.add(this);
    }
}