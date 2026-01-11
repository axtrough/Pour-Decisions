package net.raccoon.will.pour_decisions.client.hud;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.raccoon.will.pour_decisions.common.item.RevolverItem;
import net.raccoon.will.pour_decisions.registry.PDItems;
import net.raccoon.will.structura.api.gui.element.ItemElement;
import net.raccoon.will.structura.api.gui.element.TextElement;
import net.raccoon.will.structura.api.gui.hud.BaseHud;
import net.raccoon.will.structura.api.gui.layout.Anchor;
import net.raccoon.will.structura.api.gui.layout.ElementAnchor;

public class RevolverHud extends BaseHud {
    private final TextElement ammoText;
    private final ItemElement bulletIcon;

    public RevolverHud() {
        bulletIcon = addElement(new ItemElement(new ItemStack(PDItems.BULLET.get()), 16, 16, Anchor.BOTTOM_CENTER, -100, 3));
        ammoText = addElement(new TextElement(Component.literal(""), 0xd185d6, true, Anchor.BOTTOM_CENTER, bulletIcon.getOffsetX() - bulletIcon.width / 2, 6));

        ammoText.setElementAnchor(ElementAnchor.BOTTOM_RIGHT);
        bulletIcon.setElementAnchor(ElementAnchor.BOTTOM_CENTER);
    }

    @Override
    public void update(Player player) {
        ItemStack gunStack = BaseHud.findHeldItem(player, PDItems.REVOLVER.asItem());
        boolean hasGun = !gunStack.isEmpty();

        ammoText.setVisible(hasGun);
        bulletIcon.setVisible(hasGun);
        if (!hasGun) return;

        RevolverItem gunItem = (RevolverItem) gunStack.getItem();
        ammoText.setText(Component.literal(gunItem.getBulletCount(gunStack) + "/" + gunItem.getNumChambers(gunStack)));

        int bullets = gunItem.getBulletCount(gunStack);
        int chambers = gunItem.getNumChambers(gunStack);
        ammoText.setText(Component.literal(bullets + "/" + chambers));

        if (player.getOffhandItem().is(gunItem)) {
            bulletIcon.setOffsetX(bulletIcon.getOriginalOffsetX() - 29);
            ammoText.updateSize();
            ammoText.setOffsetX(bulletIcon.getOffsetX() - bulletIcon.width / 2);
        } else {
            bulletIcon.resetOffset();
            ammoText.resetOffset();
        }
    }
}
