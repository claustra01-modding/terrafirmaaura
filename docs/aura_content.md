# Aura Content Notes

このファイルは、TFAuraで確認したNature's Aura本体のaura生成・消費・保持コンテンツと、TFC対応状態、未対応分の方針案を記録する。

状態の意味:

- 対応済み: TFAura側でコードまたはdata pack上書きまで実装済み。
- 一部対応済み: TFC素材・タグ・レシピの一部は対応済みだが、全レシピやTFC挙動までは未整理。
- 未対応: TFC向けの実装はまだない。方針案を残す。
- 現状維持: 本家仕様をそのまま使う方針。
- 無効化済み: TFC環境では使わせない方針で、tooltipやレシピ削除を入れている。

各コンテンツは、Nature's Aura本家での役割を「コンテンツ概要」に、TFAura側の現状を「対応状態」に、今後のTFC向け調整を「TFC方針」に分けて記録する。

## Aura生成

- **Aura Bloom系自然植物**
  - ID: `tfaura:plant/aura_bloom`, `tfaura:plant/aura_cactus`, `tfaura:plant/aura_mushroom`, `tfaura:plant/crimson_aura_mushroom`, `tfaura:plant/warped_aura_mushroom`
  - コンテンツ概要: 自然生成されたAura Bloom系植物が初回tickで周辺へ150,000 auraを生成する。
  - 対応状態: 対応済み。
  - TFC方針: TFC設置条件と気候条件に合わせた独自植物へ置換する。自然生成時はblock entityへ `justGenerated` を立てる。crimson/warpedはNetherのみ生成する。

- **Rose of Oblivion**
  - ID: `naturesaura:end_flower`
  - コンテンツ概要: Ender Dragon討伐後にEnd島へ生える花。Ender Eyeを投入すると内部500,000 auraを5,000ずつ環境へ放出し、最後に枯れる。
  - 対応状態: 現状維持。
  - TFC方針: TFC素材・worldgenとの直接競合が薄いため、本家仕様のまま扱う。

- **Herbivorous Absorber**
  - ID: `naturesaura:flower_generator`
  - コンテンツ概要: 周囲の小型花を消費してauraを生成する。
  - 対応状態: 対応済み。
  - TFC方針: 本家同様 `#minecraft:small_flowers` を参照するため、TFC小型花を同タグへ追加してTFC花も対象にする。

- **Swamp Homi**
  - ID: `naturesaura:moss_generator`
  - コンテンツ概要: 苔化できる石材を変換し、その逆変換でauraを生成する。
  - 対応状態: 対応済み。
  - TFC方針: `BOTANIST_PICKAXE_CONVERSIONS` へTFC rockのcobble/bricksとslab/stair/wallのclean to mossy変換を追加する。逆変換では本家同様5,000 auraを生成できる。

- **Shooting Mark**
  - ID: `naturesaura:projectile_generator`
  - コンテンツ概要: 矢などの投射物を受けてauraを生成する。
  - 対応状態: 対応済み。
  - TFC方針: TFC javelinを30,000 aura、TFC glow arrowを45,000 auraとして追加する。TFC loose rockは投射物Entityではないため対象外。

- **Ancient Leaves**
  - ID: `tfaura:wood/leaves/ancient`
  - コンテンツ概要: ancient treeの葉が自然aura containerとして振る舞う。
  - 対応状態: 対応済み。
  - TFC方針: TFAura独自ブロックで本家ancient leaves相当を実装する。保持auraが尽きると `tfaura:wood/leaves/decayed` へ変化する。TFC葉同様に通り抜け可能で、原木距離decayとTFC風lootにも対応する。

- **Firecracker Gaze**
  - ID: `naturesaura:firework_generator`
  - コンテンツ概要: 花火の発射・爆発を利用してauraを生成する。
  - 対応状態: 現状維持。
  - TFC方針: 本家仕様のまま。TFC固有素材・投射物対応はまだ方針未確定。

- **Lingering Absorber**
  - ID: `naturesaura:potion_generator`
  - コンテンツ概要: lingering potion系の効果を消費してauraを生成する。
  - 対応状態: 現状維持。
  - TFC方針: 本家仕様のまま。

