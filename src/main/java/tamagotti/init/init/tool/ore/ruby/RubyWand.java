package tamagotti.init.items.tool.ore.ruby;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.items.tool.tamagotti.TPick;
import tamagotti.util.ItemHelper;
import tamagotti.util.TUtil;

public class RubyWand extends TPick {

	//原木の向きに必要
	public static final PropertyEnum<BlockLog.EnumAxis> LOG_AXIS = PropertyEnum.<BlockLog.EnumAxis>create("axis", BlockLog.EnumAxis.class);
	public static final String TAG_ACTIVE = "Active";
	public static final String TAG_MODE = "Mode";
	public int yActive = 0;

	public RubyWand(String name, ToolMaterial material) {
		super(name, material);
	}

	//攻撃スピードなどの追加
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
		if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
			multimap.put(EntityPlayer.REACH_DISTANCE.getName(), new AttributeModifier(TUtil.TOOL_REACH, "Weapon modifier", 6, 0));
		}
		return multimap;
	}


	//右クリックをした際の処理
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

        player.setActiveHand(hand);
        ItemStack stack = player.getHeldItem(hand);

		if (player.isSneaking()) {
			if (!world.isRemote) {
				int newMode = 0;

				switch (ItemHelper.getNBT(stack).getInteger(TAG_MODE)) {
				case 0:
					newMode = 1;
					player.sendMessage(new TextComponentString("丸石設置モード"));
					break;
				case 1:
					newMode = 2;
					player.sendMessage(new TextComponentString("原木設置モード"));
					break;
				case 2:
					newMode = 0;
					player.sendMessage(new TextComponentString("土設置モード"));
					player.clearActivePotions();
					break;
				}
				player.getCooldownTracker().setCooldown(this, 10);
				this.changeMode(stack, newMode);

			}
		} else {

	        Vec3d vec3d = player.getLookVec();
	        vec3d = vec3d.normalize().scale(7);
	        BlockPos pos = new BlockPos(player.posX + (int)vec3d.x, player.posY + (int)vec3d.y + 1, player.posZ + (int)vec3d.z);
	        Block block = null;

	        if (stack.getTagCompound() != null) {
		        switch (stack.getTagCompound().getInteger(TAG_MODE)) {
		        case 0:
		        	block = Blocks.DIRT;
					world.playSound(player, pos, SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 1.0F, 0.75F);
		        	break;
		        case 1:
		        	block = Blocks.COBBLESTONE;
					world.playSound(player, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1.0F, 0.875F);
		        	break;
		        case 2:
		        	block = Blocks.LOG;
					world.playSound(player, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1.0F, 0.875F);
		        	break;
		        }
	        } else {
	        	block = Blocks.DIRT;
				world.playSound(player, pos, SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 1.0F, 0.75F);
	        }

	        world.setBlockState(pos, block.getDefaultState(), 2);
			stack.damageItem(1, player);
		}
		return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,float hitX, float hitY, float hitZ) {

		ItemStack stack = player.getHeldItem(hand);
		if (!player.canPlayerEdit(pos.offset(facing), facing, stack)) { return EnumActionResult.FAIL; }

        pos = pos.offset(facing);
        Block block = null;

		if (stack.getTagCompound() != null) {
			switch (stack.getTagCompound().getInteger(TAG_MODE)) {
			case 0:
				block = Blocks.DIRT;
				world.playSound(player, pos, SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 1.0F, 0.75F);
				break;
			case 1:
				block = Blocks.COBBLESTONE;
				world.playSound(player, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1.0F, 0.875F);
				break;
			case 2:
	        	block = Blocks.LOG;
				world.playSound(player, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1.0F, 0.875F);
				break;
			}

			if (stack.getTagCompound().getInteger(TAG_MODE) == 2) {

				switch (facing) {
				case SOUTH:
				case NORTH:
					world.setBlockState(pos, block.getDefaultState().withProperty(LOG_AXIS, BlockLog.EnumAxis.Z));
					break;
				case WEST:
				case EAST:
					world.setBlockState(pos, block.getDefaultState().withProperty(LOG_AXIS, BlockLog.EnumAxis.X));
					break;
				default:
					world.setBlockState(pos, block.getDefaultState());
					break;
				}

			} else {
				world.setBlockState(pos, block.getDefaultState());
			}
		} else {
        	block = Blocks.DIRT;
			world.playSound(player, pos, SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 1.0F, 0.75F);
			world.setBlockState(pos, block.getDefaultState());
		}

		stack.damageItem(1, player);
		return EnumActionResult.SUCCESS;
	}

	public void changeMode(ItemStack stack, int mode) {
		ItemHelper.getNBT(stack).setInteger(TAG_MODE, mode);
	}

	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {

  		String block = "土";
  		NBTTagCompound tags = stack.getTagCompound();

		if (tags != null) {
			switch (tags.getInteger(TAG_MODE)) {
			case 0:
				block = "土";
				break;
			case 1:
				block = "丸石";
				break;
			case 2:
	        	block = "原木";
				break;
			}
		}

  		tooltip.add(I18n.format(TextFormatting.GOLD + block + "設置モード"));
  		tooltip.add(I18n.format(TextFormatting.GOLD + "右クリックで設置"));
    }
}
