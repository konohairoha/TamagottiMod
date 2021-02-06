package tamagotti.init.items.tool.tamagotti.iitem;

public interface IShotter {

	// 弾数を設定
	void setAmmo (int ammo) ;

	// 弾数を取得
	int getAmmon ();

	// 最大弾数を設定
	void setMaxAmmo (int ammo);

	// 最大弾数を取得
	int getMaxAmmo();
}
