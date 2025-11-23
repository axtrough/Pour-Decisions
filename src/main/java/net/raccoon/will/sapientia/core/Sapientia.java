 package net.raccoon.will.sapientia.core;

import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.raccoon.will.sapientia.common.command.RouletteCommand;
import net.raccoon.will.sapientia.core.registry.*;
import org.slf4j.Logger;


@Mod(Sapientia.MODID)
public class Sapientia {
    public static final String MODID = "sapientia";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Sapientia(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);

        SapBlocks.register(modEventBus);
        SapItems.register(modEventBus);
        SapComponents.register(modEventBus);
        SapSounds.register(modEventBus);
        SapCreativeTab.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        RouletteCommand.register(event.getDispatcher());
    }

    @EventBusSubscriber(modid = Sapientia.MODID, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            SapGuiElements.init();
        }
    }
}
