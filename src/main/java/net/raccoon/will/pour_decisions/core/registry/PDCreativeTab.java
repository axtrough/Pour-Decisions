package net.raccoon.will.pour_decisions.core.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.raccoon.will.pour_decisions.core.PourDecisions;

import java.util.function.Supplier;

public class PDCreativeTab {
    public static DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PourDecisions.MODID);

    public static final Supplier<CreativeModeTab> POUR_DECISIONS_TAB = CREATIVE_MODE_TAB.register("pour_decisions_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(PDItems.REVOLVER.get()))
                    .title(Component.translatable("itemGroup.pour_decisions_tab"))
                    .displayItems((parameters, output) -> {
                        output.accept(PDItems.HOME_RUNE);
                        output.accept(PDBlocks.CHROMSTONE);
                        output.accept(PDItems.REVOLVER);
                        output.accept(PDItems.BULLET);
                        output.accept(PDItems.COCKTAIL_SHAKER);
                        output.accept(PDItems.JACK_N_COKE);
                        output.accept(PDItems.JACK_DANIELS);
                        output.accept(PDItems.VODKA);
                        output.accept(PDItems.GIN);
                        output.accept(PDItems.BEER);
                        output.accept(PDItems.ICED_TEA);
                        output.accept(PDItems.REDBULL);
                        output.accept(PDItems.SPRITE);
                        output.accept(PDItems.COLA);






                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
