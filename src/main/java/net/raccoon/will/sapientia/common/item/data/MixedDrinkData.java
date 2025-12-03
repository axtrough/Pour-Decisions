package net.raccoon.will.sapientia.common.item.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public record MixedDrinkData(List<String> fluids) {

    public static final Codec<MixedDrinkData> CODEC  = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.list(Codec.STRING).fieldOf("fluids").forGetter(MixedDrinkData::fluids)
            ).apply(instance, MixedDrinkData::new)
    );

    public static final StreamCodec<ByteBuf, MixedDrinkData> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);
}
