package tamagotti.init.items.tool.tamagotti;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tamagotti.TamagottiMod;
import tamagotti.util.ItemHelper;
import tamagotti.util.TUtil;

public class TParasol extends TItem {

	public static final String TAG_ACTIVE = "Active";
	public static final String TAG_MODE = "Mode";
	protected static final ResourceLocation ACTIVE_NAME = new ResourceLocation(TamagottiMod.MODID, "active");
	protected static final IItemPropertyGetter ACTIVE_GETTER = (stack, world, entity) -> stack.hasTagCompound() && stack.getTagCompound().getBoolean(TAG_ACTIVE) ? 1F : 0F;

	public TParasol(String name) {
		super(name);
        this.setMaxStackSize(1);
        this.addPropertyOverride(ACTIVE_NAME, ACTIVE_GETTER);
    }

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return TUtil.shouldCauseReequipAnimation(oldStack, newStack, slotChanged, TAG_ACTIVE, TAG_MODE);
	}

	public boolean changeMode(@Nonnull EntityPlayer player, @Nonnull ItemStack stack, EnumHand hand) {
		stack.getTagCompound().setBoolean(TAG_ACTIVE, !ItemHelper.getNBT(stack).getBoolean(TAG_ACTIVE));
		return true;
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		if (!world.isRemote) {
			this.changeMode(player, player.getHeldItem(hand), hand);
			player.getCooldownTracker().setCooldown(this, 5);
		}
		world.playSound(player, new BlockPos(player), SoundEvents.BLOCK_CLOTH_PLACE, SoundCategory.NEUTRAL, 1.0F, 1.0F);
		return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
}
