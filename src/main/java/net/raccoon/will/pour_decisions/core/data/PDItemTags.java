package net.raccoon.will.pour_decisions.core.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.raccoon.will.pour_decisions.core.PourDecisions;
import net.raccoon.will.pour_decisions.core.registry.PDItems;
import net.raccoon.will.pour_decisions.core.registry.PDTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class PDItemTags extends ItemTagsProvider {
    public PDItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                      CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, PourDecisions.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(PDTags.Items.COCKTAIL_INGREDIENTS)
                //soft drink
                .add(PDItems.COLA.get())
                .add(PDItems.SPRITE.get())
                .add(PDItems.REDBULL.get())
                .add(PDItems.ICED_TEA.get())

                //alcohol
                .add(PDItems.JACK_DANIELS.get())
                .add(PDItems.GIN.get())
                .add(PDItems.VODKA.get())
                .add(PDItems.RUM.get())
                .add(PDItems.BEER.get());
    }
}
