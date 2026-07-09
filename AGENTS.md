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
  - brilliant fiber供給用の弱光源装飾植物: `brilliant_grass`。ブロックIDは`tfaura:plant/brilliant_grass`、ドロップは`naturesaura:gold_fiber`。
  - Nature's Aura由来の古代木: log, wood, planks, stairs, slab, fence, fence gate, leaves, golden leaves, fallen leaves, twig, sapling, potted sapling。
- 植物の設置条件:
  - 通常植物・キノコはTFCの`BUSH_PLANTABLE_ON`。
  - サボテン系はTFCの`DRY_PLANT_PLANTABLE_ON`。
  - brilliant grassはTFCの`GRASS_PLANTABLE_ON`。
  - 苗木はTFCの`TREE_GROWS_ON`。
- テクスチャと既存基本モデルはNature's Auraのアセットを参照し、TFC固有形状（落ち葉・小枝）はTFCのモデルparentを参照する。
- Nature's Auraの`ancient_tree`はconfigured featureのみで、元mod内に自然生成用placed feature/biome modifierが無いため、現時点では自然生成させない。
- `aura_bloom`系植物は専用feature経由の自然生成時にblock entityへ`justGenerated`を立て、元mod同様に150,000 auraを生成する。
- ancient leavesは専用block entityとNature's Aura aura capabilityを持ち、保持auraが尽きた場合は`naturesaura:decayed_leaves`へ変化する。
- golden leavesの侵蝕はTFC葉タグ（`tfc:seasonal_leaves`, `tfc:fruit_tree_leaves`, `minecraft:leaves`）にも対応する。`naturesaura:gold_fiber`をTFC葉へ使った場合は`tfaura:wood/leaves/golden`へ変換する。
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
