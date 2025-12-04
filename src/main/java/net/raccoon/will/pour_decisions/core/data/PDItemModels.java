package net.raccoon.will.pour_decisions.core.data;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.raccoon.will.pour_decisions.core.PourDecisions;
import net.raccoon.will.pour_decisions.core.registry.PDItems;

public class PDItemModels extends ItemModelProvider {
    public PDItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, PourDecisions.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(PDItems.HOME_RUNE.get());
        basicItem(PDItems.BULLET.get());
        basicItem(PDItems.COCKTAIL_SHAKER.get());
        basicItem(PDItems.JACK_DANIELS.get());
//        basicItem(SapItems.VODKA.get());
//        basicItem(SapItems.BEER.get());
        basicItem(PDItems.COLA.get());
        basicItem(PDItems.JACK_N_COKE.get());
    }
}
