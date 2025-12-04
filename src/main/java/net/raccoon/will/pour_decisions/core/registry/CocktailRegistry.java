package net.raccoon.will.pour_decisions.core.registry;

import net.minecraft.world.item.ItemStack;
import net.raccoon.will.pour_decisions.common.recipe.CocktailRecipes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CocktailRegistry {
    public static final List<CocktailRecipes> RECIPES = List.of(
            new CocktailRecipes(List.of("jack_daniels", "cola"), PDItems.JACK_N_COKE.get())
            //other recipes



    );

    public static ItemStack getResultFor(List<String> fluids) {
        Set<String> fluidsSet = new HashSet<>(fluids);

        for (CocktailRecipes recipe : RECIPES) {
            Set<String> recipeSet = new HashSet<>(recipe.ingredients());

            if (fluidsSet.equals(recipeSet)) {
                return new ItemStack(recipe.result());
            }
        }
        return ItemStack.EMPTY;
    }
}
