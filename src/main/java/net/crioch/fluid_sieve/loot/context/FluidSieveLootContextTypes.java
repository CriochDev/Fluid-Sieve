package net.crioch.fluid_sieve.loot.context;

import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.context.ContextType;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class FluidSieveLootContextTypes {
    private static final LootContextTypesMapGetter ORIGINAL_MAP = ((LootContextTypesMapGetter)(Object)new LootContextTypes());
    public static final ContextType FLUID_SIEVE = register("sieve", builder -> builder.require(LootContextParameters.BLOCK_STATE).require(LootContextParameters.ORIGIN).allow(LootContextParameters.THIS_ENTITY));

    private static ContextType register(String name, Consumer<ContextType.Builder> type) {
        ContextType.Builder builder = new ContextType.Builder();
        type.accept(builder);
        ContextType lootContextType = builder.build();
        Identifier identifier = Identifier.of(name);
        ContextType lootContextType2 = ORIGINAL_MAP.getMap().put(identifier, lootContextType);
        if (lootContextType2 != null) {
            throw new IllegalStateException("Loot table parameter set " + String.valueOf(identifier) + " is already registered");
        } else {
            return lootContextType;
        }
    }
}
