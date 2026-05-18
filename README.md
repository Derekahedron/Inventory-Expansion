# Inventory Expansion

This project is the repository for the Inventory Expansion mod, which adds various items for managing your inventory.

## Items

### Sack

Can store 4 mixed stacks of items of the same sack type. Various sack types have been applied to different items via datapacks.
Sack types group similar items together, like stone, ores, and monster drops. Sacks are unique in that you can use items directly from the sack.
This lets you do things like combine multiple types of food into one sack, or collect all your mined ores in one sack to declutter your inventory.
Items are automatically added to your sack when picked up.

Sack type compatibility with various mods comes bundled in with this mod.

See below for information on how to add various sack types.

### Quiver

The quiver is similar to a sack/bundle, but it only holds arrows. It can hold 8 stacks of arrows and arrows are fired directly from the quiver.
Arrows are also picked up directly into the quiver.

### Bundle

This mod includes a backport of the bundle to 1.20.1, with some additional changes:

- Scrolling in a bundle now lets you scroll beyond the top 3 rows, allowing you to access additional slots.
- Drag clicking a bundle on the inventory can quickly dump/pick up items.

Dyed bundles have also been added, but are under the `invexp` namespace; so the blue bundle has the id `invexp:blue_bundle`.

## Adding sack types

Items can be added to existing sack types easily by adding them to the item tag: `invexp:sack_types/{sack_type}`.

This only exists if there is an existing defined sack type and sack type rule that connects it.

You can create a new sack type by adding the file `data/{mod_id}/invexp/sack_type/{sack_type_id}.json`. Sack types don't have any parameters, so your file should just consist of `{}`

There is a designated tag `invexp:sack_type/none` for removing sack types from items, making them unable to be used in sacks.

You can hook up the sack type with a sack type rule. Sack type rules live under `data/{mod_id}/invexp/sack_type_rule/{sack_type_rule_id}.json`.
To add all items from an item tag to a sack type, the rule should look like this:

```
{
  "items": {
    "tag": "my_mod_id:sack_type/my_sack_type"
  },
  "sack_type": "invexp:my_sack_type"
}
```

### Sack Weights

Sack weights (how much space in a sack an item takes up) are also similarly defined. The mod comes with some existing sack weight tags that you can use:

- `invexp:sack_weight/fifth`
- `invexp:sack_weight/fourth`
- `invexp:sack_weight/third`
- `invexp:sack_weight/half`
- `invexp:sack_weight/double`

You can also create your own sack weight with a sack weight rule. The schema is similar to sack types.

For example, the sack weight under `data/{mod_id}/invexp/sack_weight_rule/two_thirds.json` with the value:

```
{
  "items": {
    "tag": "invexp:sack_weight/two_thirds"
  },
  "sack_weight": {
    "denominator": 3,
    "numerator": 2
  }
}
```

makes it so any item under the tag `invexp:sack_weight/two_thirds` will have 2/3 sack weight.

### Predicates and Priority

You can attach an optional `predicate` and an optional `priority` to your `sack_type_rule` and `sack_weight_rule`.
Having a higher priority means this rule is applied before other rules. This is how we get around items being in multiple
different sack type tags.

The predicate schema defines an item predicate that tests if the rule should be applied to an item stack.

Take the example sack weight rule `bees.json`:

```
{
  "items": [
    {
      "item": "minecraft:beehive"
    },
    {
      "item": "minecraft:bee_nest"
    }
  ],
  "predicate": {
    "nbt": "{BlockEntityTag:{Bees:[{EntityData:{id:\"minecraft:bee\"}}]}}"
  },
  "priority": 10,
  "sack_weight": 64
}
```

This rule applies a sack weight of 64 (full stack) to beehives and bees nests if they have bees inside of them.
The priority is 64 so it overpowers any sack weight rule applied already (the default priority is 0).

## Container Item API

All sack and quiver behavior was implemented without hard-coding any logic in, meaning it's possible to extend this to
add more container items, like an ammo pouch or a potion bag. To implement the quiver, you need to make the classes
`QuiverItem`, `QuiverContents`, `QuiverContentsReader`, and `QuiverContentsWriter`, as well as `ClientQuiverTooltip` and `QuiverTooltip`.
Then the quiver behavior is registered in `ContainerItemBehaviors`.

There are also methods you can extend on `SackItem`, `QuiverItem`, and `BetterBundleItem` that give you precise control
over attributes like max weight. This allows you to extend the items and make your own quivers/sacks/bundles with custom attributes.