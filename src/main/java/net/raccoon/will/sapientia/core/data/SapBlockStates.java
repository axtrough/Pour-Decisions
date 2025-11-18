package net.raccoon.will.sapientia.core.data;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.raccoon.will.sapientia.core.Sapientia;
import net.raccoon.will.sapientia.core.registry.SapBlocks;

public class SapBlockStates extends BlockStateProvider {
    public SapBlockStates(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Sapientia.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(SapBlocks.CHROMSTONE);
    }

    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }
}
