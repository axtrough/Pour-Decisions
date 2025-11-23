package net.raccoon.will.sapientia.client.event;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.raccoon.will.sapientia.api.client.gui.GuiManager;
import net.raccoon.will.sapientia.common.item.RevolverItem;
import net.raccoon.will.sapientia.core.Sapientia;
import net.raccoon.will.sapientia.core.registry.SapGuiElements;

@EventBusSubscriber(modid = Sapientia.MODID, value = Dist.CLIENT)
public class GuiEvents {

    @SubscribeEvent
    public static void onRenderGenericGui(RenderGuiEvent.Pre event) {
        float deltaSeconds = 1f / 20f + event.getPartialTick().getGameTimeDeltaTicks() / 20f;
        GuiGraphics guiGraphics = event.getGuiGraphics();
        Minecraft minecraft = Minecraft.getInstance();
        Window window = minecraft.getWindow();
        Player player = minecraft.player;
        ItemStack stack = player.getMainHandItem();



        int screenWidth = window.getGuiScaledWidth();
        int screenHeight = window.getGuiScaledHeight();

        if (stack.getItem() instanceof RevolverItem gunItem) {
            int bullets = gunItem.getBulletCount(stack);
            int chambers = gunItem.getNumChambers(stack);

            if (player.isHolding(gunItem)) {
                SapGuiElements.gunInfo().setText(Component.literal(bullets + "/" + chambers));
            } else {
                SapGuiElements.gunInfo().resetText();
            }
        }

        GuiManager.render(guiGraphics, screenWidth, screenHeight, event);
    }
}

