package net.raccoon.will.pour_decisions.common.item.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public record CocktailShakerData(List<ItemStack> ingredients, int shakeProgress,
                                 int maxShake, ItemStack result) {

    public static final Codec<CocktailShakerData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ItemStack.CODEC.listOf().fieldOf("ingredients").forGetter(CocktailShakerData::ingredients),
                    Codec.INT.fieldOf("shake_progress").forGetter(CocktailShakerData::shakeProgress),
                    Codec.INT.fieldOf("max_shake").forGetter(CocktailShakerData::maxShake),
                    ItemStack.CODEC.optionalFieldOf("result", ItemStack.EMPTY).forGetter(CocktailShakerData::result)
            ).apply(instance, CocktailShakerData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CocktailShakerData> STREAM_CODEC =
            StreamCodec.composite(
                    ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), CocktailShakerData::ingredients,
                    ByteBufCodecs.VAR_INT, CocktailShakerData::shakeProgress,
                    ByteBufCodecs.VAR_INT, CocktailShakerData::maxShake,
                    ByteBufCodecs.optional(ItemStack.STREAM_CODEC),
                    data -> data.result().isEmpty() ? Optional.empty() : Optional.of(data.result()),
                    (ingredients, shake, max, resultOpt) ->
                            new CocktailShakerData(ingredients, shake, max, resultOpt.orElse(ItemStack.EMPTY))
            );
}
