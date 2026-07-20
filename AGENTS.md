# TerraFirmaAura mod

NeoForge 1.21.1環境のTerraFirmaCraftとNature's Aura連携を実装する。

## 共通開発ルール

- 仕様ファイル名は大文字の `AGENTS.md` に統一する。
- 本書は現在維持すべき仕様を記録し、未実装事項は実装済みと区別する。挙動、対応版、依存、ID、登録、有効化条件、生成規則、検証手順の変更と同時に更新する。
- READMEは利用者向けの短い概要とbuild入口に絞り、詳細仕様や作業履歴を重複掲載しない。
- ライセンスと第三者表示はrootの `LICENCE` 一つへ統合し、別のlicense/noticeファイルを作らない。
- 現在値は `gradle.properties`、Gradle設定、Mod metadata、コード、同梱dataを正本とする。対象版の公式ソース、依存ソース、実JAR/dataを確認し、別バージョンの記憶でAPIやIDを決めない。
- 公開API、registry、tag、dataを優先し、Mixin、accessor、reflectionは必要な対象へ限定する。任意依存のクラスを通常ロード経路から参照せず、client/server境界を守る。
- 公開済みregistry ID、namespace、config keyはworld/data pack互換性を優先し、依頼なしに破壊的変更を行わない。他Mod namespaceは意図したtag追加・同名上書きだけに使う。
- 依存JAR、展開物、比較画像、解析・生成scriptは `.tmp/` に置いてGit管理外にする。仕様と生成済みresourceを正本とする。
- JSONはBOMなしUTF-8とし、対象版のpack layoutに従う。`.gradle/`、`build/`、`run/`、`runs/`、IDE metadata、依存JARを変更対象へ含めない。
- 変更前に既存の登録、命名、resource配置、生成scriptを確認し、依頼外のrename、format変更、依存・version更新、無関係な既存差分を混ぜない。
- 通常は `./gradlew compileJava`、完了時は `./gradlew build` を実行する。dataは全JSONをparseし、optional連携・Mixin・client/server変更はMod有無と専用サーバー安全性を重点確認する。
- Minecraftクライアントはランタイム確認が必要な変更または明示依頼時だけ起動し、未実施の検証は理由と範囲を報告する。

## 関連文書

- aura生成・消費コンテンツの一覧、概要、実装済み数値、未対応分の方針案は `docs/aura_content.md` に記録する。
- 未対応分は対応状態を明記し、実装済み内容と混同しないようにする。

## 基本情報

- mod名は `TerraFirmaAura`、mod idは `tfaura`、Javaパッケージは `net.claustra01.tfaura`。
- TerraFirmaCraft、Nature's Aura、Patchouliを必須依存にする。
- TFAura creative tabのアイコンは ancient wood lumber（`tfaura:wood/lumber/ancient`）を使う。

## ID規則

- TFC対応植物: `tfaura:plant/<name>`
- TFC対応古代木: `tfaura:wood/<type>/ancient`
- TFC対応worldgen植物feature: `tfaura:plant/<name>`
- TFC風金属アイテム: `tfaura:metal/<form>/<metal>`

## 植物・木材

