package net.raccoon.will.pour_decisions.core.registry;

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
import net.raccoon.will.pour_decisions.common.item.data.CocktailShakerData;
import net.raccoon.will.pour_decisions.common.item.data.CocktailData;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PDComponents {
    public static final DeferredRegister.DataComponents COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, "pour_decisions");

    public static final Supplier<DataComponentType<BlockPos>> RUNE_POS = register("rune_pos", BlockPos.CODEC, BlockPos.STREAM_CODEC);

    public static final Supplier<DataComponentType<Integer>> COOLDOWN = register("cooldown", ExtraCodecs.NON_NEGATIVE_INT, ByteBufCodecs.VAR_INT);
    public static final Supplier<DataComponentType<Integer>> MAX_COOLDOWN = register("max_cooldown", ExtraCodecs.NON_NEGATIVE_INT, ByteBufCodecs.VAR_INT);
    public static final Supplier<DataComponentType<Integer>> CURRENT_CHAMBER = register("current_chamber", ExtraCodecs.NON_NEGATIVE_INT, ByteBufCodecs.VAR_INT);
    public static final Supplier<DataComponentType<Integer>> NUM_CHAMBERS = register("num_chambers", ExtraCodecs.NON_NEGATIVE_INT, ByteBufCodecs.VAR_INT);
    public static final Supplier<DataComponentType<List<Integer>>> BULLET_CHAMBERS = registerIntList("bullet_chambers");

    public static final Supplier<DataComponentType<CocktailShakerData>> SHAKER_DATA =
            register("shaker_data", CocktailShakerData.CODEC, CocktailShakerData.STREAM_CODEC);

    public static final Supplier<DataComponentType<CocktailData>> COCKTAIL_FLUIDS =
            register("cocktail_fluids", CocktailData.CODEC, CocktailData.STREAM_CODEC);

    public static final Supplier<DataComponentType<Integer>> BEVERAGE_USES = register("beverage_uses", ExtraCodecs.NON_NEGATIVE_INT, ByteBufCodecs.VAR_INT);


    private static Supplier<DataComponentType<List<Integer>>> registerIntList(String name) {
        StreamCodec<RegistryFriendlyByteBuf, List<Integer>> codec = StreamCodec.of(
                (buf, list) -> {
                    buf.writeVarInt(list.size());
                    for (int i : list) buf.writeVarInt(i);
                },
                buf -> {
                    int size = buf.readVarInt();
                    List<Integer> list = new ArrayList<>(size);
                    for (int j = 0; j < size; j++) list.add(buf.readVarInt());
                    return list;
                }
        );
        return register(name, Codec.list(Codec.INT), codec);
    }

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