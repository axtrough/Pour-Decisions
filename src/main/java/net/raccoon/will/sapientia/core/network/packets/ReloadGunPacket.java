package net.raccoon.will.sapientia.core.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.raccoon.will.sapientia.common.item.RevolverItem;

public record ReloadGunPacket() implements CustomPacketPayload {

    public static final Type<ReloadGunPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("sapientia", "reload_key"));

    public static final StreamCodec<FriendlyByteBuf, ReloadGunPacket> STREAM_CODEC =
            StreamCodec.unit(new ReloadGunPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ReloadGunPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                ItemStack[] stacks = {player.getMainHandItem(), player.getOffhandItem()};

                RevolverItem gunItem = null;
                ItemStack gunStack = null;

                for (ItemStack stack : stacks) {
                    if (stack.getItem() instanceof RevolverItem gun) {
                        gunItem = gun;
                        gunStack = stack;
                    }

                    if (gunItem != null) {
                        gunItem.reload(stack, player);
                    }
                }
            }
        });
    }
}

