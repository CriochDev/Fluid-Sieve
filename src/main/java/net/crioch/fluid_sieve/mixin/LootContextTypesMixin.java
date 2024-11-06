package net.crioch.fluid_sieve.mixin;

import com.google.common.collect.BiMap;
import net.crioch.fluid_sieve.loot.context.LootContextTypesMapGetter;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.context.ContextType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LootContextTypes.class)
public class LootContextTypesMixin implements LootContextTypesMapGetter {
    @Shadow
    @Final
    private static BiMap<Identifier, ContextType> MAP;

    @Override
    public BiMap<Identifier, ContextType> getMap() {
        return MAP;
    }
}
