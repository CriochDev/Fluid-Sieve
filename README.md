# Fluid Sieve: Rewoven
A remake of CheaterCode's [Fluid Sieve](https://github.com/CheaterCodes/FluidSieve) for Minecraft Fabric 1.21+.

## Overview
Introduces two new blocks: String Sieve and Dense String Sieve. These can be placed on top of any solid block, or a hopper, and generates items. Unlike the original mod, the sieves are not block entities and depend on random ticks to generate items.

If the block beneath them has an inventory the items are added to it, otherwise they spawn into the world.

## Changing Generated Items
All generated items are drawn from loot tables that are of the type `fluid_sieve:sieve`.

Loot tables for a specific fluid should be added at `<fluid namespace>/loot_table/sieve/<sieve_type>/<fluid name>.json` within the. For Minecraft's water fluid and the String Sieve, that would look like `minecraft/loot_table/sieve/water.json`.

Currently this mod can only handle loot for the `minecraft:empty` and `minecraft:water` fluids, but work is planned for the future to allow it to work more like the original.

### Available Loot Context Parameters
The `fluid_sieve:sieve` loot context type has the following parameters available to it:
- Block State
    - Allows use of the `block_state_property` predicate
- Origin: The position of the sieve
    - Allows use of the `location_check` predicate
- `this` entity: A random entity within the sieve
    - Allows use of the `entity_properties` predicate
    - Allows use of the `entity_scores` predicate

## Roadmap
- Allow fluids to flow through the block (Waiting for [Towlette](https://github.com/Virtuoel/Towelette) to update to 1.21)
- Implement a few more sieve types