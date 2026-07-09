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
  - brilliant fiber供給用の弱光源装飾植物: `brilliant_grass`。ブロックIDは`tfaura:plant/brilliant_grass`、鎌ドロップは`naturesaura:gold_fiber`。
  - Nature's Aura由来の古代木: log, stripped log, wood, stripped wood, planks, lumber item, stairs, slab, fence, log fence, fence gate, leaves, sapling, potted sapling。
- 植物の設置条件:
  - 通常植物・キノコはTFCの`BUSH_PLANTABLE_ON`。
  - サボテン系はTFCの`DRY_PLANT_PLANTABLE_ON`。
  - brilliant grassはTFCの`GRASS_PLANTABLE_ON`。
  - 苗木はTFCの`TREE_GROWS_ON`。
- テクスチャと既存基本モデルはNature's Auraのアセットを参照する。ただし stripped ancient log/wood はTFC oak stripped log/topをベースにNature's Auraのancient planks/log top寄りへ再配色した`tfaura:block/wood/stripped_log(_top)/ancient`を使う。
- 植物・苗木のアイテムモデルは`minecraft:item/generated`を使い、block/crossモデルをitem parentにしない。
- ancient leavesはclient color handlerでNature's Aura寄りの色を付ける。saplingには色を付けない。brilliant grass、ancient lumber、log系アイテムはTFC素材をNature's Aura寄りに再配色した専用PNGを使う。
- brilliant grassはTFAura側ではブロックのみ登録し、BlockItem・creative tab項目・独自ドロップアイテムは作らない。TFC鎌（`#c:tools/scythe`）で破壊した場合のみ`naturesaura:gold_fiber`を2〜4個ドロップする。
- ancient saplingはTFAura側の専用`tfaura:tick_counter` block entityを使う。TFC本体の`tfc:tick_counter`は有効ブロック検証に失敗するため使わない。
- 古代木の追加木材は通常建材寄りのTFC block type（stripped log/wood, log fence, fence gate）とTFC標準のlumber item/木工レシピ/燃料定義まで対応する。fallen leavesとtwigは現時点では追加しない。door, trapdoor, button, pressure plateは現時点では追加しない。TFCのblock entity/UI前提の木材（chest, barrel, sluice等）はblock entity type互換性を確認してから追加する。
- Nature's Auraの`ancient_tree`はconfigured featureのみで、元mod内に自然生成用placed feature/biome modifierが無いため、現時点では自然生成させない。
- `aura_bloom`系植物は専用feature経由の自然生成時にblock entityへ`justGenerated`を立て、元mod同様に150,000 auraを生成する。
- ancient leavesは専用block entityとNature's Aura aura capabilityを持ち、保持auraが尽きた場合は`naturesaura:decayed_leaves`へ変化する。
- ancient leavesはTFC葉と同様に衝突判定を持たず、プレイヤーやEntityが通り抜けられる。
- golden leavesはTFAura独自ブロックを作らず、Nature's Aura本体の`naturesaura:golden_leaves`を使う。`naturesaura:gold_fiber`をTFC葉へ使った場合は本体golden leavesへ変換し、本体golden leavesのrandom tick末尾にmixinでTFC葉への伝播補助を足す。
- TFCワールド生成は`#tfc:feature/land_plants`へplaced featureを追加し、各植物のplaced feature側で`tfc:climate`により温度・地下水・森林度を制限する。
- Nature's Aura由来金属はTFAura側の独自アイテムとして実装し、Nature's Aura本体インゴットをそのままTFC金属フォームとしては使わない。
- TFC風金属実装対象は`infused_iron`, `tainted_gold`, `sky`, `depth`。
- 標準金属フォームは常時登録: ingot, double_ingot, sheet, double_sheet, rod。
- TFC More Items系フォームは`tfcmu2`/metallum同様に`Optional`扱いで、`tfc_items`がロードされている場合のみ登録: foil, gear, heavy_sheet, nail, ring, rivet, screw, stamen, wire。
- 溶融金属Fluidはmetallumと同様、TFC名前空間の`tfc:metal/<metal>` / `tfc:metal/flowing_<metal>`として登録する。
- 金属レシピはTFCのwelding/anvil/heatingを基本にし、Nature's Aura本体インゴットからTFAuraインゴットへの変換、altar/offering/depth作成系のTFAura版を用意する。
- 金属アイテムテクスチャは`tfaura:item/metal/<form>/<metal>`へ出力し、モデルはTFAura内テクスチャを参照する。
- 金属テクスチャ生成は`.tmp/generate_tfaura_metal_textures.py`で行う。Nature's Aura本体ingotから`shadow`/`mid`/`highlight`/`glint`パレットを抽出し、TFCの`wrought_iron`標準フォームとTFC More Items互換フォームの輝度テンプレートへ補間適用する。
- More Items系フォームのテンプレートは、`tfc_items` jarが依存に入っていない現状では参照repo（TFC-metallum-overhaul）の`compressed_iron`生成済みテクスチャを形状・輝度ベースとして使う。
- ancient wood / brilliant grass系テクスチャ生成は`.tmp/generate_ancient_stripped_textures.py`で行う。TFC oakのstripped block/log系item、TFC lumber、TFC bluegrassを入力に、Nature's Aura ancient planks/log/gold fiber寄りの落ち着いたパレットへ再配色する。
- TFC植物向けaura効果は独自drain spot effectとして`tfaura:tfc_plant_boost`と`tfaura:tfc_plant_decay`を登録する。
- Nature's Auraの基本aura値は`IAuraChunk.DEFAULT_AURA = 1,000,000`。ただしdrain spot effectへ渡る`spotAura`と`IAuraChunk.getAuraAndSpotAmountInArea`の合算値は基本値からの差分として扱われ、`0`が中立/デフォルト、正値が余剰、負値が不足を表す。
- 正のaura効果はNature's Auraの`plant_boost`と同じ差分条件（Overworld aura、周辺差分auraが`DEFAULT_AURA * 1.5`以上）を基準に発動し、TFC通常苗木、TFC果樹苗木、TFC竹苗、TFCのプレイヤー植え作物（`CropBlock`）、TFC grass/dirtにだけ作用する。
- 正のauraではTFC苗木系の内部経過tickを大きく進めてrandom tickを即時試行し、作物は`CropBlockEntity.lastGrowthTick`を大きく巻き戻してTFC成長処理を進める。単体作物は成長が進まない場合に追加で直接成長を補助する。
- 負のaura効果はNature's Auraの`grass_die`と同じ差分条件（drain spotと周辺差分auraが負）を基準に発動し、TFC grassを対応dirtへ戻す。TFC苗木系は経過tickをリセットし、高負荷時は消滅する。TFCのプレイヤー植え作物は成長tickを現在に戻してほぼ成長しないようにし、確率でdead crop化する。
- 現時点でaura効果の対象にするTFC植物は苗木系・プレイヤー植え作物・grass blockのみ。野生作物、berry bush、その他TFC植物には広げない。
