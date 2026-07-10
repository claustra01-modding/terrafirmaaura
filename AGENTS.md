# TerraFirmaAura mod

NeoForge 1.21.1環境のTerraFirmaCraftとNature's Aura連携を実装する。

## 運用ルール

- 仕様追加や実装変更があった場合、この `AGENTS.md` を随時更新する。
- 形状の追加・削除、条件付き有効化の変更、ID規則の変更を優先して反映する。
- 依存元Modの取得・展開・一時解析（jar展開や素材抽出など）は、リポジトリ直下の `.tmp` で行う。
- 名前空間は `net.claustra01.tfaura`、mod名は `TerraFirmaAura` とする。
- aura生成・消費コンテンツの一覧、概要、実装済み数値、未対応分の方針案は `docs/aura_content.md` に記録する。
- 未対応分は対応状態を明記し、実装済み内容と混同しないようにする。

## 基本情報

- mod idは `tfaura`、Javaパッケージは `net.claustra01.tfaura`。
- TerraFirmaCraft、Nature's Aura、Patchouliを必須依存にする。
- TFC More Items系（mod id `tfc_items`）は任意依存扱いにし、存在する場合のみ追加フォームを登録する。
- TFAura creative tabのアイコンは ancient wood lumber（`tfaura:wood/lumber/ancient`）を使う。

## ID規則

- TFC対応植物: `tfaura:plant/<name>`
- TFC対応古代木: `tfaura:wood/<type>/ancient`
- TFC対応worldgen植物feature: `tfaura:plant/<name>`
- TFC風金属アイテム: `tfaura:metal/<form>/<metal>`

## 植物・木材

- Nature's Aura由来植物として `aura_bloom`, `aura_cactus`, `aura_mushroom`, `crimson_aura_mushroom`, `warped_aura_mushroom` をTFC設置条件付きで追加する。
- brilliant fiber供給用の弱光源装飾植物として `brilliant_grass` を追加する。ブロックIDは `tfaura:plant/brilliant_grass`、TFCハサミ（`#c:tools/shear`）で破壊した場合のみ `naturesaura:gold_fiber` を2〜4個ドロップする。BlockItem・creative tab項目・独自ドロップアイテムは作らない。
- 植物の設置条件は、通常植物・キノコがTFC `BUSH_PLANTABLE_ON`、サボテン系が `DRY_PLANT_PLANTABLE_ON`、brilliant grassが `GRASS_PLANTABLE_ON`、苗木が `TREE_GROWS_ON`。
- 植物・苗木のアイテムモデルは `minecraft:item/generated` を使い、block/crossモデルをitem parentにしない。
- Nature's Aura由来の古代木として log, stripped log, wood, stripped wood, planks, lumber item, stairs, slab, fence, log fence, fence gate, leaves, sapling, potted sapling を追加する。
- fallen leavesとtwigは追加しない。door, trapdoor, button, pressure plateも追加しない。TFCのblock entity/UI前提の木材（chest, barrel, sluice等）はblock entity type互換性を確認してから追加する。
- ancient log/woodはTFCの `LogBlock` を使い、硬さ8.0、正しい工具要求、斧ストリップ、TFC一括伐採に対応する。sapling/worldgenで生成される幹は `branch_direction=down`、手置きlogはTFC同様 `none` のまま一括伐採対象外にする。
- ancient saplingはTFAura専用 `tfaura:tick_counter` block entityを使う。TFC本体の `tfc:tick_counter` は有効ブロック検証に失敗するため使わない。
- ancient/golden/decayed leavesはTFC葉同様に衝突判定なし。ancient leavesは原木距離更新と原木消失時decayをTFAura共通葉ブロックで行い、葉ブロックドロップはTFC同様ハサミまたはSilk Touch限定にする。
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
- 標準フォームは ingot, double_ingot, sheet, double_sheet, rod。`tfc_items` がロードされている場合のみ foil, gear, heavy_sheet, nail, ring, rivet, screw, stamen, wire を登録する。
- 溶融金属FluidはTFC名前空間の `tfc:metal/<metal>` / `tfc:metal/flowing_<metal>` として登録する。表示はTFC/Metallum同様 `FluidRendererExtension` で `tfc:block/molten_still` / `tfc:block/molten_flow` に金属色tintを乗せる。
- Nature's Aura装備・ツールは元の作業台レシピを同名上書きし、TFC式の中間素材を使う。装備対象金属は `infused_iron`, `sky`, `depth` のみで、`tainted_gold` にはtool head/unfinished armorを追加しない。
- tool head/bladeは `pickaxe_head`, `axe_head`, `shovel_head`, `hoe_head`, `sword_blade`。未完成防具は `unfinished_helmet`, `unfinished_chestplate`, `unfinished_greaves`, `unfinished_boots`。
- ツール完成レシピはTFC `advanced_shaped_crafting` 同様に中間素材 + `#c:rods/wooden` を使い、`tfc:copy_forging_bonus` を適用する。防具完成レシピはTFC `welding` 同様にunfinished armor + sheet/double_sheetを使い、`bonus: copy_best` を適用する。
- 装備・ツール用中間素材と完成済みNature's Aura装備はTFC heatingに対応する。完成品は `use_durability: true` で溶かし戻し量を耐久値に連動させる。

