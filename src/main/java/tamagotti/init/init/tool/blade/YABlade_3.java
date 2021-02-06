package tamagotti.init.items.tool.blade;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.iitem.ISMItem;
import sweetmagic.api.iitem.IWand;
import sweetmagic.init.EnchantInit;
import sweetmagic.init.item.sm.eitem.SMType;
import tamagotti.TamagottiMod;
import tamagotti.init.base.BaseBlade;
import tamagotti.init.items.tool.tamagotti.iitem.IMode;
import tamagotti.util.ItemHelper;

@Optional.Interface(iface = "sweetmagic.api.iitem.IWand", modid = "sweetmagic")
public class YABlade_3 extends BaseBlade implements IMode, IWand {

	public int tier;
	public int maxMF;
	public int slot;
	public int level;
	public ISMItem slotItem;
	public float chargeTick = 0;

	public static final String TAG_ACTIVE = "Active";
	public static final String TAG_MODE = "Mode";
	protected static final ResourceLocation ACTIVE_NAME = new ResourceLocation(TamagottiMod.MODID, "active");
	protected static final IItemPropertyGetter ACTIVE_GETTER = (stack, world, entity) -> stack.hasTagCompound() && stack.getTagCompound().getBoolean(TAG_ACTIVE) ? 1F : 0F;

	public YABlade_3 (String name, ToolMaterial material, int tier, int maxMF, int slot) {
		super(name, material, 10D);
		this.setTier(tier);
		this.setMaxMF(maxMF);
		this.setSlot(slot);
        this.addPropertyOverride(ACTIVE_NAME, ACTIVE_GETTER);
	}

	//敵に攻撃したら自分にポーション効果
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {

		// nbtを取得
		NBTTagCompound tags = this.getNBT(stack);

		// 杖なら終了
		if (tags.getBoolean(TAG_ACTIVE)) { return true; }

		int mf = this.getMF(stack);
		int maxMF = this.getMaxMF(stack);

		// MFが最大なら終了
		if (mf >= maxMF) { return true; }

		// MFをセット
		this.setMF(stack, mf + 10 >= maxMF ? maxMF : mf + 10);

		return true;
	}

  	//アイテムにダメージを与える処理を無効
  	@Override
	public void setDamage(ItemStack stack, int damage) {
  		return;
  	}

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

		if (!tags.getBoolean(TAG_ACTIVE)) {
	        player.setActiveHand(hand);
	        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
		}

		// 選択中のアイテムを取得
		ItemStack slotItem = this.getSlotItem(player, stack, tags);

		if (slotItem.isEmpty()) { return new ActionResult(EnumActionResult.PASS, stack); }

		ISMItem item = (ISMItem) slotItem.getItem();
		this.slotItem = item;

		// 射撃タイプで分別
		switch (item.getType()) {

		// 射撃タイプ
		case SHOTTER:

			// 射撃処理
			this.shotterActived(world, player, stack, slotItem, tags);
			break;

		// 空中タイプ
		case AIR:

			// 空中処理
			this.airActived(world, player, stack, slotItem, tags);
			break;
		case CHARGE:
			player.setActiveHand(hand);
			break;
		default:
			return new ActionResult(EnumActionResult.PASS, stack);

		}

		return new ActionResult(EnumActionResult.SUCCESS, stack);
	}

	// モブに右クリック
	@Override@Optional.Method(modid = "sweetmagic")
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {

		ItemStack st = player.getHeldItemMainhand();

		if (st.isEmpty()) { return false; }

		NBTTagCompound tags = this.getNBT(stack);	// nbtを取得

		if (!tags.getBoolean(TAG_ACTIVE)) { return false; }

		// 選択中のアイテムを取得
		ItemStack slotItem = this.getSlotItem(player, stack, tags);

		if (slotItem.isEmpty()) { return false; }

		ISMItem item = (ISMItem) slotItem.getItem();
		this.slotItem = item;

		// 射撃タイプで分別
		switch (item.getType()) {

		// モブタイプ
		case MOB:
			this.mobActived(player.world, player, stack, slotItem, tags);
			break;
		default:
			return false;
		}
		return true;
	}

	// 地面右クリック
	@Override@Optional.Method(modid = "sweetmagic")
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,float x, float y, float z) {

		super.onItemUse(player, world, pos, hand, facing, x, y, z);

		// アイテムスタックを取得
		ItemStack stack = player.getHeldItemMainhand();

		if (stack.isEmpty()) { return EnumActionResult.PASS;}

		// nbtを取得
		NBTTagCompound tags = this.getNBT(stack);

		if (!tags.getBoolean(TAG_ACTIVE)) { return EnumActionResult.PASS;}

		// 選択中のアイテムを取得
		ItemStack slotItem = this.getSlotItem(player, stack, tags);

		if (slotItem.isEmpty()) { return EnumActionResult.PASS; }

		ISMItem item = (ISMItem) slotItem.getItem();
		this.slotItem = item;

		// 射撃タイプで分別
		switch (item.getType()) {

		// 地面タイプ
		case GROUND:
			this.groundActived(world, player, stack, slotItem, tags);
			break;
		default:
			return EnumActionResult.PASS;
		}

		return EnumActionResult.SUCCESS;
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

		if (!tags.getBoolean(TAG_ACTIVE)) { return; }

		// 選択中のアイテムを取得
		ItemStack slotItem = this.getSlotItem(player, stack, tags);

		if (slotItem.isEmpty()) { return; }

		ISMItem item = (ISMItem) slotItem.getItem();
		this.slotItem = item;

		int i = this.getMaxItemUseDuration(stack) - timeLeft;
		this.setChargeTick(this.getArrowVelocity(i, 1F));

		// 射撃タイプで分別
		switch (item.getType()) {

		// 地面タイプ
		case CHARGE:
			this.chargeActived(world, player, stack, slotItem, tags);
			break;
		default:
			return;
		}
	}

	@Override
	public void changeMode(ItemStack stack, EntityPlayer player) {
		NBTTagCompound tags = ItemHelper.getNBT(stack);
		boolean active = !tags.getBoolean(TAG_ACTIVE);
		tags.setBoolean(TAG_ACTIVE, active);
		player.world.playSound(null, new BlockPos(player), SoundEvents.BLOCK_IRON_DOOR_OPEN, SoundCategory.NEUTRAL, 4F, 1024F);
	}

	//右クリックをした際の挙動を弓に
	@Override
	@Optional.Method(modid = "sweetmagic")
	public EnumAction getItemUseAction(ItemStack stack) {
		NBTTagCompound tags = ItemHelper.getNBT(stack);

		// 剣モードなら
		if (!tags.getBoolean(TAG_ACTIVE)) {
			return EnumAction.BLOCK;
		}

		// 選択中のスロットがチャージタイプなら
		else if (this.slotItem != null && this.slotItem.getType() == SMType.CHARGE) {
			return EnumAction.BOW;
		}
		return EnumAction.NONE;
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
