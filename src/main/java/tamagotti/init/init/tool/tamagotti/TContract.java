package tamagotti.init.items.tool.tamagotti;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.ItemInit;
import tamagotti.util.PlayerHelper;

public class TContract extends TItem {

	public final int data;
	public static String LIMIT = "limit";

	public TContract (String name, int meta) {
		super(name);
		this.data = meta;
		this.setMaxDamage(256);
	}

	/**
	 * 0 = たまごっちの契約書
	 * 1 = ゾンマスの契約書
	 * 2 = スタロの契約書
	 * 3 = たまチキンの契約書
	 */

	//アイテムにダメージを与える処理を無効
	@Override
	public void setDamage(ItemStack stack, int damage) {
		return;
	}

	//右クリックをした際の処理
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		player.setActiveHand(hand);										// プレイヤーをアクションする
		ItemStack mainStack = player.getHeldItem(hand);				// メインハンド
		ItemStack offStack = player.getHeldItem(EnumHand.OFF_HAND);	// オフハンド
		boolean flag = false;											// アクションフラグ

		// 契約書がエンチャ済みのとき
		if (this.data == 1 && EnchantmentHelper.getEnchantments(mainStack).size() > 0 && offStack.isEmpty()) {
			flag = this.addEnchantBook(mainStack, player);
			if (flag && !player.capabilities.isCreativeMode) { mainStack.shrink(1); }
			return flag ? new ActionResult(EnumActionResult.SUCCESS, mainStack) : new ActionResult(EnumActionResult.PASS, mainStack);
		}

		// エンチャントしてなかったら終わり
		if (this.data != 2 && !offStack.isItemEnchanted()) {
			return new ActionResult(EnumActionResult.PASS, mainStack);
		}

		// マップ（エンチャの種類、レベル）の取得
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(offStack);

        switch (this.data) {

        // たまごっちの契約書
        case 0:
        	flag = this.addenchantLevel(map, offStack, player);
        	break;

        // ゾンマスの契約書
        case 1:
        	flag = this.setEnchant(map, mainStack, offStack, player);
        	break;

            // スタロの契約書
            case 2:
            	flag = this.setHealth(mainStack, player);
            	break;

            // たまチキンの契約書
            case 3:
            	flag = this.addLimitOver(offStack, player);
            	break;
            }

		if (flag) {
			if (!player.capabilities.isCreativeMode && this.data == 0) {
				mainStack.shrink(1);
			}
			return new ActionResult(EnumActionResult.SUCCESS, mainStack);
		}

