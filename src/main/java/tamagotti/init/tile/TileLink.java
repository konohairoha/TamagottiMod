package tamagotti.init.tile;

import net.minecraft.nbt.NBTTagCompound;

public class TileLink extends TileTFEBase {

	public int posX;
	public int posY;
	public int posZ;


	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setInteger("posX", this.posX);
		tags.setInteger("posY", this.posY);
		tags.setInteger("posZ", this.posZ);
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.posX = tags.getInteger("posX");
		this.posY = tags.getInteger("posY");
		this.posZ = tags.getInteger("posZ");
	}
}