- **Reaper of Ender Heights**
  - ID: `naturesaura:chorus_generator`
  - コンテンツ概要: chorus plantの成長・破壊サイクルを利用してauraを生成する。
  - 対応状態: 現状維持。
  - TFC方針: 本家仕様のまま。

- **Offshoot Observer**
  - ID: `naturesaura:slime_split_generator`
  - コンテンツ概要: slime分裂イベントを利用してauraを生成する。
  - 対応状態: 現状維持。
  - TFC方針: 本家仕様のまま。

- **Creational Catalyst**
  - ID: `naturesaura:generator_limit_remover`
  - コンテンツ概要: aura生成デバイスが通常上限を超えて生成できるようにする補助デバイス。
  - 対応状態: 現状維持。
  - TFC方針: 高aura生成の上限突破用途として本家仕様のまま扱う。

- **Canopy Diminisher**
  - ID: `naturesaura:oak_generator`
  - コンテンツ概要: 樹木の葉や成長を利用してauraを生成する。
  - 対応状態: 無効化済み。
  - TFC方針: TFC環境では保留扱い。`[Disabled]` tooltipを追加し、同名レシピを `neoforge:false` 条件で削除する。

- **Disentangler of Mortals**
  - ID: `naturesaura:animal_generator`
  - コンテンツ概要: 動物に関わる処理を利用してauraを生成する。
  - 対応状態: 無効化済み。
  - TFC方針: TFC環境では保留扱い。`[Disabled]` tooltipを追加し、同名レシピを `neoforge:false` 条件で削除する。

## Aura消費デバイス

- **Natural Altar**
  - ID: `naturesaura:nature_altar`, `naturesaura:altar/*`
  - コンテンツ概要: 周囲multiblockからauraを集め、投入アイテムを変換・作成する基本クラフト設備。Aura container itemの充填にも使われる。
  - 対応状態: 一部対応済み。
  - TFC方針: infused iron / tainted gold / infused stone系、構成ブロック、中心苗木はTFC/TFAura素材へ寄せている。未整理のconversion/crushing系altar recipeは、TFCに1:1相当がある素材だけ直接置換し、TFC加工体系に強い相当品があるものはquern/heating/anvilなどへ移す。

- **Transmutation Catalyst**
  - ID: `naturesaura:conversion_catalyst`
  - コンテンツ概要: Natural Altarの変換レシピを拡張する触媒。
  - 対応状態: 一部対応済み。
  - TFC方針: Tree RitualレシピはTFC苗木・素材へ置換済み。個別altar conversionは、TFCで入手不能または意味が変わる素材だけ差し替え、TFC加工経路があるものは本家変換を削除してTFC加工へ寄せる。

- **Crumbling Catalyst**
  - ID: `naturesaura:crushing_catalyst`
  - コンテンツ概要: Natural Altarの粉砕・分解系レシピを拡張する触媒。
  - 対応状態: 一部対応済み。
  - TFC方針: Tree RitualレシピはTFC苗木・素材へ置換済み。gold powderはTFC quernへ移行済み。残りはTFC石臼・粉砕・砂利/砂/粉系に相当するものを優先して置換する。

- **Offering to the Gods**
  - ID: `naturesaura:offering_table`, `naturesaura:offering/*`
  - コンテンツ概要: Offering Tableと周囲の花を使い、空へ捧げる形でsky ingotやtoken類を作る。
  - 対応状態: 一部対応済み。
  - TFC方針: sky ingot作成をTFAura金属へ寄せ、周囲に置く花は `#minecraft:small_flowers` で互換対応する。残りoffering recipeは明確なTFC相当素材へ差し替え、相当品がないものだけ本家素材を残す。

- **Depth Ingot Creation**
  - ID: `naturesaura:depth_ingot_creation`
  - コンテンツ概要: depth ingotを作成する特殊レシピ。
  - 対応状態: 対応済み。
  - TFC方針: 出力をTFAura金属のdepth ingotへ寄せる。

- **Altar of Birthing**
  - ID: `naturesaura:animal_spawner`
  - コンテンツ概要: Spirit of Birthingとmobに対応する素材を消費し、auraを使ってEntityをスポーンする。
  - 対応状態: 無効化済み。
  - TFC方針: TFC家畜・野生動物の年齢・性別・飼育価値と衝突しやすいため保留する。`[Disabled]` tooltipを追加し、本体レシピを `neoforge:false` 条件で削除する。