- Nature's Aura由来植物として `aura_bloom`, `aura_cactus`, `aura_mushroom`, `crimson_aura_mushroom`, `warped_aura_mushroom` をTFC設置条件付きで追加する。
- brilliant fiber供給用の弱光源装飾植物として `brilliant_grass` を追加する。ブロックIDは `tfaura:plant/brilliant_grass`、TFCの `ShortGrassBlock` をbluegrass相当の成長段階で継承し、`#tfc:natural_regrowing_plants` による密度制限付き自然拡散を行う。破壊lootはTFC short grassと同じalternatives構成とし、TFCハサミ（`#c:tools/shear`）なら `naturesaura:gold_fiber` 2〜4個、`#tfc:tools/sharp` なら `tfc:straw` 1個、それ以外は何も落とさない。BlockItem・creative tab項目・独自ドロップアイテムは作らない。
- 植物の設置条件は、通常植物・キノコがTFC `BUSH_PLANTABLE_ON`、サボテン系が `DRY_PLANT_PLANTABLE_ON`、brilliant grassが `GRASS_PLANTABLE_ON`、苗木が `TREE_GROWS_ON`。
- 植物・苗木のアイテムモデルは `minecraft:item/generated` を使い、block/crossモデルをitem parentにしない。
- Nature's Aura由来の古代木として log, stripped log, wood, stripped wood, planks, lumber item, stairs, slab, fence, log fence, fence gate, leaves, sapling, potted sapling を追加する。
- fallen leavesとtwigは追加しない。door, trapdoor, button, pressure plateも追加しない。TFCのblock entity/UI前提の木材（chest, barrel, sluice等）はblock entity type互換性を確認してから追加する。
- ancient woodのlog, stripped log, wood, stripped wood, planks, stairs, slab, fence, log fence, fence gateはTFCの `Wood.BlockType` factoryで生成し、各形状のTFCクラス・物性・専用BlockItemをそのまま利用する。log/woodは硬さ8.0、正しい工具要求、斧ストリップ、TFC一括伐採に対応する。sapling/worldgenで生成される幹は `branch_direction=down`、手置きlogはTFC同様 `none` のまま一括伐採対象外にする。
- ancient saplingは `TFCSaplingBlock` 派生とTFCのsapling用BlockItemを使い、block entityのみTFAura専用 `tfaura:tick_counter` とする。TFC本体の `tfc:tick_counter` は有効ブロック検証に失敗するため使わない。
- ancient/golden/decayed leavesは `TFCLeavesBlock` 派生とし、距離10の支持判定、fluidlogging、葉内の移動減速、季節粒子、衝突判定なしをTFCから継承する。fallen leaves/twig supplierは持たせない。ancient/golden leavesはTFC本体の原木距離更新と原木消失時decayを使い、葉ブロックドロップはTFC同様ハサミまたはSilk Touch限定にする。
- golden leavesはTFAura独自ブロック `tfaura:wood/leaves/golden` として実装する。Nature's Aura本体仕様に合わせ、stage 0〜3、stage 2以降の隣接葉伝播、stage 3の金色粒子、stage 3時のみ75%で `naturesaura:gold_leaf` をドロップする。
- decayed leavesはTFAura独自ブロック `tfaura:wood/leaves/decayed` として実装し、Nature's Aura本体同様ランダムtickで空気へ消える。
- `naturesaura:gold_fiber` によるgolden leaves変換対象はTFC/ArborFirmaCraft/TFAura/Beneath系の葉。TFCは `#tfc:seasonal_leaves` / `#tfc:fruit_tree_leaves`、他modは `#tfaura:golden_leaves_convertible` と `minecraft:leaves` + namespace判定で拾う。

## Worldgen

- TFCワールド生成は `#tfc:feature/land_plants` へoverworld植物のplaced featureを追加し、placed feature側で `tfc:climate` による温度・地下水・森林度制限を行う。
- `crimson_aura_mushroom` と `warped_aura_mushroom` はoverworld生成せず、Nature's Aura本体のNether biome modifier/configured featureを同名上書きしてTFAura版ブロックをNetherへ生成する。
- Nether mushroom生成密度は本家 `LevelGenAuraBloom` に合わせ、feature呼び出しごとに1/10、3〜8試行、x/z ±5、上下64探索、設置可能ブロックはcrimson/warpedそれぞれ対応nyliumのみとする。
- Nature's Auraの `ancient_tree` はconfigured featureのみで、元mod内に自然生成用placed feature/biome modifierが無いため自然生成させない。

## 金属・装備

- Nature's Aura由来金属はTFAura側の独自アイテムとして実装し、Nature's Aura本体インゴットをTFC金属フォームとしては使わない。
- TFC風金属は `infused_iron`, `tainted_gold`, `sky`, `depth` を対象にする。
- 金属フォームは ingot, double_ingot, sheet, double_sheet, rodのみ登録する。TFC More Items系の追加フォームには対応しない。
- 溶融金属FluidはTFC名前空間の `tfc:metal/<metal>` / `tfc:metal/flowing_<metal>` として登録する。表示はTFC/Metallum同様 `FluidRendererExtension` で `tfc:block/molten_still` / `tfc:block/molten_flow` に金属色tintを乗せる。
- Nature's Aura装備・ツールは元レシピを同名上書きし、TFAura金属のingotから通常の作業台で直接クラフトする。tool head/bladeやunfinished armorなどの中間素材は登録しない。
- ツールは `#c:ingots/<metal>` + `#c:rods/wooden`、防具は `#c:ingots/<metal>` を使う。対象金属は `infused_iron`, `sky`, `depth` のみとする。
- 完成済みNature's Aura装備・ツールはTFC heatingに対応する。溶かし戻し量はクラフトに使うingot数に合わせ、`use_durability: true` で耐久値に連動させる。

## レシピ・タグ連携

