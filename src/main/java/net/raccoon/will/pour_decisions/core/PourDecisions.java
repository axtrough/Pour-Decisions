 package net.raccoon.will.pour_decisions.core;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.raccoon.will.pour_decisions.client.input.PDKeybinds;
import net.raccoon.will.pour_decisions.common.command.RouletteCommand;
import net.raccoon.will.pour_decisions.core.network.NetworkHandler;
import net.raccoon.will.pour_decisions.core.registry.*;
import org.slf4j.Logger;


@Mod(PourDecisions.MODID)
public class PourDecisions {
    public static final String MODID = "pour_decisions";
    private static final Logger LOGGER = LogUtils.getLogger();

    public PourDecisions(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);

        PDBlocks.register(modEventBus);
        PDItems.register(modEventBus);
        PDComponents.register(modEventBus);
        PDSounds.register(modEventBus);
        PDCreativeTab.register(modEventBus);
        PDParticles.register(modEventBus);

        modEventBus.addListener(NetworkHandler::registerPayloads);
        modEventBus.addListener(PDKeybinds::register);
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
}