- **Extraneous Firestarter**
  - ID: `naturesaura:furnace_heater`
  - コンテンツ概要: 隣接furnace系設備をaura消費で加熱・稼働補助する。
  - 対応状態: 無効化済み。
  - TFC方針: TFC firepit/forge/charcoal forge/bloomery/blast furnaceの熱体系とvanilla furnace補助が噛み合わないため保留する。`[Disabled]` tooltipを追加し、Tree Ritualレシピを `neoforge:false` 条件で削除する。

- **Armorer's Aid**
  - ID: `naturesaura:blast_furnace_booster`
  - コンテンツ概要: vanilla blast furnaceをaura消費で高速化する。
  - 対応状態: 無効化済み。
  - TFC方針: TFC blast furnaceは金属精錬・温度体系が別物なので保留する。`[Disabled]` tooltipを追加し、本体レシピを `neoforge:false` 条件で削除する。

- **World Eye**
  - ID: `naturesaura:chunk_loader`
  - コンテンツ概要: redstone信号に応じた範囲のchunkを、継続aura消費で読み込む。
  - 対応状態: 現状維持。
  - TFC方針: 機能面は本家仕様のまま。サーバー負荷が大きいため、configと本家コストを尊重する。

- **Ender Crate**
  - ID: `naturesaura:ender_crate`
  - コンテンツ概要: 離れたEnder Crate間で同一ストレージを共有し、操作時にauraを消費する。
  - 対応状態: 現状維持。
  - TFC方針: 機能面は本家仕様のまま。TFC容器体系との追加調整は現時点では行わない。

- **Rails of the Worlds**
  - ID: `naturesaura:dimension_rail_overworld`, `naturesaura:dimension_rail_nether`, `naturesaura:dimension_rail_end`
  - コンテンツ概要: minecartを次元間転送し、その際にauraを消費する。
  - 対応状態: 現状維持。
  - TFC方針: 機能面は本家仕様のまま。BeneathやTFC Netherとの座標倍率・到着安全性の追加調整は現時点では行わない。

- **Aura Field Creator**
  - ID: `naturesaura:field_creator`
  - コンテンツ概要: 2点間にブロック破壊フィールドを張り、起動・維持・破壊でauraを消費する。
  - 対応状態: 現状維持。
  - TFC方針: 機能面は本家仕様のまま。TFC rock/oreや崩落制約への追加制限は現時点では行わない。

- **Imperceptible Builder**
  - ID: `naturesaura:placer`
  - コンテンツ概要: 内部inventoryのブロックをaura消費で自動設置する。
  - 対応状態: 現状維持。
  - TFC方針: 機能面は本家仕様のまま。TFC block entityや崩落制約のdeny list化は現時点では行わない。

- **Energetic Aura Forge**
  - ID: `naturesaura:rf_converter`
  - コンテンツ概要: auraをForge Energyへ変換する。
  - 対応状態: 無効化済み。
  - TFC方針: TFC単体ではFE前提が薄いため保留する。`[Disabled]` tooltipを追加し、本体レシピを `neoforge:false` 条件で削除する。

- **Winter's Calling**
  - ID: `naturesaura:snow_creator`
  - コンテンツ概要: auraを消費して周囲に雪や氷を作る。
  - 対応状態: 無効化済み。
  - TFC方針: TFCの気候・季節・淡水/塩水と矛盾しやすいため保留する。`[Disabled]` tooltipを追加し、本体レシピを `neoforge:false` 条件で削除する。

- **Everlasting Spring**
  - ID: `naturesaura:spring`
  - コンテンツ概要: auraを消費して水を供給し、cauldron充填、farmland灌漑、lava変換などを行う。
  - 対応状態: 対応済み。
  - TFC方針: 内部tankをmixinで `tfc:river_water` 供給へ差し替え、TFC流体容器からは淡水として扱えるようにする。BucketPickupもTFC淡水入り木製バケツへ差し替える。受け入れ判定は `#tfc:any_fresh_water` 相当。aura消費量、farmland ticket、lava変換、cauldron/sponge挙動は本家仕様を維持する。レシピ石材は `#tfaura:tfc_stone_bricks`、水素材は `tfc:fluid_content` の `#tfc:any_fresh_water` へ置換する。

