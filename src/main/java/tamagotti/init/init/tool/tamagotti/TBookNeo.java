package tamagotti.init.items.tool.tamagotti;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import tamagotti.TamagottiMod;
import tamagotti.handlers.TGuiHandler;
import tamagotti.init.event.TSoundEvent;
import tamagotti.init.items.tool.tamagotti.iitem.IMode;
import tamagotti.init.tile.inventory.InventoryTBookNeo;
import tamagotti.key.ClientKeyHelper;
import tamagotti.key.TKeybind;
import tamagotti.util.ItemHelper;
import tamagotti.util.TUtil;

public class TBookNeo extends TBook implements IMode {

	public static final String TAG_ACTIVE = "Active";

	public TBookNeo (String name) {
		super(name);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing face, float hitX, float hitY, float hitZ) {

		ItemStack stack = player.getHeldItem(hand);
		TileEntity tile = world.getTileEntity(pos);

		// tileのnullチェック
		if (tile != null && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face.getOpposite())) {

			IItemHandler target = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face.getOpposite());
			boolean isNotInv = false;

			// インベントリを取得
			InventoryTBookNeo neo = new InventoryTBookNeo(stack, player);
			IItemHandlerModifiable inv = neo.inventory;

			// インベントリの分だけ回す
			for (int i = 0; i < inv.getSlots(); i++) {

				ItemStack sta = inv.getStackInSlot(i);

				// 空なら次へ
				if (sta.isEmpty()) { continue; }

				if (!isNotInv) { isNotInv = true; }

				// 対象のスロット分回す
				for (int j = 0; j < target.getSlots(); j++) {

					// スロットのアイテムを取得
					ItemStack ret = target.insertItem(j, sta, true);

					// 空でないなら次へ
					if (!TUtil.isEmpty(ret)) { continue; }

					// アイテムを入れる
					target.insertItem(j, sta.copy(), false);
					tile.markDirty();
					sta.shrink(sta.getCount());
					neo.writeBack();
					break;
				}
			}

			// 本の中が空なら
			if (!isNotInv) {

				// 対象のスロット分回す
				for (int i = 0; i < target.getSlots(); i++) {
					ItemStack tileStack = target.extractItem(i, target.getStackInSlot(i).getCount(), true);

					if (tileStack.isEmpty()) { continue; }

					// インベントリの分だけ回す
					for (int k = 0; k < inv.getSlots(); k++) {

                        ItemStack invStack = inv.insertItem(k, tileStack.copy(), true);

						if (!invStack.isEmpty()) { continue; }

						inv.insertItem(k, tileStack.copy(), false);
						target.extractItem(i, tileStack.getCount(), false);
    					tile.markDirty();
    					neo.writeBack();
    					break;
					}
				}

			}

			return EnumActionResult.SUCCESS;
		}
      return EnumActionResult.PASS;
	}


	@Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote) {
			player.openGui(TamagottiMod.INSTANCE, TGuiHandler.TBOOKNEO_GUI, player.getEntityWorld(), hand == EnumHand.MAIN_HAND ? 0 : 1, -1, -1);
		}
		world.playSound(player, new BlockPos(player), TSoundEvent.PAGE, SoundCategory.AMBIENT, 0.5F, 1.0F);
		return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public void changeMode(ItemStack stack, EntityPlayer player) {
		if (!ItemHelper.getNBT(stack).getBoolean(TAG_ACTIVE)) {
			player.getEntityWorld().playSound(null, new BlockPos(player), TSoundEvent.IN, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			stack.getTagCompound().setBoolean(TAG_ACTIVE, true);
		} else {
			player.getEntityWorld().playSound(null, new BlockPos(player), TSoundEvent.OUT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			stack.getTagCompound().setBoolean(TAG_ACTIVE, false);
		}
		return;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) {

		if (!(entity instanceof EntityPlayer)) { return;}

		EntityPlayer player = (EntityPlayer) entity;

		// インベントリを取得
		InventoryTBookNeo neo = new InventoryTBookNeo(stack, player);
		IItemHandlerModifiable inv = neo.inventory;

		// インベントリの分だけ回す
		for (int i = 0; i < inv.getSlots(); i++) {

			ItemStack st = inv.getStackInSlot(i);

			// 空なら次へ
			if (st.isEmpty()) { continue; }

			Item item = st.getItem();

			try {

				// アイテムアップデートを呼び出し
				item.onUpdate(st, world, player, 0, true);

			} catch (Throwable e) {
			}
		}
	}

	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {

		// nbtを取得
		NBTTagCompound tags = stack.getTagCompound();

  		String pickUp = tags == null || !tags .getBoolean(TAG_ACTIVE) ? "通常回収モード" : "インベントリ収納モード";

  		tooltip.add(I18n.format(TextFormatting.GOLD + "右クリックでクラフトGUI + チェストGUI"));
		tooltip.add(I18n.format(TextFormatting.RED + ClientKeyHelper.getKeyName(TKeybind.MODE) + "キーで回収の切り替え"));
  		tooltip.add(I18n.format(TextFormatting.GREEN + pickUp));
		if (Keyboard.isKeyDown(42)) {
			tooltip.add(I18n.format(TextFormatting.GOLD + ""));
			tooltip.add(I18n.format(TextFormatting.GOLD + "チェストに向かってスニーク右クリックすると"));
			tooltip.add(I18n.format(TextFormatting.GOLD + "本のインベントリが空の場合はチェストの中身を入れる"));
			tooltip.add(I18n.format(TextFormatting.GOLD + "本インベントリのアイテムをチェストに入れる"));
			tooltip.add(I18n.format(TextFormatting.GOLD + ""));
			tooltip.add(I18n.format(TextFormatting.GOLD + "インベントリ内の対象アイテムを"));
			tooltip.add(I18n.format(TextFormatting.GOLD + "ホイールクリックでモード切替"));
			tooltip.add(I18n.format(TextFormatting.GOLD + "対象アイテム："));
			tooltip.add(I18n.format(TextFormatting.GREEN + "  たまごっちパラセール系"));
			tooltip.add(I18n.format(TextFormatting.GREEN + "  たまごっちペンダント"));
			tooltip.add(I18n.format(TextFormatting.GREEN + "  たまごっちリンク"));
		} else {
			tooltip.add(I18n.format(TextFormatting.RED + "左シフトで詳しく表示"));
		}
  	}
}
