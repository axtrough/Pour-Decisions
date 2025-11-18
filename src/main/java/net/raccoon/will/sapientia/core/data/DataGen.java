package net.raccoon.will.sapientia.core.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.raccoon.will.sapientia.core.Sapientia;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Sapientia.MODID)
public class DataGen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        PackOutput output = event.getGenerator().getPackOutput();
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        gen.addProvider(event.includeServer(), new SapItemModels(output, helper));
        gen.addProvider(event.includeServer(), new SapBlockStates(output, helper));


        BlockTagsProvider blockTagsProvider = new SapBlockTags(output, lookupProvider, helper);
        gen.addProvider(event.includeServer(), blockTagsProvider);

        gen.addProvider(event.includeServer(), new SapItemTags(output, lookupProvider, blockTagsProvider.contentsGetter(), helper));

        gen.addProvider(event.includeServer(), new LootTableProvider(output, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(SapBlockLoot::new, LootContextParamSets.BLOCK)), lookupProvider));
    }
}
