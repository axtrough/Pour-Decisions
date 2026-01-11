package net.raccoon.will.pour_decisions.event;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.raccoon.will.pour_decisions.PourDecisions;
import net.raccoon.will.pour_decisions.client.input.PDKeybinds;
import net.raccoon.will.pour_decisions.client.particle.MuzzleFlashProvider;
import net.raccoon.will.pour_decisions.client.renderer.TrayRenderer;
import net.raccoon.will.pour_decisions.core.network.packets.ReloadGunPacket;
import net.raccoon.will.pour_decisions.registry.PDBlockEntities;
import net.raccoon.will.pour_decisions.registry.PDHudRegistry;
import net.raccoon.will.pour_decisions.registry.PDParticles;


@EventBusSubscriber(modid = PourDecisions.MODID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        BlockEntityRenderers.register(PDBlockEntities.TRAY_BLOCK_ENTITY.get(), TrayRenderer::new);
        PDHudRegistry.register();
    }

    @SubscribeEvent
    public static void registerBE(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(PDBlockEntities.TRAY_BLOCK_ENTITY.get(),
                TrayRenderer::new);
    }

    @SubscribeEvent
    public static void registerParticleFactory(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(PDParticles.MUZZLEFLASH_PARTICLE.get(), MuzzleFlashProvider::new);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        while (PDKeybinds.RELOAD_KEY.consumeClick()) {
            PacketDistributor.sendToServer(new ReloadGunPacket());
        }
    }
}
