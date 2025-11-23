package net.raccoon.will.sapientia.core.data;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.raccoon.will.sapientia.core.Sapientia;
import net.raccoon.will.sapientia.core.registry.SapItems;

public class SapItemModels extends ItemModelProvider {
    public SapItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Sapientia.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(SapItems.HOME_RUNE.get());
        basicItem(SapItems.REVOLVER.get());
    }
}
