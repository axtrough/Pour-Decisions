package net.raccoon.will.pour_decisions.core.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.raccoon.will.pour_decisions.common.item.*;
import net.raccoon.will.pour_decisions.core.PourDecisions;

import java.util.List;

import static net.minecraft.world.item.Tiers.DIAMOND;

public class PDItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PourDecisions.MODID);

    //Misc
    public static final DeferredItem<SwordItem> BIG_DIAMOND_SWORD = ITEMS.register("big_diamond_sword", () -> new SwordItem(DIAMOND, new Item.Properties()
                    .attributes(SwordItem.createAttributes(DIAMOND, 5, -2.4f))));

    public static final DeferredItem<Item> HOME_RUNE = ITEMS.register("home_rune", () -> new HomeRuneItem(new Item.Properties()
            .stacksTo(1)
            .component(PDComponents.MAX_COOLDOWN.get(), 2400), 2400));

    public static final DeferredItem<Item> REVOLVER = ITEMS.register("revolver", () -> new RevolverItem(new Item.Properties()
            .stacksTo(1)));

    public static final DeferredItem<Item> BULLET = ITEMS.register("bullet", () -> new Item(new Item.Properties()
                    .stacksTo(64)));

    public static final DeferredItem<Item> COCKTAIL_SHAKER = ITEMS.register("cocktail_shaker", () -> new CocktailShakerItem(new Item.Properties()
            .stacksTo(1)));

    //Beverages
    public static final DeferredItem<Item> COLA = ITEMS.register("cola", () -> new BeverageItem(new Item.Properties()
            .stacksTo(16), "cola", false, 8));

    public static final DeferredItem<Item> SPRITE = ITEMS.register("sprite", () -> new BeverageItem(new Item.Properties()
            .stacksTo(16), "sprite", false, 8));

    public static final DeferredItem<Item> ICED_TEA = ITEMS.register("iced_tea", () -> new BeverageItem(new Item.Properties()
            .stacksTo(16), "iced_tea", false, 8));

    public static final DeferredItem<Item> REDBULL = ITEMS.register("redbull", () -> new BeverageItem(new Item.Properties()
            .stacksTo(16), "redbull", false, 8));

    public static final DeferredItem<Item> BEER = ITEMS.register("beer", () -> new BeverageItem(new Item.Properties()
            .stacksTo(16), "beer", true, 8));

    public static final DeferredItem<Item> JACK_DANIELS = ITEMS.register("jack_daniels", () -> new BeverageItem(new Item.Properties()
            .stacksTo(4), "jack_daniels", true, 16));

    public static final DeferredItem<Item> VODKA = ITEMS.register("vodka", () -> new BeverageItem(new Item.Properties()
            .stacksTo(4), "vodka", true, 16));

    public static final DeferredItem<Item> GIN = ITEMS.register("gin", () -> new BeverageItem(new Item.Properties()
            .stacksTo(4), "gin", true, 16));

    public static final DeferredItem<Item> RUM = ITEMS.register("rum", () -> new BeverageItem(new Item.Properties()
            .stacksTo(4), "rum", true, 16));

    //Complete Drinks
    public static final DeferredItem<Item> JACK_N_COKE = ITEMS.register("jack_n_coke", () -> new CocktailItem(new Item.Properties()
            .stacksTo(1), List.of("jack_daniels", "coke")));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}