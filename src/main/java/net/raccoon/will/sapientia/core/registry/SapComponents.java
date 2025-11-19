package net.raccoon.will.sapientia.core.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;


import java.util.function.Supplier;

public class SapComponents {
    public static final DeferredRegister.DataComponents COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, "sapientia");

    public static final Supplier<DataComponentType<BlockPos>> RUNE_POS =
            register("rune_pos", BlockPos.CODEC, BlockPos.STREAM_CODEC);

    public static final Supplier<DataComponentType<Integer>> COOLDOWN =
            register("cooldown", ExtraCodecs.NON_NEGATIVE_INT, ByteBufCodecs.VAR_INT);

    public static final Supplier<DataComponentType<Integer>> MAX_COOLDOWN =
            register("max_cooldown", ExtraCodecs.NON_NEGATIVE_INT, ByteBufCodecs.VAR_INT);


    private static <T> Supplier<DataComponentType<T>> register(
            String name,
            Codec<T> codec,
            StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec
    ) {
        return COMPONENTS.register(name, () ->
                DataComponentType.<T>builder()
                        .persistent(codec)
                        .networkSynchronized(streamCodec)
                        .build()
        );
    }

    public static void register(IEventBus bus) {
        COMPONENTS.register(bus);
    }
}