# Item Size / Weight

TerraFirmaAuraのitem size/weightは、TerraFirmaCraft 1.21.1-4.2.5のデータと
`ItemSizeManager`のフォールバックを基準にする。

## TFC基準

| 対象 | Size | Weight | 根拠 |
| --- | --- | --- | --- |
| 一般Item | very small | very light | `DEFAULT_SIZE` |
| 一般BlockItem | small | light | `BLOCK_SIZE` |
| plants / bowl powders | tiny | very light | TFC item_size |
| loose fiber | small | very light | jute / straw |
| ingot / sheet / double ingot | large | medium | TFC item_size |
| logs | very large | medium | `#minecraft:logs` |
| slabs | small | very light | `#minecraft:slabs` |
| stairs | small | light | `#minecraft:stairs` |
| rails | large | very light | `#minecraft:rails` |
| tools | very large | very heavy | TFC tools definition |
| armor | large | very heavy | `ARMOR_SIZE` |
| chest | large | light | `#c:chests` |
| minecart | very large | very heavy | `#c:minecarts` |
| table-like device | large | light | scribing/sewing table |
| compact mechanism | large | heavy | grill/tuyere相当 |
| large powered device | large | very heavy | bloomery/power loom相当 |

## TFAura分類

- Nature's AuraとTFAuraのancient log/wood/barkはvery large/medium。
- ancient planks、leaves、sapling、stairs、fence、fence gateはsmall/light。
- ancient slabはsmall/very light。
- Nature's Aura ancient stickとTFAura ancient lumberは、`#c:rods/wooden` に合わせてnormal/light。
- Nature's Auraの植物・powderはtiny/very light。gold fiberはjute相当のsmall/very light。
- Nature's Aura ingotはlarge/medium。TFAura金属フォームはCommon tag経由でTFC定義を使う。
- Nature's Aura toolsはvery large/very heavy、armorはlarge/very heavy。
- Nature's Aura金属storage blockと通常建材は、一般BlockItemと同じsmall/light。
- 各機能ブロックは用途が最も近いTFC設備に合わせ、一律に同じ値を付けない。
