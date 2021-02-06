package tamagotti.init.items.tool.tamagotti;

import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tamagotti.init.entity.monster.EntityAoi;

public class TEgg extends TItem {

	private final int data;

	public TEgg(String name, int meta) {
		super(name);
        this.data = meta;
	}

	/**
	 * 0 = 鶏
	 * 1 = 牛
	 * 2 = 豚
	 * 3 = 羊
	 * 4 = 村人
	 */

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float x, float y, float z) {

		if (world.isRemote) { return EnumActionResult.SUCCESS; }

		ItemStack stack = player.getHeldItem(hand);
		BlockPos offPos = pos.offset(facing);

		if (!player.capabilities.isCreativeMode) { stack.shrink(1); }

		if(this.data == 0) {
    		EntityChicken eggc = new EntityChicken(world);
			eggc.setGrowingAge(0);
			eggc.setLocationAndAngles(offPos.getX() + 0.5F, offPos.getY(), offPos.getZ() + 0.5F, player.rotationYaw, 0.0F);
	        world.spawnEntity(eggc);
		} else if(this.data == 1) {
    		EntityCow eggb = new EntityCow(world);
			eggb.setGrowingAge(0);
			eggb.setLocationAndAngles(offPos.getX() + 0.5F, offPos.getY(), offPos.getZ() + 0.5F, player.rotationYaw, 0.0F);
	        world.spawnEntity(eggb);
		} else if(this.data == 2) {
    		EntityPig eggp = new EntityPig(world);
    		eggp.setGrowingAge(0);
			eggp.setLocationAndAngles(offPos.getX() + 0.5F, offPos.getY(), offPos.getZ() + 0.5F, player.rotationYaw, 0.0F);
	        world.spawnEntity(eggp);
		} else if(this.data == 3) {
    		EntitySheep eggs = new EntitySheep(world);
    		eggs.setGrowingAge(0);
			eggs.setLocationAndAngles(offPos.getX() + 0.5F, offPos.getY(), offPos.getZ() + 0.5F, player.rotationYaw, 0.0F);
	        world.spawnEntity(eggs);
		} else if(this.data == 4) {
			EntityVillager eggv = new EntityVillager(world);
			if(player.isSneaking()) {
				eggv = new EntityAoi(world);
			}
    		eggv.setGrowingAge(0);
    		eggv.setProfession(world.rand.nextInt(4));
			eggv.setLocationAndAngles(offPos.getX() + 0.5F, offPos.getY(), offPos.getZ() + 0.5F, player.rotationYaw, 0.0F);
	        world.spawnEntity(eggv);
    	}
		return EnumActionResult.SUCCESS;
	}
}
