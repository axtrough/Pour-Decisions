package net.raccoon.will.sapientia.client.overlay;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.raccoon.will.sapientia.core.Sapientia;

@EventBusSubscriber(modid = Sapientia.MODID, value = Dist.CLIENT)
public class GuiEvents {

    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiEvent.Pre event) {
        GuiGraphics guiGraphics = event.getGuiGraphics();
        Minecraft minecraft = Minecraft.getInstance();
        Window window = minecraft.getWindow();

        int screenWidth = window.getGuiScaledWidth();
        int screenHeight = window.getGuiScaledHeight();

        GuiManager.render(guiGraphics, screenWidth, screenHeight, event);
    }
}

