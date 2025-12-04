package net.raccoon.will.pour_decisions.core.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.raccoon.will.pour_decisions.core.PourDecisions;

import java.util.function.Supplier;

public class PDSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, PourDecisions.MODID);

    public static Supplier<SoundEvent> REVOLVER_SHOOT = registerSoundEvent("revolver_shoot");
    public static Supplier<SoundEvent> REVOLVER_SPIN = registerSoundEvent("revolver_spin");
    public static Supplier<SoundEvent> REVOLVER_EMPTY = registerSoundEvent("revolver_empty");


    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(PourDecisions.MODID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
