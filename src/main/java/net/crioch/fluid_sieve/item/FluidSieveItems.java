package net.crioch.fluid_sieve.item;

import net.crioch.fluid_sieve.FluidSieveMod;
import net.crioch.fluid_sieve.block.FluidSieveBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FluidSieveItems {
    public static Item STRING_SIEVE;
    public static Item DENSE_SIEVE;

    public static Item STRING_MESH;
    public static Item DENSE_MESH;

    public static void register() {
        STRING_SIEVE = register("string_sieve", new BlockItem(FluidSieveBlocks.STRING_SIEVE, new Item.Settings()));
        DENSE_SIEVE = register("dense_sieve", new BlockItem(FluidSieveBlocks.DENSE_SIEVE, new Item.Settings()));

        STRING_MESH = register("string_mesh", new Item(new Item.Settings()));
        DENSE_MESH = register("dense_mesh", new Item(new Item.Settings()));

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.add(STRING_SIEVE);
            entries.add(DENSE_SIEVE);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(STRING_MESH);
            entries.add(DENSE_MESH);
        });
    }

    private static Item register(String id, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(FluidSieveMod.MOD_ID, id), item);
    }
}
