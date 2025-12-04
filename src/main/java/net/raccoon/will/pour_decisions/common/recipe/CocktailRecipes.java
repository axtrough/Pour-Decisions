package net.raccoon.will.pour_decisions.common.recipe;

import net.minecraft.world.item.Item;

import java.util.List;

public record CocktailRecipes(List<String> ingredients, Item result) {
}