- Nature's Aura本体の全recipe IDをTFAura側で上書きする。TFCに明確な相当品がある素材はTFC itemへ、岩石・金属・宝石など種類を許容すべき素材は既存のTFC/Common tagへ置換し、Nether/End固有素材など相当品がないものは維持する。
- Nature's Aura本体の `infused_iron`, `tainted_gold`, `sky_ingot`, `depth_ingot` を要求・出力するレシピは、対応する `tfaura:metal/ingot/<metal>` へ統一する。古代木素材の参照もTFAura ancient woodへ統一し、本体古代木のクラフトレシピ6種は二重系統を避けるため無効化する。
- vanilla grass素材は存在しないTFC単一grassへ固定せず、TFCの陸生grassを列挙した `#tfaura:tfc_grasses` で受ける。
- Nature's Auraレシピで木材を材料に使う箇所はplanksではなくTFC lumberへ統一する。汎用planks tagは `#tfc:lumber`、TFAura ancient planksの直接参照は `tfaura:wood/lumber/ancient` へ置換し、レシピ出力としてのplanksは変更しない。
- Altar of Birthingが無効な間は `animal_spawner/*` の全spawn recipeも `neoforge:false` で無効化する。
- Natural Altarの構成ブロックはNature's Aura本体の `data/naturesaura/tags/block/altar_*` を拡張して対応する。TFC通常木材は `#minecraft:planks`、TFC岩石レンガは `#minecraft:stone_bricks` 経由で利用でき、TFC chiseled rockは `altar_fancy_brick` へ明示追加する。TFAura ancient planksとArbor系planksタグも `altar_wood` へ追加する。
- Ritual of the ForestはNature's Aura本体の `data/naturesaura/recipe/tree_ritual/*` を同名上書きし、中心苗木を `#tfaura:tree_ritual_saplings` へ差し替える。TFC果樹苗・竹苗・2x2成長前提のsaplingは対象にしない。
- Tree Ritual内のNature's Aura金属素材はTFAura金属へ寄せる。TFCに相当品がない素材は元の `minecraft:*` のまま残し、複数候補が必要なsugarcane/magma rocksだけTFAura item tagで受ける。植物素材は本家の花系要求に寄せ、必要なら `#minecraft:small_flowers` を使う。
- Nature's Aura本体の主要素材生成レシピは同名上書きでTFAura金属へ寄せる。altarのinfused iron/tainted gold、offeringのsky ingot、depth ingot creationはTFAura metal ingotを出力する。
- `gold_powder` はTFC石臼 `type: tfc:quern` へ同名上書きし、`naturesaura:gold_leaf` から `naturesaura:gold_powder` を2個出す。
- `gold_brick` は `naturesaura:gold_fiber` + `#tfaura:tfc_stone_bricks` でクラフトする。このタグは通常TFC rock bricksのみを含み、cracked/mossy/chiseledやvanilla bricks/fire bricksは含めない。
- Offering Table周囲に置く花はNature's Aura本体の判定に合わせて `#minecraft:small_flowers` で互換対応する。TFAura植物に加え、TFCの小型花をTFAura側の同タグにも明示追加する。
- Canopy Diminisher（`naturesaura:oak_generator`）、Disentangler of Mortals（`naturesaura:animal_generator`）、Altar of Birthing（`naturesaura:animal_spawner`）、Extraneous Firestarter（`naturesaura:furnace_heater`）、Armorer's Aid（`naturesaura:blast_furnace_booster`）、Winter's Calling（`naturesaura:snow_creator`）、Cloudshifter（`naturesaura:weather_changer`）はTFC環境では保留扱いにし、`[Disabled]` tooltipを追加して同名レシピを `neoforge:false` 条件で削除する。
- Energetic Aura Forge（`naturesaura:rf_converter`）は本体のAura -> Forge Energy一方向変換を有効化する。multiblock、Aura閾値、変換速度、`auraToRFRatio` は本体仕様を維持し、レシピのsky ingot 2個だけTFAura sky ing 2枚へ置換する。redstone block、token 2種、conversion catalystは相当品がないため維持する。
- Everlasting Spring（`naturesaura:spring`）はFluidHandlerとBucketPickupを本家同様のvanilla waterのまま維持する。TFC木製バケツ等の特殊容器へ差し替えるmixinは使わない。消費aura量とlava/cauldron/sponge/farmland ticket等の本家挙動も維持する。レシピの石材は `#tfaura:tfc_stone_bricks`、水素材は `tfc:fluid_content` の `#tfc:any_fresh_water` へ寄せる。

## Aura連携

