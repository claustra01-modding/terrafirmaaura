# TerraFirmaAura mod

Neoforge 1.21.1環境のTerraFirmaCraftとNature's Aura連携を実装する

## 運用ルール

- 仕様追加や実装変更があった場合、この `AGENTS.md` を随時更新する。
- 形状の追加・削除、条件付き有効化の変更、ID規則の変更を優先して反映する。
- 依存元Modの取得・展開・一時解析（jar展開や素材抽出など）は、リポジトリ直下の `.tmp` で行う。
- 名前空間は`net.claustra01.tfaura`、mod名は`TerraFirmaAura`とする

## (以下好きに追記して良い)

## 現在の実装メモ

- mod idは`tfaura`、Javaパッケージは`net.claustra01.tfaura`。
- 依存関係はTerraFirmaCraft、Nature's Aura、Patchouliを必須扱いにする。
- TFC More Items系（mod id `tfc_items`）は任意依存扱いにし、存在する場合のみ対応アイテムを登録する。
- 初期ID規則:
  - TFC対応植物は`tfaura:plant/<name>`。
  - TFC対応古代木は`tfaura:wood/<type>/ancient`。
  - TFC対応worldgen植物featureは`tfaura:plant/<name>`。
  - TFC風金属アイテムは`tfaura:metal/<form>/<metal>`。
- 初期実装対象:
  - Nature's Aura由来の植物: `aura_bloom`, `aura_cactus`, `aura_mushroom`, `crimson_aura_mushroom`, `warped_aura_mushroom`。
  - brilliant fiber供給用の弱光源装飾植物: `brilliant_grass`。ブロックIDは`tfaura:plant/brilliant_grass`、TFCハサミドロップは`naturesaura:gold_fiber`。
  - Nature's Aura由来の古代木: log, stripped log, wood, stripped wood, planks, lumber item, stairs, slab, fence, log fence, fence gate, leaves, sapling, potted sapling。
- 植物の設置条件:
  - 通常植物・キノコはTFCの`BUSH_PLANTABLE_ON`。
  - サボテン系はTFCの`DRY_PLANT_PLANTABLE_ON`。
  - brilliant grassはTFCの`GRASS_PLANTABLE_ON`。
  - 苗木はTFCの`TREE_GROWS_ON`。