## レシピ・タグ連携

- Natural Altarの構成ブロックはNature's Aura本体の `data/naturesaura/tags/block/altar_*` を拡張して対応する。TFC通常木材は `#minecraft:planks`、TFC岩石レンガは `#minecraft:stone_bricks` 経由で利用でき、TFC chiseled rockは `altar_fancy_brick` へ明示追加する。TFAura ancient planksとArbor系planksタグも `altar_wood` へ追加する。
- Ritual of the ForestはNature's Aura本体の `data/naturesaura/recipe/tree_ritual/*` を同名上書きし、中心苗木を `#tfaura:tree_ritual_saplings` へ差し替える。TFC果樹苗・竹苗・2x2成長前提のsaplingは対象にしない。
- Tree Ritual内のNature's Aura金属素材はTFAura金属へ寄せる。TFCに相当品がない素材は元の `minecraft:*` のまま残し、複数候補が必要なsugarcane/magma rocksだけTFAura item tagで受ける。植物素材は本家の花系要求に寄せ、必要なら `#minecraft:small_flowers` を使う。
- Nature's Aura本体の主要素材生成レシピは同名上書きでTFAura金属へ寄せる。altarのinfused iron/tainted gold、offeringのsky ingot、depth ingot creationはTFAura metal ingotを出力する。
- `gold_powder` はTFC石臼 `type: tfc:quern` へ同名上書きし、`naturesaura:gold_leaf` から `naturesaura:gold_powder` を2個出す。
- `gold_brick` は `naturesaura:gold_fiber` + `#tfaura:tfc_stone_bricks` でクラフトする。このタグは通常TFC rock bricksのみを含み、cracked/mossy/chiseledやvanilla bricks/fire bricksは含めない。
- Offering Table周囲に置く花はNature's Aura本体の判定に合わせて `#minecraft:small_flowers` で互換対応する。TFAura植物に加え、TFCの小型花をTFAura側の同タグにも明示追加する。
- Canopy Diminisher（`naturesaura:oak_generator`）とDisentangler of Mortals（`naturesaura:animal_generator`）はTFC環境では保留扱いにし、`[Disabled]` tooltipを追加して同名レシピを `neoforge:false` 条件で削除する。

## Aura連携

- aura生成・消費コンテンツの一覧と概要は `docs/aura_content.md` を参照する。
- TFC/Arbor系植物向けaura効果は独自drain spot effectとして `tfaura:tfc_plant_boost` と `tfaura:tfc_plant_decay` を登録する。
- ancient leavesは専用block entityとNature's Aura aura capabilityを持つ。内部containerは本家同様 `NaturalAuraContainer(TYPE_OVERWORLD, 2000, 500)`、aura color `13522057`、drain時client syncを持ち、保持auraが尽きた場合は `tfaura:wood/leaves/decayed` へ変化する。
- Herbivorous AbsorberはNature's Aura本体同様 `#minecraft:small_flowers` を参照するため、Offering Table対応で追加したTFC小型花tag付与でTFC花も消費対象になる。
- Swamp HomiはNature's Aura本体の `BOTANIST_PICKAXE_CONVERSIONS` へTFC rockのcobble/bricksと、それらのslab/stair/wallのclean -> mossy変換を追加する。
- Shooting MarkはTFC投射物として `TFCEntities.THROWN_JAVELIN` を30,000 aura、`TFCEntities.GLOW_ARROW` を45,000 auraで追加する。TFC loose rockは1.21.1-4.2.5ではgroundcoverの設置/回収ブロックで、投射物Entityではないため対象外。

## 生成スクリプト

- ancient wood / brilliant grass系テクスチャ生成は `.tmp/generate_ancient_stripped_textures.py` で行う。TFC oakのstripped block/log系item、TFC lumber、TFC bluegrassを入力にする。
- 金属テクスチャ生成は `.tmp/generate_tfaura_metal_textures.py` で行う。Nature's Aura本体ingotからパレットを抽出し、TFCの `wrought_iron` 標準フォームとTFC More Items互換フォームの輝度テンプレートへ補間適用する。
- 金属登録・基本レシピ生成は `.tmp/generate_tfaura_metals.py`、装備・ツール用中間素材/レシピ/テクスチャ生成は `.tmp/generate_tfaura_equipment_recipes.py` を使う。
- More Items系フォームのテンプレートは、`tfc_items` jarが依存に入っていない現状では参照repo（TFC-metallum-overhaul）の `compressed_iron` 生成済みテクスチャを形状・輝度ベースとして使う。
