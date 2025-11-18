package net.raccoon.will.sapientia.core.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.raccoon.will.sapientia.common.item.HomeRuneItem;
import net.raccoon.will.sapientia.core.Sapientia;

import static net.minecraft.world.item.Tiers.DIAMOND;

public class SapItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Sapientia.MODID);

    public static final DeferredItem<SwordItem> BIG_DIAMOND_SWORD = ITEMS.register("big_diamond_sword",
            () -> new SwordItem(DIAMOND, new Item.Properties().attributes(SwordItem.createAttributes(DIAMOND, 5, -2.4f))));

    public static final DeferredItem<Item> HOME_RUNE = ITEMS.register("home_rune",
                    () -> new HomeRuneItem(new Item.Properties().stacksTo(1)
                            .component(SapComponents.MAX_COOLDOWN.get(), 2400), 2400));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
