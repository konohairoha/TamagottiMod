package tamagotti.init.items.tool.tamagotti.iitem;

public interface IAmulet {

	// 死亡時にアイテム消費するかどうか
	boolean isShrink ();

	// 死亡時に必要な経験値
	int needExp();
}