- **Shifting Sundial**
  - ID: `naturesaura:time_changer`
  - コンテンツ概要: auraを消費して時刻を変更する。
  - 対応状態: 現状維持。
  - TFC方針: 機能面は本家仕様のまま。TFC calendar、季節、作物成長、家畜年齢への追加調整は現時点では行わない。

- **Cloudshifter**
  - ID: `naturesaura:weather_changer`
  - コンテンツ概要: auraを消費して天候を変更する。
  - 対応状態: 未対応（再検討）。
  - TFC方針: TFCは `WorldTracker` とclimate modelで雨・雷を駆動し、vanilla `/weather clear/rain` も無効化しているため、vanilla天候仕様と同等ではない。現時点では機能変更せず、TFC climateへどう介入するかを再検討する。

- **Hopper Enhancement**
  - ID: `naturesaura:hopper_upgrade`
  - コンテンツ概要: hopper周辺のitem移送を拡張し、処理時にauraを消費する。
  - 対応状態: 現状維持。
  - TFC方針: 機能面は本家仕様のまま。TFCの重量/サイズ制約への追加制限は現時点では行わない。

- **Lamp of Sanctuary**
  - ID: `naturesaura:spawn_lamp`
  - コンテンツ概要: redstone入力時に範囲内のmob spawnを止め、そのたびに少量のauraを消費する。
  - 対応状態: 現状維持。
  - TFC方針: 機能面は本家仕様のまま。TFC野生動物スポーンへの追加調整は現時点では行わない。

- **Redstone Aura Vaporizer**
  - ID: `naturesaura:aura_timer`
  - コンテンツ概要: Bottled Auraを時間として蒸発させ、完了時にredstone pulseを出す。
  - 対応状態: 現状維持。
  - TFC方針: 機能面は本家仕様のまま。bottle作成・aura type入手経路の追加調整は現時点では行わない。

- **Aura Attraction Cart**
  - ID: `naturesaura:mover_cart`
  - コンテンツ概要: activator railで周辺auraを掴み、minecart移動に合わせてaura spotを移送する。
  - 対応状態: 現状維持。
  - TFC方針: 機能面は本家仕様のまま。長距離aura輸送への追加損失は現時点では行わない。

- **Aura Imbalance Ward**
  - ID: `naturesaura:lower_limiter`
  - コンテンツ概要: 低aura時に隣接デバイスを停止させ、負のaura影響を避ける保護装置。自身はauraを消費しない。
  - 対応状態: 現状維持。
  - TFC方針: 機能面は本家仕様のまま。TFCの負のaura効果拡張との追加連携は現時点では行わない。

## Aura増減エフェクト

- **Vegetational Increase**
  - ID: `naturesaura:plant_boost`
  - コンテンツ概要: 正のaura過多とEffect Powderにより、周囲の植物成長を促進する本家効果。
  - 対応状態: 一部対応済み。
  - TFC方針: 本家効果はvanilla植物向けとして維持し、TFC/TFAura/Beneath/Arbor系植物には `tfaura:tfc_plant_boost` を追加登録して補助する。

- **Natural Decay**
  - ID: `naturesaura:grass_die`
  - コンテンツ概要: 負のaura不足により、草・葉・植物を劣化または消滅させる本家効果。
  - 対応状態: 一部対応済み。
  - TFC方針: 本家効果は維持し、TFC/TFAura/Beneath/Arbor系植物には `tfaura:tfc_plant_decay` を追加登録する。葉は直接消さずdecayed leavesへ変換してからランダムtickで消える。

- **TFC植物boost**
  - ID: `tfaura:tfc_plant_boost`
  - コンテンツ概要: 正のaura過多がTFC系自然ブロックへ与える成長促進効果。
  - 対応状態: 独自実装済み。
  - TFC方針: TFC/TFAura/Beneath/Arbor系植物の成長を段階的に補助し、成功対象ごとにauraをdrainする。

