package net.raccoon.will.sapientia.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.raccoon.will.sapientia.common.item.data.MixedDrinkData;
import net.raccoon.will.sapientia.core.registry.SapComponents;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MixedDrinkItem extends Item {
    private final List<String> recipe;

    public MixedDrinkItem(Properties properties, List<String> recipe) {
        super(properties);
        this.recipe = List.copyOf(recipe);
    }

    public List<String> getRecipe() {
        return recipe;
    }

    public List<String> getFluids(ItemStack stack) {
        MixedDrinkData data = stack.get(SapComponents.MIXED_DRINK_FLUIDS.get());
        return data != null ? data.fluids() : new ArrayList<>();
    }

    public boolean isComplete(ItemStack stack) {
        List<String> actual = getFluids(stack);
        return new HashSet<>(actual).containsAll(recipe) && new HashSet<>(recipe).containsAll(actual);
    }
}