		return new ActionResult(EnumActionResult.PASS, mainStack);
	}

    // たまごっちの契約書
	public boolean addenchantLevel (Map<Enchantment, Integer> map, ItemStack offStack, EntityPlayer player) {

		boolean flag = false;

		// エンチャの数だけ回す
		for (Enchantment enchant : map.keySet()) {

			// エンチャに最大レベルが1なら何もしない
			if (enchant.getMaxLevel() == 1) { continue; }

			// 効率エンチャがあればコストを減らす
			int cost = enchant == Enchantments.EFFICIENCY ? 5 : 10;

			// エンチャがnullか必要レベルに達していないか
			if (enchant == null || player.experienceLevel < cost) { continue; }

			// エンチャレベルを取得しレベルを + 1する
			int level = map.get(enchant).intValue();

			// エンチャの上限レベルに達しているかどうかの確認
			if (this.checkLimitLevel(offStack, enchant, level)) {

				level++; 												// レベルを増やす
				map.put(enchant, Integer.valueOf(level)); 				// マップに入れなおす
				EnchantmentHelper.setEnchantments(map, offStack); 	// アイテムにエンチャを再設定
				flag = true; 											// アクションフラグをtrueに

				// 経験値を減らす
				if (!player.capabilities.isCreativeMode) {
					player.addExperienceLevel(-cost);
				}

			}
		}
		return flag;
	}

	// エンチャの上限レベルに達しているかどうかの確認
	public boolean checkLimitLevel (ItemStack stack, Enchantment enchant, int level) {

			int limit = enchant == Enchantments.EFFICIENCY ? 30 : 10;
			NBTTagCompound tags = stack.getTagCompound();

			// 上限解放のNBTがあれば上限解放
			if (tags != null && tags.hasKey(LIMIT)) {
				limit = limit + (tags.getInteger(LIMIT) * 5);
			}

			return level < limit;
	}

	// ゾンマスの契約書
	public boolean setEnchant (Map<Enchantment, Integer> map,ItemStack mainStack , ItemStack offStack, EntityPlayer player) {
		EnchantmentHelper.setEnchantments(map, mainStack); 	// 契約書にエンチャを設定
		offStack.getTagCompound().removeTag("ench");			// エンチャを外す
		return true;
	}

	// スタロの契約書
	public boolean setHealth (ItemStack stack, EntityPlayer player) {

		// メインハンドに契約書に持ってないか体力が40を超えているかレベルが30以下
		if (stack.getItem() != ItemInit.stalo_contract ||
				PlayerHelper.getMaxHealthModifier(player) >= 40 ||
				player.experienceLevel < 30) {
			return false;
		}

		if (!player.capabilities.isCreativeMode) { stack.shrink(1); }

        World world = player.getEntityWorld();

		// レベルが20以上なら
		if (!world.isRemote) {

			// 体力を+1
			PlayerHelper.setMaxHealthModifier(player, PlayerHelper.getMaxHealthModifier(player) + 2);
			player.addExperienceLevel(-30);
			return true;
		}
		return false;
	}

	// 本にエンチャント
	public boolean addEnchantBook(ItemStack stack, EntityPlayer player) {

        World world = player.getEntityWorld();
		if (world.isRemote) { return false; }

		// マップ（エンチャの種類、レベル）の取得
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
		NonNullList<ItemStack> inv = player.inventory.mainInventory;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack invStack = inv.get(i);

			if (invStack.getItem() != Items.BOOK) { continue; }

			invStack.setCount(invStack.getCount() - 1);
			ItemStack ench = new ItemStack(Items.ENCHANTED_BOOK);
			EnchantmentHelper.setEnchantments(map, ench); 			// 契約書にエンチャを設定
			world.spawnEntity(new EntityItem(world, player.posX, player.posY, player.posZ, ench));
			return true;
		}

		return false;
	}

	// 限界エンチャントレベル開放
	public boolean addLimitOver (ItemStack stack, EntityPlayer player) {

		NBTTagCompound tags = stack.getTagCompound();

		if (tags == null) {
			stack.setTagCompound(new NBTTagCompound());
			tags = stack.getTagCompound();
		}

		if (!tags.hasKey(LIMIT)) {
			tags.setInteger(LIMIT, 0);
		}

		int limit = tags.getInteger(LIMIT);

		// 上限が4を超えてたら終了
		if (limit > 5) {
			return false;
		}

		// リミット上限を増やす
		tags.setInteger(LIMIT, tags.getInteger(LIMIT) + 1);

		return true;
	}

	//ツールチップの表示
  	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {

  		// シフトを押したとき
		if (Keyboard.isKeyDown(42)) {
			tooltip.add(I18n.format(TextFormatting.GOLD + "メインハンドに契約書、"));

			switch (this.data) {
			case 0:
				tooltip.add(I18n.format(TextFormatting.GOLD + "オフハンドにエンチャントしたアイテムを持って右クリック"));
				tooltip.add(I18n.format(""));
				tooltip.add(I18n.format(TextFormatting.GREEN + "効率エンチャント："));
				tooltip.add(I18n.format(TextFormatting.YELLOW + "  ・必要プレイヤーレベル：5"));
				tooltip.add(I18n.format(TextFormatting.YELLOW + "  ・上限エンチャントレベル：30"));
				tooltip.add(I18n.format(TextFormatting.GREEN + "それ以外のエンチャント："));
				tooltip.add(I18n.format(TextFormatting.YELLOW + "  ・必要プレイヤーレベル：10"));
				tooltip.add(I18n.format(TextFormatting.YELLOW + "  ・上限エンチャントレベル：10"));
				break;
			case 1:
				if (EnchantmentHelper.getEnchantments(stack).size() > 0) {
					tooltip.add(I18n.format(TextFormatting.GOLD + "インベントリに本を入れて右クリック"));
				} else {
					tooltip.add(I18n.format(TextFormatting.GOLD + "オフハンドにエンチャントしたアイテムを持って右クリック"));
				}
				break;
			case 2:
				tooltip.add(I18n.format(TextFormatting.GOLD + "プレイヤーのレベルが30以上のとき右クリック"));
				break;
			}

		} else {
			tooltip.add(I18n.format(TextFormatting.RED + "左シフトで詳しく表示"));
		}
	}
}