- **TFC植物decay**
  - ID: `tfaura:tfc_plant_decay`
  - コンテンツ概要: 負のaura不足がTFC系自然ブロックへ与える枯死・劣化効果。
  - 対応状態: 独自実装済み。
  - TFC方針: TFC/TFAura/Beneath/Arbor系植物を段階的に弱らせる。葉は `tfaura:wood/leaves/decayed` へ変換する。

- **Increase of Fertility**
  - ID: `naturesaura:animal`
  - コンテンツ概要: 正のaura過多とEffect Powderにより、周辺動物の繁殖や卵孵化を促す本家効果。
  - 対応状態: 現状維持。
  - TFC方針: TFC家畜の年齢・性別・妊娠・親密度と干渉しうるため、現時点ではTFC動物へ拡張しない。

- **Natural Storage**
  - ID: `naturesaura:cache_recharge`
  - コンテンツ概要: 正のaura過多とEffect Powderにより、プレイヤー所持のAura Cache / Aura Troveを自然充填する本家効果。
  - 対応状態: 現状維持。
  - TFC方針: Aura Cache / Aura Trove素材レシピのTFC化方針に合わせる。効果自体は本家仕様を維持する。

- **Mineral Amassing**
  - ID: `naturesaura:ore_spawn`
  - コンテンツ概要: 強い正のaura過多とEffect Powderにより、石やnetherrack中へ鉱石を生成する本家効果。
  - 対応状態: 現状維持。
  - TFC方針: TFC鉱床・岩種・鉱石品質のバランスと大きく衝突するため、TFC鉱石へは拡張しない。

- **Crimson Overgrowth**
  - ID: `naturesaura:nether_grass`
  - コンテンツ概要: Netherの正のaura過多とEffect Powderにより、netherrackを本家Nether grass系へ変化させる効果。
  - 対応状態: 現状維持。
  - TFC方針: Beneath/Nether植生への拡張は未実装。本家Nether用途として維持する。

- **Crimson Decay**
  - ID: `naturesaura:nether_decay`
  - コンテンツ概要: Netherの負のaura不足により、周囲のNether地形をsoul soil/soul sand等へ劣化させ、火も発生させる本家効果。
  - 対応状態: 現状維持。
  - TFC方針: BeneathやTFC Nether地形への拡張は未実装。本家仕様を維持する。

- **Inexplicable Anger**
  - ID: `naturesaura:anger`
  - コンテンツ概要: 負のaura不足により、通常は中立のmobを怒らせる本家効果。
  - 対応状態: 現状維持。
  - TFC方針: TFC動物AI・野生動物の攻撃性へは現時点では拡張しない。

- **Breathlessness**
  - ID: `naturesaura:breathless`
  - コンテンツ概要: 負のaura不足により、酸素を使う生物へ窒息・弱体寄りの悪影響を与える本家効果。
  - 対応状態: 現状維持。
  - TFC方針: TFC体温・健康・水分システムとは直接連携しない。

- **Unstable Outbreak**
  - ID: `naturesaura:explosions`
  - コンテンツ概要: 極端な負のaura不足で、周囲に爆発を起こす本家効果。
  - 対応状態: 現状維持。
  - TFC方針: TFC地盤崩落や鉱床との追加連携は行わず、本家仕様を維持する。

- **内部aura調整**
  - ID: `naturesaura:replenishing`, `naturesaura:spread`, `naturesaura:balance`
  - コンテンツ概要: aura containerからspotへauraを戻す処理、spot間の拡散・均衡化など、本家内部のaura調整効果。
  - 対応状態: 現状維持。
  - TFC方針: TFC自然ブロック対応とは独立した基盤処理として本家仕様を維持する。

## Aura保持・移送

- **Bottled Aura**
  - ID: `naturesaura:aura_bottle`, `naturesaura:vacuum_bottle`, `naturesaura:bottle_two_the_rebottling`
  - コンテンツ概要: 周辺auraをボトル化し、dimension別aura type付き素材としてtokenやtimerに使う。
  - 対応状態: 未対応。
  - TFC方針: glass bottleをTFC glass bottle/poured glass系へ寄せられるか確認し、type付きbottle要求は本家のaura typeを維持する。

- **Aura Cache / Aura Trove**
  - ID: `naturesaura:aura_cache`, `naturesaura:aura_trove`
  - コンテンツ概要: auraを蓄える携帯container。自然回復効果やプレイヤー消費アイテムの供給源になる。
  - 対応状態: 未対応。
  - TFC方針: 素材をTFC金属・革・容器へ寄せ、容量や充填速度は本家維持を基本にする。