- aura生成・消費コンテンツの一覧と概要は `docs/aura_content.md` を参照する。
- TFC/Arbor系植物向けaura効果は独自drain spot effectとして `tfaura:tfc_plant_boost` と `tfaura:tfc_plant_decay` を登録する。
- Increase of Fertility（`naturesaura:animal`）はTFAura側で差し替え、TFC家畜の繁殖促進効果として扱う。正のaura + Effect Powderのみで発動し、成長促進・成長阻害は行わない。TFC本体のready条件（成体、親密度0.3以上、空腹でない、未受精、交配クールダウン終了、同class異性）を満たす近距離ペアだけを対象にし、メス側の `getBreedOffspring(server, male)` を呼ぶことで、哺乳類の妊娠・遺伝子保存、卵産み系の有精卵フラグなどをTFC本体処理へ委ねる。発動条件と確率は本家AnimalEffect相当で、周辺aura差分1,500,000以上、半径5〜35、chance最大50、成功時3,500 auraを消費する。
- Mineral Amassing（`naturesaura:ore_spawn`）はTFC鉱床・岩種・鉱石品質と衝突するため全次元で利用不可にする。Tree Ritualレシピは `neoforge:false` で削除する。既存アイテムやcreative取得分のランタイム無効化は考慮しない。
- Inexplicable Anger（`naturesaura:anger`）は本家のNeutralMob対象を維持した上で、TFC家畜・野生動物にも拡張する。TFC predator系はBrainの `ATTACK_TARGET` と `Activity.FIGHT` も設定し、TFC AIへ伝わりやすくする。
- ancient leavesは専用block entityとNature's Aura aura capabilityを持つ。内部containerは本家同様 `NaturalAuraContainer(TYPE_OVERWORLD, 2000, 500)`、aura color `13522057`、drain時client syncを持ち、保持auraが尽きた場合は `tfaura:wood/leaves/decayed` へ変化する。
- Herbivorous AbsorberはNature's Aura本体同様 `#minecraft:small_flowers` を参照するため、Offering Table対応で追加したTFC小型花tag付与でTFC花も消費対象になる。
- Swamp HomiはNature's Aura本体の `BOTANIST_PICKAXE_CONVERSIONS` へTFC rockのcobble/bricksと、それらのslab/stair/wallのclean -> mossy変換を追加する。
- Shooting MarkはTFC投射物として `TFCEntities.THROWN_JAVELIN` を30,000 aura、`TFCEntities.GLOW_ARROW` を45,000 auraで追加する。TFC loose rockは1.21.1-4.2.5ではgroundcoverの設置/回収ブロックで、投射物Entityではないため対象外。
- Everlasting SpringはFluidHandler経由の表示・drain対象を本家同様 `minecraft:water` のままにする。`BlockSpring#pickupBlock` も本家戻り値を維持し、鉄バケツで汲んだ場合はvanilla water bucketになる。

## Item Size / Weight

- Nature's Auraの全実アイテムモデルに `tfc:item_size` データを追加し、`docs/item_size.md` の分類表を正とする。見た目だけで決めず、TFC本体の明示定義、クラス別フォールバック、最も近い既存設備の順で決定する。
- TFCの基底値は一般Itemがvery small/very light、一般BlockItemがsmall/light。主要な明示値はplants・powdersがtiny/very light、ingotがlarge/medium、logsがvery large/medium、slabがsmall/very light、stairsがsmall/light、railsがlarge/very light、toolsがvery large/very heavy、armorがlarge/very heavy。
- Nature's Aura本家ancient woodとTFAura ancient woodは同形状ごとに同じ値へ揃える。ancient stickは `#c:rods/wooden` のTFC値、TFAura lumberもレシピ上の代替関係に合わせてnormal/lightへ揃える。
- 機能ブロックは一律分類せず、table/chest/minecart/bloomery/power loom/grill等のTFC類似設備を個別に参照する。通常の建材・金属storage block・葉・苗木はBlockItem基底のsmall/lightとする。
- TFAura金属フォームはCommon tag経由でTFC本体のingot/sheet/rod等の既存size/weight定義を利用する。

## 生成スクリプト

- ancient wood / brilliant grass系テクスチャ生成は `.tmp/generate_ancient_stripped_textures.py` で行う。TFC oakのstripped block/log系item、TFC lumber、TFC bluegrassを入力にする。
- 金属テクスチャ生成は `.tmp/generate_tfaura_metal_textures.py` で行う。TFC-metallum-overhaul最新版に合わせ、通常版（平均色、輝度contrast 1.5、mapped下限0.05）と高輝度版（shadow/mid/highlight/glint、入力輝度contrast 1.45）を金属設定で選択できるようにする。Nature's Aura由来4金属はTFCフォームとの馴染みを優先し、すべて通常版を使う。
- 基本フォームはTFC wrought ironを形状・輝度マスクとして使う。ingot pile用 `assets/tfc/textures/block/metal/smooth/<metal>.png` もTFC wrought iron smoothから同方式で生成する。sheet pile用アセットは追加しない。
- 金属登録・基本レシピ生成は `.tmp/generate_tfaura_metals.py`、装備・ツールの直接クラフトレシピと完成品heating生成は `.tmp/generate_tfaura_equipment_recipes.py` を使う。
- Nature's Aura全レシピのTFC素材置換とitem size/weight生成は `.tmp/generate_naturesaura_tfc_compat.py` を使う。
