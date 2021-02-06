package tamagotti.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class RecipeHelper {

	public Item material;
	public Item stick;
	public Item pick;
	public Item axe;
	public Item shovel;
	public Item sword;

	public Item hel;
	public Item chest;
	public Item leg;
	public Item boot;

	public Block ore;

	public Item rush;

	public RecipeHelper (Item material, Item stick, Item pick, Item axe, Item shovel, Item sword) {
		this.material = material;
		this.stick = stick;
		this.pick = pick;
		this.axe = axe;
		this.shovel = shovel;
		this.sword = sword;
	}

	public RecipeHelper (Item material, Item hel, Item chest, Item leg, Item boot) {
		this.material = material;
		this.hel = hel;
		this.chest = chest;
		this.leg = leg;
		this.boot = boot;
	}

	public RecipeHelper (Item material, Block ore) {
		this.material = material;
		this.ore = ore;
	}

	public RecipeHelper (Item material, Item rush) {
		this.material = material;
		this.rush = rush;
	}

	// 素材の取得
	public Item getMaterial ( ) {
		return this.material;
	}

	// 棒の取得
	public Item getStick () {
		return this.stick;
	}

	// ピッケルの取得
	public Item getPick () {
		return this.pick;
	}

	// アックスの取得
	public Item getAxe () {
		return this.axe;
	}

	// シャベルの取得
	public Item getShovel () {
		return this.shovel;
	}

	// ソードの取得
	public Item getSword () {
		return this.sword;
	}

	// ヘルメットの取得
	public Item getHel () {
		return this.hel;
	}

	// チェストプレートの取得
	public Item getChest () {
		return this.chest;
	}

	// レギンスの取得
	public Item getLeg () {
		return this.leg;
	}

	// ブーツの取得
	public Item getBoot () {
		return this.boot;
	}

	// 鉱石の取得
	public Block getOre () {
		return this.ore;
	}

	// たまごっちラッシュ
	public Item getRush () {
		return this.rush;
	}
}
