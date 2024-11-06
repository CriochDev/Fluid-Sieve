package net.crioch.fluid_sieve;

import net.crioch.fluid_sieve.block.FluidSieveBlocks;
import net.crioch.fluid_sieve.item.FluidSieveItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.enchantment.Enchantment;

public class FluidSieveMod implements ModInitializer {
    public static final String MOD_ID = "fluid_sieve";

    @Override
    public void onInitialize() {
        FluidSieveBlocks.register();
        FluidSieveItems.register();
    }
}
