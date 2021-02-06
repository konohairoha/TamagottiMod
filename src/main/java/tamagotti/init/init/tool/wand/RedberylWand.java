package tamagotti.init.items.tool.wand;

import com.google.common.collect.Multimap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.iitem.ISMItem;
import sweetmagic.api.iitem.IWand;
import sweetmagic.init.EnchantInit;
import sweetmagic.init.item.sm.eitem.SMType;
import tamagotti.init.ItemInit;
import tamagotti.init.base.BaseRangeBreak;
import tamagotti.util.TUtil;

@Optional.Interface(iface = "sweetmagic.api.iitem.IWand", modid = "sweetmagic")
public class RedberylWand extends BaseRangeBreak implements IWand {

	private final int size;

	// 変数
	public int tier;
	public int maxMF;
	public int slot;
	public int level;
	public ISMItem slotItem;
	public float chargeTick = 0;

	public RedberylWand(String name,ToolMaterial material, int cycle, int tier, int maxMF, int slot) {
		super(name, material, cycle);
		this.size = cycle;
		this.setTier(tier);
		this.setMaxMF(maxMF);
		this.setSlot(slot);
	}

	// 攻撃スピードなどの追加
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(slot);
		if (slot == EntityEquipmentSlot.MAINHAND) {
			multimap.put(EntityPlayer.REACH_DISTANCE.getName(),
					new AttributeModifier(TUtil.TOOL_REACH, "Weapon modifier", (this.size - 2), 0));
		}
		return multimap;
	}

	// 壊すブロックの採掘速度を変更
	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		if(this.size == 2) {
			return 8.0F;
		} else if (this.size == 5) {
			return 12.0F;
		}
		return 9.0F;
	}

	// 全てのブロック（マテリアル）を破壊可能に
	@Override
	public boolean canHarvestBlock(IBlockState blockIn) {
	    return true;
	}

	// 特定のアイテムで修復可能に
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		if(this.size == 2) {
			return repair.getItem() == ItemInit.redberyl ? true : super.getIsRepairable(toRepair, repair);
		} else if (this.size == 5) {
			return repair.getItem() == ItemInit.smokyquartz ? true : super.getIsRepairable(toRepair, repair);
		}
		return false;
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

		// 射撃タイプで分別
		switch (item.getType()) {

		// 射撃タイプ
		case SHOTTER:
			this.shotterActived(world, player, stack, slotItem, tags);
			break;

		// 空中タイプ
		case AIR:
			this.airActived(world, player, stack, slotItem, tags);
			break;

		// 溜めタイプ
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
		if (st.isEmpty() || !(st.getItem() instanceof ISMItem)) { return false; }

		// nbtを取得
		NBTTagCompound tags = this.getNBT(stack);

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
		if (stack.isEmpty() || !(stack.getItem() instanceof ISMItem)) { return EnumActionResult.PASS;}

		// nbtを取得
		NBTTagCompound tags = this.getNBT(stack);

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

		// 選択中のアイテムを取得
		ItemStack slotItem = this.getSlotItem(player, stack, tags);
		if (slotItem.isEmpty() || !(slotItem.getItem() instanceof ISMItem)) { return; }

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

	/*
	 * =========================================================
	 * 				アクション登録　End
	 * =========================================================
	 */

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

	// 最大１分間出来るように
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		int chargeTime = 0;
		if (this.slotItem != null && this.slotItem.getType() == SMType.CHARGE) {
			chargeTime = 72000;
		}
		return chargeTime;
	}

	// 右クリックをした際の挙動を弓に
	@Override
	@Optional.Method(modid = "sweetmagic")
	public EnumAction getItemUseAction(ItemStack stack) {
		if (this.slotItem != null && this.slotItem.getType() == SMType.CHARGE) {
			return EnumAction.BOW;
		}
		return EnumAction.NONE;
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
