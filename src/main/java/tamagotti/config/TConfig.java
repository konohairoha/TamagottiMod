package tamagotti.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class TConfig {

	//メインクラスのPreInitにて最初に読み込んだほうがよさげ
	//すべてコンフィグで扱う変数にはStatic参照ができるようにすること
	private TConfig() {}
	public static final TConfig INSTANCE = new TConfig();

	public static boolean add_rangeh = true;		//range = 範囲 harvest = 採掘 採掘ツール用
	public static int spawnchance_sakura = 15;		//桜生成回数。（一応最低、最大を決めておきます。最大100まで）
	public static int spawnchance_bamboo = 10;		//竹生成回数。（一応最低、最大を決めておきます。最大50まで）
	public static int spawnchance_desert = 4;		//砂漠ダンジョン生成回数。（一応最低、最大を決めておきます。最大20まで）
	public static boolean housegen = true;			//家、ダンジョンの生成
	public static boolean toregen = true;			//鉱石の生成
	public static boolean foodeffect = true;
	public static boolean mobspawn = false;

	public void load(File file) {
		File cfgFile = new File(file, "tamagottimod.cfg");
		TConfig.INSTANCE.load(new Configuration(cfgFile));
	}

	public void load(Configuration cfg) {

		try {

			//初期読み込み
			cfg.load();
			cfg.setCategoryComment("item_add_config","If those flag is set to true , those items are added.");
			cfg.setCategoryComment("item_add_config","If those flag is set to false , those items are not added.");
			cfg.setCategoryComment("world_spawn_config","This configuration will adjust what is generated to the world.");
			cfg.setCategoryComment("food_add_config","Setting whether there is effect of food");

			//Property　別変数　=　cfg.get("小さいカテゴリでまとめるための文字列", "Key(例：add bettyutool=true　みたいな)", コンフィグ参照用変数)

			Property look_rh = cfg.get("item_add_config", "add range_harvest tools & ruby_tools", add_rangeh,
							"When this flag is set to true, add range_harvest tools and ruby_tools are added. default = true");

			Property look_sp_chsakura = cfg.get("world_spawn_config", "spawn chance at sakura", spawnchance_sakura,
					"(int) This number is the number of times to generate in the world. default = 15, max = 100");

			Property look_sp_chbamboo = cfg.get("world_spawn_config", "spawn chance at bamboo", spawnchance_bamboo,
					"(int) This number is the number of times to generate in the world. default = 10, max = 50");

			Property look_sp_desert = cfg.get("world_spawn_config", "spawn chance at desert", spawnchance_desert,
					"(int) This number is the number of times to generate in the world. default = 4, max = 50");

			Property look_house = cfg.get("generator_config", "add house generator", housegen,
					"When this flag is set to true, house generator is added. default = true");

			Property look_tore = cfg.get("generator_config", "add ore generator", toregen,
					"When this flag is set to true, ore generator is added. default = true");

			Property look_food = cfg.get("food effect_config", "add food effect", foodeffect,
					"Setting whether there is effect of food. default = true");

			Property mob_spawn = cfg.get("mob_spawn_config", "add mob spawn", mobspawn,
					"Setting whether there is mob spawn. default = false");

			//look_○○　で　読み込んだ変数をStatic参照できる変数に代入
			add_rangeh = look_rh.getBoolean();
			housegen = look_house.getBoolean();
			toregen = look_tore.getBoolean();
			foodeffect = look_food.getBoolean();
			mobspawn = mob_spawn.getBoolean();

			//桜
			spawnchance_sakura = look_sp_chsakura.getInt();
			if(spawnchance_sakura > 100) TConfig.spawnchance_sakura = 15;
			//竹
			spawnchance_bamboo = look_sp_chbamboo.getInt();
			if(spawnchance_bamboo > 50) TConfig.spawnchance_bamboo = 10;
			spawnchance_desert = look_sp_desert.getInt();
			if(spawnchance_desert > 50) TConfig.spawnchance_desert = 10;

		} catch (Exception e) {
			e.printStackTrace();
			//これはなんかのエラー用に置いておくっぽい。
		} finally {
			//コンフィグで書き換えられている内容を保存するためにfinallyブロックで呼び出す。
			cfg.save();
		}
	}
}
