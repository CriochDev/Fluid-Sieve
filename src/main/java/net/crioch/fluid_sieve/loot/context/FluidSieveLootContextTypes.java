package net.crioch.fluid_sieve.loot.context;

import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class FluidSieveLootContextTypes {
    private static final LootContextTypesMapGetter ORIGINAL_MAP = ((LootContextTypesMapGetter)(Object)new LootContextTypes());
    public static final LootContextType FLUID_SIEVE = register("sieve", builder -> builder.require(LootContextParameters.BLOCK_STATE).require(LootContextParameters.ORIGIN).allow(LootContextParameters.THIS_ENTITY));

    private static LootContextType register(String name, Consumer<LootContextType.Builder> type) {
        LootContextType.Builder builder = new LootContextType.Builder();
        type.accept(builder);
        LootContextType lootContextType = builder.build();
        Identifier identifier = Identifier.of(name);
        LootContextType lootContextType2 = ORIGINAL_MAP.getMap().put(identifier, lootContextType);
        if (lootContextType2 != null) {
            throw new IllegalStateException("Loot table parameter set " + String.valueOf(identifier) + " is already registered");
        } else {
            return lootContextType;
        }
    }
}