- **Ancient Leaves保持aura**
  - ID: `tfaura:wood/leaves/ancient`
  - コンテンツ概要: ancient leavesが自然aura containerとして少量のauraを保持する。
  - 対応状態: 独自実装済み。
  - TFC方針: `NaturalAuraContainer(TYPE_OVERWORLD, 2000, 500)` を持つ。保持auraが尽きるとdecayed leavesへ変化する。

## Aura関連機械

- **Aura Detector**
  - ID: `naturesaura:aura_detector`
  - コンテンツ概要: 周辺aura量をredstone信号として出力する。
  - 対応状態: 未対応。
  - TFC方針: auraは消費しないため、レシピ素材をTFC金属・石材へ寄せるだけでよい。

- **Corporeal Eye**
  - ID: `naturesaura:animal_container`
  - コンテンツ概要: Entityを保持・移送するための機械。
  - 対応状態: 未対応。
  - TFC方針: TFC家畜の性別・妊娠・年齢・親密度NBTを壊さず保持できるか確認する。

- **Automatic Constructor**
  - ID: `naturesaura:auto_crafter`
  - コンテンツ概要: itemを自動クラフトする機械。
  - 対応状態: 未対応。
  - TFC方針: TFCのadvanced craftingやtool damage/food decayが絡むレシピを実行対象にするか制限する。

- **Adept Hopper**
  - ID: `naturesaura:grated_chute`
  - コンテンツ概要: item移送を補助するhopper系ブロック。
  - 対応状態: 未対応。
  - TFC方針: TFC item size/weightを無視しすぎる場合のみ制限を検討する。

- **Item Distributor**
  - ID: `naturesaura:item_distributor`
  - コンテンツ概要: itemを周囲へ分配するブロック。
  - 対応状態: 未対応。
  - TFC方針: auraを直接消費しないため、レシピ素材の置換を優先する。

- **Item Grounder**
  - ID: `naturesaura:pickup_stopper`
  - コンテンツ概要: 周囲のitem pickupを抑止するブロック。
  - 対応状態: 未対応。
  - TFC方針: auraを直接消費しないため、石材・金属素材レシピだけ置換する方針。

- **Powder Manipulator**
  - ID: `naturesaura:powder_placer`
  - コンテンツ概要: Effect Powderを設置・利用する補助機械。
  - 対応状態: 未対応。
  - TFC方針: 粉自体のTFC対応方針に合わせ、設置対象や自動化悪用だけ確認する。

## Aura消費アイテム・効果

- **Environmental Eye / Ocular**
  - ID: `naturesaura:eye`, `naturesaura:eye_improved`
  - コンテンツ概要: aura量や関連情報を可視化する基本アイテム。
  - 対応状態: 一部対応済み。
  - TFC方針: Tree RitualレシピはTFC苗木・素材へ置換済み。機能は本家維持。

- **Bucket of Infinite Color**
  - ID: `naturesaura:color_changer`
  - コンテンツ概要: プレイヤー側aura containerからauraを消費してブロック等の色を変更する。
  - 対応状態: 未対応。
  - TFC方針: 染料・bucket素材をTFC相当に寄せ、染料入手経路の穴を埋める。

- **Amulet of Wrath**
  - ID: `naturesaura:shockwave_creator`
  - コンテンツ概要: プレイヤー側aura containerを消費して衝撃波を出す。
  - 対応状態: 未対応。
  - TFC方針: 地盤崩落・支柱・硬い岩石への影響を確認し、採掘用途に悪用できる場合はTFC rock/oreを対象外にする。

- **Ender Ocular**
  - ID: `naturesaura:ender_access`
  - コンテンツ概要: プレイヤー側aura containerを消費してEnder Crateへ遠隔アクセスする。
  - 対応状態: 未対応。
  - TFC方針: Ender Crateと同じく、容器バランスを崩さない素材コストにする。

- **Staff of Shadows**
  - ID: `naturesaura:cave_finder`
  - コンテンツ概要: auraを消費して洞窟方向を粒子で示す。
  - 対応状態: 未対応。
  - TFC方針: TFC洞窟・鉱床探索を強くしすぎないよう範囲/コストは本家以上を検討する。

