package net.raccoon.will.pour_decisions.core.data;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.raccoon.will.pour_decisions.core.registry.PDBlocks;

import java.util.Set;

public class PDBlockLoot extends BlockLootSubProvider {
    public PDBlockLoot(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        this.dropSelf(PDBlocks.CHROMSTONE.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return PDBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
