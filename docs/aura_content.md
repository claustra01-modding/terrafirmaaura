# Aura Content Notes

このファイルは、TFAuraで実装済みまたは方針確定済みのaura生成・消費コンテンツを記録する。検討段階でまだ実装していないaura消費対応は、ここには書かず会話側で扱う。

## Aura生成

| コンテンツ | 対象ID | TFAuraでの扱い | 概要 |
| --- | --- | --- | --- |
| Aura Bloom系自然植物 | `tfaura:plant/aura_bloom`, `aura_cactus`, `aura_mushroom`, `crimson_aura_mushroom`, `warped_aura_mushroom` | 対応済み | TFC設置条件と気候条件に合わせた植物へ置換。自然生成時にblock entityへ `justGenerated` を立て、本家同様150,000 auraを生成する。crimson/warpedはNetherのみ生成する。 |
| Herbivorous Absorber | `naturesaura:flower_generator` | タグ互換対応済み | 本家同様 `#minecraft:small_flowers` を参照するため、TFC小型花を同タグへ追加してTFC花も生成素材にする。 |
| Swamp Homi | `naturesaura:moss_generator` | TFC rock対応済み | 本家の `BOTANIST_PICKAXE_CONVERSIONS` へTFC rockのcobble/bricksとslab/stair/wallのclean -> mossy変換を追加する。逆変換でmossy側を消費して本家同様5,000 auraを生成できる。 |
| Shooting Mark | `naturesaura:projectile_generator` | TFC投射物対応済み | TFC javelinを30,000 aura、TFC glow arrowを45,000 auraとして追加する。TFC loose rockは投射物Entityではないため対象外。 |
| Ancient Leaves | `tfaura:wood/leaves/ancient` | TFAura独自ブロックで対応済み | 本家ancient leaves相当のaura containerを持つ葉。保持auraが尽きると `tfaura:wood/leaves/decayed` へ変化する。TFC葉同様に通り抜け可能で、原木距離decayとTFC風lootにも対応する。 |
| Firecracker Gaze | `naturesaura:firework_generator` | 現状維持 | 本家仕様のまま。TFC固有素材・投射物対応はまだ入れていないが、追加方針は未確定。 |
| Lingering Absorber | `naturesaura:potion_generator` | 現状維持 | 本家仕様のまま。 |
| Reaper of Ender Heights | `naturesaura:chorus_generator` | 現状維持 | 本家仕様のまま。 |
| Offshoot Observer | `naturesaura:slime_split_generator` | 現状維持 | 本家仕様のまま。 |
| Creational Catalyst | `naturesaura:generator_limit_remover` | 現状維持 | 本家仕様のまま。高aura生成の上限突破用途として扱う。 |
| Canopy Diminisher | `naturesaura:oak_generator` | 無効化済み | TFC環境では保留扱い。`[Disabled]` tooltipを追加し、同名レシピを `neoforge:false` 条件で削除する。 |
| Disentangler of Mortals | `naturesaura:animal_generator` | 無効化済み | TFC環境では保留扱い。`[Disabled]` tooltipを追加し、同名レシピを `neoforge:false` 条件で削除する。 |

## Aura消費・保持

| コンテンツ | 対象ID | TFAuraでの扱い | 概要 |
| --- | --- | --- | --- |
| Natural Altar基礎素材 | `naturesaura:altar/*` | 主要素材対応済み | infused iron / tainted gold / infused stone系をTFC素材・TFAura金属へ寄せる。Altar構成ブロックはTFC木材・岩石レンガ・TFAura ancient planks・Arbor系planksも使えるようにタグ拡張する。 |
| Offering to the Gods | `naturesaura:offering/*` | sky ingot系対応済み | sky ingot作成をTFAura金属へ寄せる。周囲に置く花は本家判定に合わせて `#minecraft:small_flowers` で互換対応する。 |
| Depth Ingot Creation | `naturesaura:depth_ingot_creation` | 対応済み | depth ingot作成をTFAura金属へ寄せる。 |
| TFC植物boost | `tfaura:tfc_plant_boost` | 独自実装済み | 正のaura過多でTFC/TFAura/Beneath/Arbor系植物の成長を補助し、成功対象ごとにauraをdrainする。 |
| TFC植物decay | `tfaura:tfc_plant_decay` | 独自実装済み | 負のaura不足でTFC/TFAura/Beneath/Arbor系植物を段階的に弱らせる。葉は直接破壊せず `tfaura:wood/leaves/decayed` へ変換する。 |
| Ancient Leaves保持aura | `tfaura:wood/leaves/ancient` | 独自実装済み | `NaturalAuraContainer(TYPE_OVERWORLD, 2000, 500)` を持つ。保持auraが尽きるとdecayed leavesへ変化する。 |

