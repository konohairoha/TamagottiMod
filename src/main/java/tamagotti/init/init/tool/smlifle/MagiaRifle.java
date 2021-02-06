package tamagotti.init.items.tool.smlifle;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.iitem.ISMItem;
import sweetmagic.api.iitem.IWand;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.EnchantInit;
import sweetmagic.packet.PlayerSoundPKT;
import sweetmagic.util.SoundHelper;
import tamagotti.TamagottiMod;
import tamagotti.handlers.TGuiHandler;
import tamagotti.init.base.BaseBow;
import tamagotti.init.items.tool.tamagotti.iitem.IZoom;
import tamagotti.util.TUtil;

@Optional.Interface(iface = "sweetmagic.api.iitem.IWand", modid = "sweetmagic")
public class MagiaRifle extends BaseBow implements IZoom, IWand {

	public int tier;
	public int maxMF;
	public int slot;
	public int level;
	public ISMItem slotItem;
	public float chargeTick = 0;
	int downTime = 1;

	public MagiaRifle (String name, int tier, int maxMF, int slot) {
		super(name);
		this.setTier(tier);
		this.setMaxMF(maxMF);
		this.setSlot(slot);
	}

	/*
	 * =========================================================
	 * 				アクション登録　Start
	 * =========================================================
	 */

	// 右クリック
	@Override@Optional.Method(modid = "sweetmagic")
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		// アイテムスタックを取得
		ItemStack stack = player.getHeldItem(hand);

		if (hand == EnumHand.OFF_HAND || stack.isEmpty()) {
			return new ActionResult(EnumActionResult.PASS, stack);
		}

		// nbtを取得
		NBTTagCompound tags = this.getNBT(stack);

		// 選択中のアイテムを取得
		ItemStack slotItem = this.getSlotItem(player, stack, tags);

		if (slotItem.isEmpty() || !(slotItem.getItem() instanceof ISMItem)) { return new ActionResult(EnumActionResult.PASS, stack); }

		ISMItem item = (ISMItem) slotItem.getItem();
		this.slotItem = item;

		// 射撃処理
		player.setActiveHand(hand);

		return new ActionResult(EnumActionResult.SUCCESS, stack);
	}

	// 右クリックチャージをやめたときに矢を消費せずに矢を射る
	@Override@Optional.Method(modid = "sweetmagic")
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {

		EntityPlayer player = (EntityPlayer) living;

		// アイテムスタックを取得
		ItemStack st = player.getHeldItemMainhand();

		if (st.isEmpty()) { return;}

		// nbtを取得
		NBTTagCompound tags = this.getNBT(stack);

		// 選択中のアイテムを取得
		ItemStack slotItem = this.getSlotItem(player, stack, tags);

		if (slotItem.isEmpty() || !(slotItem.getItem() instanceof ISMItem)) { return; }

		ISMItem item = (ISMItem) slotItem.getItem();
		this.slotItem = item;

		int i = this.getMaxItemUseDuration(stack) - timeLeft;
		this.setChargeTick(TUtil.getArrowVelocity(i, 1F));

		this.shotterActived(world, player, stack, slotItem, tags);
	}

	/*
	 * =========================================================
	 * 				アクション登録　End
	 * =========================================================
	 */


	/*
	 * =========================================================
	 * 				アクション処理　Start
	 * =========================================================
	 */

	// 射撃処理
	@Override
	public void shotterActived (World world, EntityPlayer player, ItemStack stack, ItemStack slotItem, NBTTagCompound tags) {

		Item item = slotItem.getItem();
		ISMItem smItem = (ISMItem) item;

		// クリエワンド以外で魔法が使えるかチェック
		if(!this.magicActionBeforeCheck(player, stack, item, smItem)) { return; }

		// アイテムの処理を実行
		boolean actionFlag = this.onAction(world, player, stack, item, smItem, tags);

		// 魔法アクション後の処理
		this.magicActionAfter(world, player, stack, item, smItem, tags, actionFlag);

	}

	// 魔法アクション中の処理
	@Override
	public boolean onAction (World world, EntityPlayer player, ItemStack stack, Item item, ISMItem smItem, NBTTagCompound tags) {

		boolean flag = false;

		// レベルの取得
		int level = this.getLevel(stack);
		int enchaLevel = this.getEnchantLevel(EnchantInit.wandAddPower, stack);
		int power = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
		tags.setInteger(LEVEL, (int) ((level + enchaLevel + power + 3) * this.getChargeTick()));

		flag = smItem.onItemAction(world, player, stack, item);

		// レベルを戻す
		tags.setInteger(LEVEL, level);

		return flag;
	}

	/*
	 * =========================================================
	 * 				アクション処理　End
	 * =========================================================
	 */


	/*
	 * =========================================================
	 * 				キー処理　Start
	 * =========================================================
	 */

	@Override
	public void openGui(World world, EntityPlayer player, ItemStack stack) {
		if (!world.isRemote) {
			player.openGui(TamagottiMod.INSTANCE, TGuiHandler.MFRIFLE_GUI, world, 0, -1, -1);

			// クライアント（プレイヤー）へ送りつける
			PacketHandler.sendToPlayer(new PlayerSoundPKT(SoundHelper.S_PAGE, 1F, 0.33F), (EntityPlayerMP) player);
		}
	}

	/*
	 * =========================================================
	 * 				キー処理　End
	 * =========================================================
	 */


	@Override
	public float zoomItem() {
		return 3;
	}

  	// 最大MFを取得
  	@Override
	public int getMaxMF (ItemStack stack) {
		int addMaxMF = (this.getEnchantLevel(EnchantInit.maxMFUP, stack) * 5) * (this.maxMF / 100);
  		return this.maxMF + addMaxMF;
  	}

  	// 最大MFを設定
  	@Override
	public void setMaxMF (int maxMF) {
  		this.maxMF = maxMF;
  	}

  	// スロット数を取得
	@Override
	public int getSlot() {
		return this.slot;
	}

  	// スロット数を設定
	@Override
	public void setSlot(int slot) {
		this.slot = slot;
	}

	// tierの取得
	@Override
	public int getTier () {
		return this.tier;
	}

	// tierの設定
	@Override
	public void setTier (int tier) {
		this.tier = tier;
	}

	// レベルを取得
	@Override
	public void setLevel(ItemStack stack, int level) {
		this.level = level;
	}

	@Override
	public float getChargeTick() {
		return this.chargeTick;
	}

	@Override
	public void setChargeTick(float chargeTick) {
		this.chargeTick = chargeTick;
	}

	// エンチャントエフェクト描画
	@Override
	@Optional.Method(modid = "sweetmagic")
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
	    return super.hasEffect(stack) || this.getLevel(stack) >= this.getMaxLevel();
    }

//	@Override
//	@Optional.Method(modid = "sweetmagic")
//	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchant) {
//		return super.canApplyAtEnchantingTable(stack, enchant) || enchant.type == EnchantWand.type;
//	}
}
