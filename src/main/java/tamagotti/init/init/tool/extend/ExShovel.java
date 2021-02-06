package tamagotti.init.items.tool.extend;

import com.google.common.collect.Multimap;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.items.tool.tamagotti.TShovel;
import tamagotti.util.TUtil;

public class ExShovel extends TShovel {

	public ExShovel(String name, ToolMaterial material) {
		super(name, material);
	}

	//攻撃スピードなどの追加
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(slot);
		if (slot == EntityEquipmentSlot.MAINHAND) {
			multimap.put(EntityPlayer.REACH_DISTANCE.getName(), new AttributeModifier(TUtil.TOOL_REACH, "Weapon modifier", 5, 0));
		}
		return multimap;
	}

	//右クリックをした際の処理
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        player.setActiveHand(hand);
        ItemStack stack = player.getHeldItem(hand);
        Vec3d vec3d = player.getLookVec();
        vec3d = vec3d.normalize().scale(7);
        BlockPos pos = new BlockPos(player.posX + (int)vec3d.x, player.posY + (int)vec3d.y, player.posZ + (int)vec3d.z);
        world.setBlockState(pos, Blocks.DIRT.getDefaultState());
		world.playSound(player, pos, SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 1.0F, 0.75F);
		stack.damageItem(1, player);
		return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,float hitX, float hitY, float hitZ) {

		ItemStack stack = player.getHeldItem(hand);
		if (!player.canPlayerEdit(pos.offset(facing), facing, stack)) { return EnumActionResult.FAIL; }

        pos = pos.offset(facing);
        world.setBlockState(pos, Blocks.DIRT.getDefaultState());
		world.playSound(player, pos, SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 1.0F, 0.75F);
		stack.damageItem(1, player);
		return EnumActionResult.SUCCESS;
	}

	@Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

	public RayTraceResult getHitBlock(EntityPlayer player) {
		return this.rayTrace(player.getEntityWorld(), player, player.isSneaking());
	}
}