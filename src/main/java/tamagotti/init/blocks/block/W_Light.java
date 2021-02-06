package tamagotti.init.blocks.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class W_Light extends TIron {

	public W_Light(String name) {
		super(Material.WOOD);
        setUnlocalizedName(name);
        setRegistryName(name);
        setHardness(0.5F);
        setResistance(16F);
        setSoundType(SoundType.METAL);
		setLightLevel(1.0F);
    }
}