package tamagotti.init.tile;

import net.minecraft.nbt.NBTTagCompound;

public class TileWindMillL extends TileWindMill {

	@Override
    public void readFromNBT(NBTTagCompound tags) {
        super.readFromNBT(tags);
        tags.setBoolean("active", this.getActive());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tags) {
        super.writeToNBT(tags);
        this.setActive(tags.getBoolean("active"));
        return tags;
    }
}
