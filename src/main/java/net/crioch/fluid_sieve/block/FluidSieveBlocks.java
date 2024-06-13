package net.crioch.fluid_sieve.block;

import net.crioch.fluid_sieve.FluidSieveMod;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FluidSieveBlocks {
    public static  Block STRING_SIEVE;
    public static Block DENSE_SIEVE;
    public static Block IRON_SIEVE;
    public static Block NETHERITE_SIEVE;

    public static void register() {
        STRING_SIEVE = register("string_sieve", new BaseSieve(Block.Settings.copy(Blocks.OAK_PLANKS)));
        DENSE_SIEVE = register("dense_sieve", new BaseSieve(Block.Settings.copy(Blocks.OAK_PLANKS)));
    }

    private static Block register(String path, Block block) {
        return Registry.register(Registries.BLOCK, Identifier.of(FluidSieveMod.MOD_ID, path), block);
    }
}