- **Staff of Riches**
  - ID: `naturesaura:loot_finder`
  - コンテンツ概要: auraを消費して周辺loot containerを探す。
  - 対応状態: 未対応。
  - TFC方針: TFC構造物・vessel・chestの戦利品探索が強すぎる場合は対象block entityを絞る。

- **Staff of Ancient Knowledge**
  - ID: `naturesaura:netherite_finder`
  - コンテンツ概要: auraを消費してancient debris系を探す。
  - 対応状態: 未対応。
  - TFC方針: 本家Nether用途なので、TFC/Beneath鉱石へは安易に広げない。

- **Staff of Baldur**
  - ID: `naturesaura:light_staff`
  - コンテンツ概要: auraを消費して光源projectileやlight blockを扱う。
  - 対応状態: 未対応。
  - TFC方針: 本家機能維持を基本に、TFCの光源・火・熱システムとは混同しない。

- **Structure Finder Eyes**
  - ID: `naturesaura:fortress_finder`, `naturesaura:end_city_finder`, `naturesaura:outpost_finder`
  - コンテンツ概要: 使用時にitemを消費して対象構造物の方向を示す。
  - 対応状態: 未対応。
  - TFC方針: 直接aura消費は見当たらないが、Aura Bottleを素材にする。Beneath/Nether/End構造物との整合だけ確認する。

- **Eir's Token**
  - ID: `naturesaura:break_prevention`
  - コンテンツ概要: toolや装備の破壊を防ぐ補助アイテム。
  - 対応状態: 一部対応済み。
  - TFC方針: Tree RitualレシピはTFC苗木・素材へ置換済み。機能は本家維持を基本にする。

- **Spirit of Birthing**
  - ID: `naturesaura:birth_spirit`
  - コンテンツ概要: 本家では繁殖成功時、周辺に余剰auraがあるとauraを消費して生成され、Altar of Birthing素材になる。
  - 対応状態: 未対応。
  - TFC方針: TFC家畜の繁殖年齢・妊娠・性別判定に合わせ、野生動物の自然繁殖からは出さない方針。

- **Token of Undying Friendship**
  - ID: `naturesaura:pet_reviver`
  - コンテンツ概要: pet復活時に周辺auraを消費する。
  - 対応状態: 未対応。
  - TFC方針: TFC家畜・野生動物の所有/年齢データと噛み合うか確認し、TFC家畜を対象にするなら復活時の年齢・妊娠・遺伝情報を保持する。

- **Ring of Last Chance**
  - ID: `naturesaura:death_ring`
  - コンテンツ概要: 死亡回避系trinket。
  - 対応状態: 未対応。
  - TFC方針: 本家機能維持を基本に、TFC health/food/thirstと二重回復しすぎないかだけ確認する。

- **Crimson Meal**
  - ID: `naturesaura:crimson_meal`
  - コンテンツ概要: Nether植物成長系アイテム。
  - 対応状態: 未対応。
  - TFC方針: 本家Nether/Beneath用途を基本にし、TFC overworld植物には広げない。

- **Nature's Mend / Nature's Heal**
  - ID: `naturesaura:aura_mending`, `naturesaura:aura_mending_enchantability`
  - コンテンツ概要: aura containerを使って修繕・回復を行うenchantment系機能。
  - 対応状態: 未対応。
  - TFC方針: TFC耐久・鍛造bonus・医療/食料バランスを壊さないよう、TFC金属装備への適用可否と回復量を確認する。

- **Effect Powder**
  - ID: `naturesaura:effect_powder`
  - コンテンツ概要: aura imbalance effectを粉として持ち運び、指定地点で発生させる。
  - 対応状態: 一部対応済み。
  - TFC方針: Tree RitualレシピはTFC苗木・素材へ置換済み。plant boost/decayはTFAura独自効果でTFC植物へ拡張済み。animal/ore_spawn/nether_grass/cache系は未対応で、animalはTFC家畜年齢・繁殖、oreはTFC鉱床生成、nether_grassはBeneath/Nether植生、cacheはAura Cache/Trove方針に合わせて検討する。

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
