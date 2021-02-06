package tamagotti.init.items.tool.tamagotti;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.iitem.ISMItem;
import sweetmagic.api.iitem.IWand;
import sweetmagic.init.item.sm.eitem.SMElement;
import sweetmagic.init.item.sm.eitem.SMType;
import tamagotti.init.items.tool.tamagotti.iitem.ILink;
import tamagotti.util.TeleportUtil;

@Optional.Interface(iface = "sweetmagic.api.iitem.ISMItem", modid = "sweetmagic")
public class TLink extends TItem implements ILink, ISMItem {

	private final int data;
	public SMElement ele;
	public SMType type;
	public int tier;
	public int coolTime;
	public int mf;
	public String name;

	public TLink(String name, int meta) {
		super(name);
		this.data = meta;
		this.name = name;
		this.tier = 1;
		this.coolTime = 20;
		this.mf = 0;
    }

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float x, float y, float z) {

		ItemStack stack = player.getHeldItem(hand);
		NBTTagCompound tags = stack.getTagCompound();
		BlockPos offPos = pos.offset(facing);

		if (player.isSneaking()) {
			if (tags == null) {
				stack.setTagCompound(new NBTTagCompound());
				tags = stack.getTagCompound();
			}
			tags.setInteger("x", offPos.getX());
			tags.setInteger("y", offPos.getY());
			tags.setInteger("z", offPos.getZ());
			tags.setInteger("dim", world.provider.getDimension());
			player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1, 1);
			player.sendStatusMessage(new TextComponentTranslation(TextFormatting.GREEN + "登録した座標：" + " " +
					tags.getInteger("x") + ", " + tags.getInteger("y") + ", " + tags.getInteger("z")), true);
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.FAIL;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		ItemStack stack = player.getHeldItem(hand);
		NBTTagCompound tags = stack.getTagCompound();

		if (tags == null) { return new ActionResult(EnumActionResult.PASS, stack); }

		this.teleportPlayer(tags, player);

		if(this.data == 1) {
			if (stack.hasDisplayName()) {

				int x = world.rand.nextInt(30000) - 15000;
				int z = world.rand.nextInt(30000) - 15000;

				tags.setInteger("x", x);
				tags.setInteger("y", 70);
				tags.setInteger("z", z);

				BlockPos pos = new BlockPos(x, 70, z);

				for (BlockPos p : BlockPos.getAllInBox(pos.add(-1, -1, -1), pos.add(1, 2, 1))) {
					world.setBlockState(p, Blocks.AIR.getDefaultState(), 2);
				}

				for (BlockPos p : BlockPos.getAllInBox(pos.add(-1, -1, -1), pos.add(1, -1, 1))) {
					world.setBlockState(p, Blocks.GRASS.getDefaultState(), 2);
				}

				this.teleportPlayer(tags, player);
			}

			if (!player.capabilities.isCreativeMode) { stack.shrink(1); }
		}

		return new ActionResult(EnumActionResult.PASS, stack);
	}

	public void teleportPlayer(NBTTagCompound tags, EntityPlayer player) {

		if (player.dimension != tags.getInteger("dim")) {

			int dim = tags.getInteger("dim");
            BlockPos pos = new BlockPos(tags.getInteger("x") + 0.5, tags.getInteger("y") + 1, tags.getInteger("z") + 0.5);
			TeleportUtil.teleportToDimension(player, dim, pos);
//			player.changeDimension(tags.getInteger("dim"));
//			player.setDi
		}

		else {
			player.setPositionAndUpdate(tags.getInteger("x") + 0.5F, tags.getInteger("y") + 1F, tags.getInteger("z") + 0.5F);
		}
		player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1, 1);
	}

	//ツールチップの表示
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
		NBTTagCompound tags = stack.getTagCompound();
		if (tags == null) {
			tooltip.add(I18n.format(TextFormatting.RED + "座標未登録"));
			tooltip.add(I18n.format(TextFormatting.GOLD + "" + TextFormatting.ITALIC + "ブロックに向かってスニーク右クリックで座標登録"));
		} else {
			tooltip.add(I18n.format(TextFormatting.GREEN + "登録した座標: " + tags.getInteger("x") + ", " + tags.getInteger("y") + ", " + tags.getInteger("z")));
			tooltip.add(I18n.format(TextFormatting.GREEN + "登録したディメンションID: " + tags.getInteger("dim")));
		}
	}

	// ツールチップ
	@Override
	public List<String> magicToolTip (List<String> toolTip) {
		toolTip.add("tip.tatele.name");
		return toolTip;
	}

	@Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
	    return stack.getTagCompound() != null;
    }

	@Override
	public SMElement getElement() {
		return SMElement.TIME;
	}

	public void setElement(SMElement ele) {
		this.ele = ele;
	}

	@Override
	public SMType getType() {
		return SMType.AIR;
	}

	public void setType(SMType type) {
		this.type = type;
	}

	@Override
	public int getTier() {
		return this.tier;
	}

	public void setTier(int tier) {
		this.tier = tier;
	}

	@Override
	public int getCoolTime() {
		return this.coolTime;
	}

	public void setCoolTime(int time) {
		this.coolTime = time;
	}

	@Override
	public int getUseMF () {
		return this.mf;
	}

	public void setUseMF(int mf) {
		this.mf = mf;
	}

	@Override
	public boolean isShirink() {
		return this.data == 1;
	}

	@Override
	public ResourceLocation getResource() {
		return new ResourceLocation("tamagottimod","textures/items/"+ this.name + ".png");
	}

	@Override
	public boolean onItemAction(World world, EntityPlayer player, ItemStack stack, Item slotItem) {

		IWand wand = (IWand) stack.getItem();
		NBTTagCompound tags = wand.getSlotItem(player, stack, wand.getNBT(stack)).getTagCompound();

		if (tags != null && tags.hasKey("x")) {
			this.teleportPlayer(tags, player);
		}
		return false;
	}
}