package net.crioch.fluid_sieve.client;

import net.crioch.fluid_sieve.block.FluidSieveBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class FluidSieveClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), FluidSieveBlocks.STRING_SIEVE, FluidSieveBlocks.DENSE_SIEVE);
    }
}