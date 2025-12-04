package net.raccoon.will.pour_decisions.core.data;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.raccoon.will.pour_decisions.core.PourDecisions;
import net.raccoon.will.pour_decisions.core.registry.PDBlocks;

public class PDBlockStates extends BlockStateProvider {
    public PDBlockStates(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, PourDecisions.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(PDBlocks.CHROMSTONE);
    }

    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }
}
