package net.crioch.fluid_sieve.loot.context;

import com.google.common.collect.BiMap;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.util.Identifier;

public interface LootContextTypesMapGetter {
    BiMap<Identifier, LootContextType> getMap();
}