## TFC植物aura効果の数値

- Nature's Auraの基本aura値は `IAuraChunk.DEFAULT_AURA = 1,000,000`。
- drain spot effectへ渡る `spotAura` と `IAuraChunk.getAuraAndSpotAmountInArea` の合算値は、基本値からの差分として扱う。`0` が中立、正値が余剰、負値が不足。
- 段階判定は差分絶対値で行う。tier 1は25%以上（250,000〜599,999）、tier 2は60%以上（600,000〜999,999）、tier 3は100%以上（1,000,000以上）。
- 強度 `intensity` は `abs(delta) / 1,000,000` を0.25〜2.0にclampする。

### 対象

- 対象namespaceはTFC/TFAura/Beneath/ArborFirmaCraft/Arbor_FirmaCraft/AFC。
- `#tfaura:aura_nature_effects`, `#minecraft:leaves`, `#minecraft:saplings`, `#minecraft:flowers`, `#minecraft:small_flowers`, TFCの `natural_regrowing_plants` / `spreading_bushes` / `thorny_bushes` / `fruit_tree_saplings` / `bamboo_sapling` / `grass` / 葉タグを対象にする。
- TFAura golden leavesとTFAura decayed leaves自身は対象外。

### 正のaura効果

- 発動条件はspotAuraが正、半径30の周辺差分auraが250,000以上。
- 探索回数係数は `ceil(abs(delta) / 80,000 / spotCount)`、最大160。
- 探索距離は `abs(delta) / 100,000` を4〜45にclampする。
- random tick試行はtier 1で1回、tier 2で3回、tier 3で `6 + floor(intensity * 2)` 回。TFC通常苗木は追加でtier分を足し、1〜12回にclampする。
- TFC通常苗木はtick counterを `24,000 * intensity` 進める。範囲は6,000〜240,000。
- TFC果樹苗木も同量のTickingPlant tickを加える。
- TFC竹苗と汎用植物は段階別random tickを行う。
- TFC作物は `lastGrowthTick` を `18,000 * intensity` 巻き戻す。範囲は6,000〜336,000。進まない場合は成長量を `0.04 * intensity` だけ直接補助する。範囲は0.02〜0.35。
- TFC grassは段階別random tick +1回。dirtからgrass化はtier 2以上かつ近くにgrassがある場合のみ。
- aura消費量は成功対象ごとにtier 1で2,500、tier 2で4,000、tier 3で5,500。

### 負のaura効果

- 発動条件はspotAuraが負、半径50の周辺差分aura絶対値が250,000以上。
- 探索回数係数は `ceil(abs(delta) / 90,000 / spotCount)`、最大400。
- 探索距離は `abs(delta) / 70,000` を4〜85にclampする。
- tier 1は主に成長遅延。作物growthを0.015減らし、苗木系tick counterをresetする。
- tier 2は作物growthを `0.05 * intensity` 減らす。範囲は0.03〜0.12。dead crop化3〜12%、grass die 3%、苗木消滅2%、汎用非葉植物消滅2%。
- tier 3は作物growthを `0.12 * intensity` 減らす。範囲は0.08〜0.35。dead crop化12〜75%、grass die 25〜85%、苗木消滅8〜65%、汎用非葉植物消滅8〜55%。
- 葉はNature's Aura本体のGrassDieEffectに合わせ、直接破壊せず `tfaura:wood/leaves/decayed` へ変換し、その後decayed leaves自身のrandom tickで空気へ消える。
