package net.raccoon.will.pour_decisions.core.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.raccoon.will.pour_decisions.core.network.packets.ReloadGunPacket;

public class NetworkHandler {

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                ReloadGunPacket.TYPE,
                ReloadGunPacket.STREAM_CODEC,
                ReloadGunPacket::handle
        );
    }
}