- テクスチャと既存基本モデルはNature's Auraのアセットを参照する。ただし stripped ancient log/wood はTFC oak stripped log/topをベースにNature's Auraのancient planks/log top寄りへ再配色した`tfaura:block/wood/stripped_log(_top)/ancient`を使う。
- 植物・苗木のアイテムモデルは`minecraft:item/generated`を使い、block/crossモデルをitem parentにしない。
- ancient leavesはclient color handlerでNature's Aura寄りの色を付ける。saplingには色を付けない。brilliant grass、ancient lumber、log系アイテムはTFC素材をNature's Aura寄りに再配色した専用PNGを使う。
- brilliant grassはTFAura側ではブロックのみ登録し、BlockItem・creative tab項目・独自ドロップアイテムは作らない。TFCハサミ（`#c:tools/shear`）で破壊した場合のみ`naturesaura:gold_fiber`を2〜4個ドロップする。
- ancient saplingはTFAura側の専用`tfaura:tick_counter` block entityを使う。TFC本体の`tfc:tick_counter`は有効ブロック検証に失敗するため使わない。
- 古代木の追加木材は通常建材寄りのTFC block type（stripped log/wood, log fence, fence gate）とTFC標準のlumber item/木工レシピ/燃料定義まで対応する。fallen leavesとtwigは現時点では追加しない。door, trapdoor, button, pressure plateは現時点では追加しない。TFCのblock entity/UI前提の木材（chest, barrel, sluice等）はblock entity type互換性を確認してから追加する。
- Nature's Auraの`ancient_tree`はconfigured featureのみで、元mod内に自然生成用placed feature/biome modifierが無いため、現時点では自然生成させない。
- `aura_bloom`系植物は専用feature経由の自然生成時にblock entityへ`justGenerated`を立て、元mod同様に150,000 auraを生成する。
- Herbivorous AbsorberはNature's Aura本体同様`#minecraft:small_flowers`を参照するため、Offering Table対応で追加したTFC小型花tag付与でTFC花も消費対象になる。
- Swamp HomiはNature's Aura本体の`BOTANIST_PICKAXE_CONVERSIONS`へTFC rockのcobble/bricksと、それらのslab/stair/wallのclean -> mossy変換を追加し、本体同様に逆変換でmossy側を消費して5,000 auraを生成できるようにする。
- Shooting MarkはTFC投射物として`TFCEntities.THROWN_JAVELIN`を30,000 aura、`TFCEntities.GLOW_ARROW`を45,000 auraで追加する。TFC loose rockは1.21.1-4.2.5ではgroundcoverの設置/回収ブロックで、投射物Entityではないため対象外。
- ancient log/woodはTFCの`LogBlock`を使い、自然logと同等に硬さ8.0・正しい工具要求・斧ストリップ・TFC一括伐採に対応する。sapling/worldgenで生成される幹は`branch_direction=down`を付け、手置きlogはTFC同様`none`のまま一括伐採対象外にする。
- ancient leavesは専用block entityとNature's Aura aura capabilityを持ち、保持auraが尽きた場合はTFAura版`tfaura:wood/leaves/decayed`へ変化する。内部containerは本家同様`NaturalAuraContainer(TYPE_OVERWORLD, 2000, 500)`、aura color `13522057`、drain時client syncを持つ。葉の距離更新/原木消失時decayはTFAura共通葉ブロックでTFC寄りに行い、ancient leavesの葉ブロックドロップはTFC同様ハサミまたはSilk Touch限定にする。
- ancient leavesはTFC葉と同様に衝突判定を持たず、プレイヤーやEntityが通り抜けられる。
- golden leavesはTFAura独自ブロック`tfaura:wood/leaves/golden`として実装する。Nature's Aura本体仕様に合わせ、stage 0〜3、stage 2以降の隣接葉伝播、stage 3の金色粒子、stage 3時のみ75%で`naturesaura:gold_leaf`ドロップを持つ。TFC葉同様に衝突判定なし。
- decayed leavesはTFAura独自ブロック`tfaura:wood/leaves/decayed`として実装する。Nature's Aura本体仕様に合わせ、ランダムtickで空気へ消える。TFC葉同様に衝突判定なし。
- `naturesaura:gold_fiber`を使ったgolden leaves変換対象はTFC/ArborFirmaCraft/TFAura/Beneath系の葉。TFCは`#tfc:seasonal_leaves`/`#tfc:fruit_tree_leaves`、他modは`#tfaura:golden_leaves_convertible`タグと`minecraft:leaves` + namespace判定で拾う。
- TFCワールド生成は`#tfc:feature/land_plants`へoverworld植物のplaced featureを追加し、各植物のplaced feature側で`tfc:climate`により温度・地下水・森林度を制限する。ただし`crimson_aura_mushroom`と`warped_aura_mushroom`はoverworld生成せず、Nature's Aura本体のNether biome modifier/configured featureを同名上書きしてTFAura版ブロックをNetherへ生成する。Nether mushroom生成密度は本家`LevelGenAuraBloom`に合わせ、feature呼び出しごとに1/10、3〜8試行、x/z ±5、上下64探索、設置可能ブロックはcrimson/warpedそれぞれ対応nyliumのみとする。
- Nature's Aura由来金属はTFAura側の独自アイテムとして実装し、Nature's Aura本体インゴットをそのままTFC金属フォームとしては使わない。
- TFC風金属実装対象は`infused_iron`, `tainted_gold`, `sky`, `depth`。
- 標準金属フォームは常時登録: ingot, double_ingot, sheet, double_sheet, rod。
- TFC More Items系フォームは`tfcmu2`/metallum同様に`Optional`扱いで、`tfc_items`がロードされている場合のみ登録: foil, gear, heavy_sheet, nail, ring, rivet, screw, stamen, wire。
- 溶融金属Fluidはmetallumと同様、TFC名前空間の`tfc:metal/<metal>` / `tfc:metal/flowing_<metal>`として登録する。
- 溶融金属Fluidの表示はTFC/Metallum 1.21.1と同様、`RegisterClientExtensionsEvent`で`FluidRendererExtension`を登録し、`tfc:block/molten_still`/`tfc:block/molten_flow`へ金属色tintを乗せる。`assets/tfc/blockstates/fluid/metal/<metal>.json`と`assets/tfc/models/block/fluid/metal/<metal>.json`もTFC名前空間側へ追加する。
- 金属レシピはTFCのwelding/anvil/heatingを基本にし、Nature's Aura本体インゴットからTFAuraインゴットへの変換、altar/offering/depth作成系のTFAura版を用意する。
- Nature's Aura装備・ツールは元の作業台レシピを同名上書きし、TFC式の中間素材を使う。装備対象金属は`infused_iron`, `sky`, `depth`のみで、`tainted_gold`には使い道のないtool head/unfinished armorを追加しない。
- 装備・ツール用中間素材は`tfaura:metal/<form>/<metal>`で登録する。tool head/bladeは`pickaxe_head`, `axe_head`, `shovel_head`, `hoe_head`, `sword_blade`、未完成防具は`unfinished_helmet`, `unfinished_chestplate`, `unfinished_greaves`, `unfinished_boots`。
- Nature's Auraツール完成レシピはTFCの`advanced_shaped_crafting`同様に中間素材 + `#c:rods/wooden`を使い、`tfc:copy_forging_bonus`を適用する。Nature's Aura防具完成レシピはTFCの`welding`同様にunfinished armor + sheet/double_sheetを使い、`bonus: copy_best`を適用する。
- 装備・ツール用中間素材と完成済みNature's Aura装備はTFC heatingに対応する。完成品はTFC同様`use_durability: true`で溶かし戻し量を耐久値に連動させる。
- 金属アイテムテクスチャは`tfaura:item/metal/<form>/<metal>`へ出力し、モデルはTFAura内テクスチャを参照する。
- 金属テクスチャ生成は`.tmp/generate_tfaura_metal_textures.py`で行う。Nature's Aura本体ingotから`shadow`/`mid`/`highlight`/`glint`パレットを抽出し、TFCの`wrought_iron`標準フォームとTFC More Items互換フォームの輝度テンプレートへ補間適用する。
- 装備・ツール用中間素材テクスチャ生成は`.tmp/generate_tfaura_equipment_recipes.py`で行う。TFCの`wrought_iron` tool head/unfinished armorを形状テンプレートにし、Nature's Aura本体ingotパレットへ再配色する。同スクリプトは中間素材レシピ、元装備レシピ上書き、heating、common item tags、item models、langも生成する。
- More Items系フォームのテンプレートは、`tfc_items` jarが依存に入っていない現状では参照repo（TFC-metallum-overhaul）の`compressed_iron`生成済みテクスチャを形状・輝度ベースとして使う。
- ancient wood / brilliant grass系テクスチャ生成は`.tmp/generate_ancient_stripped_textures.py`で行う。TFC oakのstripped block/log系item、TFC lumber、TFC bluegrassを入力に、Nature's Aura ancient planks/log/gold fiber寄りの落ち着いたパレットへ再配色する。
- TFC/Arbor系植物向けaura効果は独自drain spot effectとして`tfaura:tfc_plant_boost`と`tfaura:tfc_plant_decay`を登録する。
- Nature's Auraの基本aura値は`IAuraChunk.DEFAULT_AURA = 1,000,000`。ただしdrain spot effectへ渡る`spotAura`と`IAuraChunk.getAuraAndSpotAmountInArea`の合算値は基本値からの差分として扱われ、`0`が中立/デフォルト、正値が余剰、負値が不足を表す。
- aura効果の段階は差分絶対値で判定する。tier 1は25%以上（250,000〜599,999）、tier 2は60%以上（600,000〜999,999）、tier 3は100%以上（1,000,000以上）。強度`intensity`は`abs(delta) / 1,000,000`を0.25〜2.0にclampする。
- aura効果対象はTFC/TFAura/Beneath/ArborFirmaCraft/Arbor_FirmaCraft/AFC名前空間の植物系ブロック。`#tfaura:aura_nature_effects`、`#minecraft:leaves`、`#minecraft:saplings`、`#minecraft:flowers`、`#minecraft:small_flowers`、TFCの`natural_regrowing_plants`/`spreading_bushes`/`thorny_bushes`/`fruit_tree_saplings`/`bamboo_sapling`/`grass`/葉タグを対象にする。ただしTFAura golden leaves自体は対象外。
- 正のaura効果はspotAuraが正、半径30の周辺差分auraが250,000以上で発動する。探索回数係数は`ceil(abs(delta) / 80,000 / spotCount)`（最大160）、探索距離は`abs(delta) / 100,000`を4〜45にclampする。
- 正のauraのrandom tick試行はtier 1で1回、tier 2で3回、tier 3で`6 + floor(intensity * 2)`回。TFC通常苗木は追加でtier分を足し、1〜12回にclampする。
- 正のauraの具体効果: TFC通常苗木はtick counterを`24,000 * intensity`（6,000〜240,000にclamp）進める。TFC果樹苗木も同量のTickingPlant tickを加える。TFC竹苗と汎用植物は段階別random tickを行う。TFC作物は`lastGrowthTick`を`18,000 * intensity`（6,000〜336,000にclamp）巻き戻し、進まない場合は成長量を`0.04 * intensity`（0.02〜0.35にclamp）だけ直接補助する。TFC grassは段階別random tick +1回、dirtからgrass化はtier 2以上かつ近くにgrassがある場合のみ。
- 正のauraの消費量は成功対象ごとにtier 1で2,500、tier 2で4,000、tier 3で5,500 auraをdrainする。
- 負のaura効果はspotAuraが負、半径50の周辺差分aura絶対値が250,000以上で発動する。探索回数係数は`ceil(abs(delta) / 90,000 / spotCount)`（最大400）、探索距離は`abs(delta) / 70,000`を4〜85にclampする。
- 負のauraの具体効果: tier 1は主に成長遅延で、作物growthを0.015減らし、苗木系tick counterをresetする。tier 2は作物growthを`0.05 * intensity`（0.03〜0.12にclamp）減らし、dead crop化3〜12%、grass die 3%、苗木消滅2%、汎用非葉植物消滅2%。tier 3は作物growthを`0.12 * intensity`（0.08〜0.35にclamp）減らし、dead crop化12〜75%、grass die 25〜85%、苗木消滅8〜65%、汎用非葉植物消滅8〜55%。葉はNature's Aura本体のGrassDieEffectに合わせ、直接破壊せず`tfaura:wood/leaves/decayed`へ変換し、その後decayed leaves自身のrandom tickで空気へ消える。
- 葉のdecayed化対象はTFC/TFAura/Beneath/ArborFirmaCraft/Arbor_FirmaCraft/AFC名前空間で、`#minecraft:leaves`、`#tfaura:golden_leaves_convertible`、またはTFC葉タグに入る対応葉。TFAura golden leavesとTFAura decayed leaves自身は対象外。
- Natural Altarの構成ブロックはNature's Aura本体の`data/naturesaura/tags/block/altar_*`を拡張して対応する。TFC通常木材は`#minecraft:planks`、TFC岩石レンガは`#minecraft:stone_bricks`経由で利用でき、TFC chiseled rockは`altar_fancy_brick`へ明示追加する。TFAura ancient planksとArbor系planksタグも`altar_wood`へ追加する。
- Ritual of the ForestはNature's Aura本体の`data/naturesaura/recipe/tree_ritual/*`を同名上書きし、中心苗木を`#tfaura:tree_ritual_saplings`へ差し替える。このタグはTFAura ancient sapling、TFC通常木sapling、BeneathのNether sapling、Arbor系の1x1/single sapling optional tagsだけを含む。TFC果樹苗・竹苗・2x2成長前提のsaplingはRitual対象にしない。
- Tree Ritual内のNature's Aura金属素材はTFAura金属へ寄せる。`ancient_sapling` ritualは`tfaura:wood/sapling/ancient`を出力する。レシピ素材はTFC素材またはTFCが供給するcommon tagへ寄せ、`tfc:torch`や`tfc:poured_glass`のような明確な1:1対応はレシピへ直接置換する。TFCに相当品がない素材は元の`minecraft:*`のまま残し、複数候補が必要なsugarcane/magma rocksだけTFAura item tagで受ける。植物素材は本家の花系要求に寄せ、必要なら`#minecraft:small_flowers`を使う。
- Nature's Aura本体の主要素材生成レシピは同名上書きでTFAura金属へ寄せる。altarのinfused iron/tainted gold、offeringのsky ingot、depth ingot creationはTFAura metal ingotを出力する。
- Nature's Auraの`gold_powder`通常レシピはTFC石臼`type: tfc:quern`へ同名上書きし、`naturesaura:gold_leaf`から`naturesaura:gold_powder`を2個出す。`gold_brick`は`naturesaura:gold_fiber` + `#tfaura:tfc_stone_bricks`でクラフトする。このタグは通常TFC rock bricksのみを含み、cracked/mossy/chiseledやvanilla bricks/fire bricksは含めない。
- Offering Table周囲に置く花はNature's Aura本体の判定に合わせて`#minecraft:small_flowers`で互換対応する。TFAura植物に加え、TFCの小型花をTFAura側の同タグにも明示追加する。
- TFAura creative tabのアイコンはancient wood lumber（`tfaura:wood/lumber/ancient`）を使う。
