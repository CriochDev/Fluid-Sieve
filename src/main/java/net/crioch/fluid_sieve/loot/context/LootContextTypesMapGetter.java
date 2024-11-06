package net.crioch.fluid_sieve.loot.context;

import com.google.common.collect.BiMap;
import net.minecraft.util.context.ContextType;
import net.minecraft.util.Identifier;

public interface LootContextTypesMapGetter {
    BiMap<Identifier, ContextType> getMap();
}
