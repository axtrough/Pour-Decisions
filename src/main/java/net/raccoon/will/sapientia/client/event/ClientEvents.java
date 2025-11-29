package net.raccoon.will.sapientia.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.raccoon.will.sapientia.client.input.SapientiaKeybinds;
import net.raccoon.will.sapientia.client.particle.MuzzleFlashProvider;
import net.raccoon.will.sapientia.common.item.RevolverItem;
import net.raccoon.will.sapientia.core.Sapientia;
import net.raccoon.will.sapientia.core.network.packets.ReloadGunPacket;
import net.raccoon.will.sapientia.core.registry.SapGuiElements;
import net.raccoon.will.sapientia.core.registry.SapParticles;


@EventBusSubscriber(modid = Sapientia.MODID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        SapGuiElements.init();
    }

    @SubscribeEvent
    public static void registerParticleFactory(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(SapParticles.MUZZLEFLASH_PARTICLE.get(), MuzzleFlashProvider::new);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        while (SapientiaKeybinds.RELOAD_KEY.consumeClick()) {
            PacketDistributor.sendToServer(new ReloadGunPacket());
        }
    }
}
