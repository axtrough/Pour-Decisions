package net.raccoon.will.pour_decisions.common.item.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public record CocktailData(List<String> fluids) {

    public static final Codec<CocktailData> CODEC  = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.list(Codec.STRING).fieldOf("fluids").forGetter(CocktailData::fluids)
            ).apply(instance, CocktailData::new)
    );

    public static final StreamCodec<ByteBuf, CocktailData> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);
}
