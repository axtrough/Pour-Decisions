package net.raccoon.will.pour_decisions.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.raccoon.will.pour_decisions.common.item.data.CocktailData;
import net.raccoon.will.pour_decisions.core.registry.PDComponents;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CocktailItem extends Item {
    private final List<String> recipe;

    public CocktailItem(Properties properties, List<String> recipe) {
        super(properties);
        this.recipe = List.copyOf(recipe);
    }

    public List<String> getRecipe() {
        return this.recipe;
    }

    public List<String> getFluids(ItemStack stack) {
        CocktailData data = stack.get(PDComponents.COCKTAIL_FLUIDS.get());
        return data != null ? data.fluids() : new ArrayList<>();
    }

    public boolean isComplete(ItemStack stack) {
        List<String> actual = getFluids(stack);
        return new HashSet<>(actual).containsAll(recipe) && new HashSet<>(recipe).containsAll(actual);
    }
}
