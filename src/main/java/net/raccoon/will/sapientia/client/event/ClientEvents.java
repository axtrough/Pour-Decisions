package net.raccoon.will.sapientia.client.event;


import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.raccoon.will.sapientia.client.input.SapientiaKeybinds;
import net.raccoon.will.sapientia.core.Sapientia;
import net.raccoon.will.sapientia.core.network.packets.ReloadGunPacket;

@EventBusSubscriber(modid = Sapientia.MODID, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        while (SapientiaKeybinds.RELOAD_KEY.consumeClick()) {
            PacketDistributor.sendToServer(new ReloadGunPacket());
        }
    }
}
