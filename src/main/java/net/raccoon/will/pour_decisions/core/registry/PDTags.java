package net.raccoon.will.pour_decisions.core.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.raccoon.will.pour_decisions.core.PourDecisions;

public class PDTags {
    public static class Blocks {
        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(PourDecisions.MODID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> COCKTAIL_INGREDIENTS = createTag("cocktail_ingredients");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(PourDecisions.MODID, name));
        }
    }
}
