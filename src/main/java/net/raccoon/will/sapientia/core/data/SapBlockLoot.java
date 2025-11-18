package net.raccoon.will.sapientia.core.data;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.raccoon.will.sapientia.core.registry.SapBlocks;

import java.util.Set;

public class SapBlockLoot extends BlockLootSubProvider {
    public SapBlockLoot(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        this.dropSelf(SapBlocks.CHROMSTONE.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return SapBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
