package tamagotti.init.items.armor;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.TamagottiMod;
import tamagotti.init.ItemInit;
import tamagotti.init.event.TSoundEvent;
import tamagotti.key.ClientKeyHelper;
import tamagotti.key.TKeybind;
import tamagotti.util.ItemHelper;
import tamagotti.util.TUtil;

public class MArmor extends ItemArmor {

	public static final String TAG_ACTIVE = "Active";
	public static final String TAG_MODE = "Mode";
	protected static final ResourceLocation ACTIVE_NAME = new ResourceLocation(TamagottiMod.MODID, "active");
	protected static final IItemPropertyGetter ACTIVE_GETTER = (stack, world, entity) -> stack.hasTagCompound() && stack.getTagCompound().getBoolean(TAG_ACTIVE) ? 1F : 0F;

	public MArmor(String name, int render, EntityEquipmentSlot slot) {
		super(TArmorMaterial.machine, render, slot);
        setUnlocalizedName(name);
        setRegistryName(name);
		ItemInit.itemList.add(this);
        this.addPropertyOverride(ACTIVE_NAME, ACTIVE_GETTER);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return TUtil.shouldCauseReequipAnimation(oldStack, newStack, slotChanged, TAG_ACTIVE, TAG_MODE);
	}

	public boolean changeMode(@Nonnull EntityPlayer player, @Nonnull ItemStack stack) {
		World world = player.getEntityWorld();
		if (world.isRemote) { return true; }

		String text = new TextComponentTranslation("ta.key.mode", new Object[0]).getFormattedText();
		if (!ItemHelper.getNBT(stack).getBoolean(TAG_ACTIVE)) {
			player.sendMessage(new TextComponentString(text + ": OFF"));
			world.playSound(null, new BlockPos(player), TSoundEvent.MAGICOFF, SoundCategory.NEUTRAL, 0.25F, 1.0075F);
			stack.getTagCompound().setBoolean(TAG_ACTIVE, true);
		} else {
			player.sendMessage(new TextComponentString(text + ": ON"));
			world.playSound(null, new BlockPos(player), TSoundEvent.MAGICON, SoundCategory.NEUTRAL, 0.4F, 1.01F);
			stack.getTagCompound().setBoolean(TAG_ACTIVE, false);
		}
		return true;
	}

	//ポーション効果を付ける処理
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {

		if (!world.isRemote) {
			EntityPlayerMP playerMP = ((EntityPlayerMP) player);
			playerMP.fallDistance = 0;
		} else if (!ItemHelper.getNBT(stack).getBoolean(TAG_ACTIVE)) {
			if (!player.capabilities.isFlying && TamagottiMod.proxy.isJumpPressed()) {
				player.motionY += 0.1125;
			}

			if (!player.onGround) {
				if (player.motionY <= 0 && player.isSneaking()) {
					player.motionY *= 0.9;
				} else if (!TamagottiMod.proxy.isJumpPressed()) {
					player.motionY = 0;
				}

				if (!player.capabilities.isFlying) {
					if (player.moveForward < 0) {
						player.motionX *= 0.9;
						player.motionZ *= 0.9;
					} else if (player.moveForward > 0 && player.motionX * player.motionX
							+ player.motionY * player.motionY + player.motionZ * player.motionZ < 3) {
						player.motionX *= 1.1;
						player.motionZ *= 1.1;
					}
				}
			}
		}
	}


    //ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		tooltip.add(I18n.format(TextFormatting.BLUE + "クリエ飛行 + 落下無効"));
		tooltip.add(I18n.format(TextFormatting.RED + ClientKeyHelper.getKeyName(TKeybind.MODE) + "キーで飛行モード切り替え"));
  	}

  	//アイテムにダメージを与える処理を無効
  	@Override
	public void setDamage(ItemStack stack, int damage) {
  		return;
  	}
}
