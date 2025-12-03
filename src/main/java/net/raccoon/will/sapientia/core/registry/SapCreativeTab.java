package net.raccoon.will.sapientia.core.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.raccoon.will.sapientia.core.Sapientia;

import java.util.function.Supplier;

public class SapCreativeTab {
    public static DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Sapientia.MODID);

    public static final Supplier<CreativeModeTab> SAPIENTIA_TAB = CREATIVE_MODE_TAB.register("sapientia_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(SapItems.REVOLVER.get()))
                    .title(Component.translatable("creativetab.sapientia.sapientia_tab"))
                    .displayItems((parameters, output) -> {
                        output.accept(SapItems.HOME_RUNE);
                        output.accept(SapBlocks.CHROMSTONE);
                        output.accept(SapItems.REVOLVER);
                        output.accept(SapItems.BULLET);
                        output.accept(SapItems.COCKTAIL_SHAKER);



                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
