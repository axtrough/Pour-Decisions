package net.raccoon.will.sapientia.client.event;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.raccoon.will.structura.api.gui.*;
import net.raccoon.will.sapientia.common.item.RevolverItem;
import net.raccoon.will.sapientia.core.Sapientia;
import net.raccoon.will.sapientia.core.registry.SapGuiElements;
import net.raccoon.will.structura.client.gui.GuiManager;

@EventBusSubscriber(modid = Sapientia.MODID, value = Dist.CLIENT)
public class GuiEvents {

    @SubscribeEvent
    public static void onRenderGenericGui(RenderGuiEvent.Pre event) {
        GuiGraphics guiGraphics = event.getGuiGraphics();
        Minecraft minecraft = Minecraft.getInstance();
        Window window = minecraft.getWindow();
        int screenWidth = window.getGuiScaledWidth();
        int screenHeight = window.getGuiScaledHeight();
        boolean debug = Screen.hasControlDown();

        Player player = minecraft.player;
        ItemStack[] stacks = {player.getMainHandItem(), player.getOffhandItem()};

        SapGuiElements.gunText().setDebug(debug);
        SapGuiElements.bulletIcon().setDebug(debug);

        RevolverItem gunItem = null;
        ItemStack gunStack = null;

        for (ItemStack stack : stacks) {
            if (stack.getItem() instanceof RevolverItem gun) {
                gunItem = gun;
                gunStack = stack;
            }
        }

            if (gunItem == null) {
                SapGuiElements.gunText().setVisible(false);
                SapGuiElements.bulletIcon().setVisible(false);
        } else {
                SapGuiElements.gunText().setVisible(true);
                SapGuiElements.bulletIcon().setVisible(true);

            int bullets = gunItem.getBulletCount(gunStack);
            int chambers = gunItem.getNumChambers(gunStack);
            SapGuiElements.gunText().setText(Component.literal(bullets + "/" + chambers));

                if (chambers >= 10) {
                    SapGuiElements.gunText().setOffsetX(SapGuiElements.bulletIcon().getOffsetX() - 22);
                } else {
                    SapGuiElements.gunText().resetOffset();
                }
        }

        GuiManager.render(guiGraphics, screenWidth, screenHeight, event);
    }
}

