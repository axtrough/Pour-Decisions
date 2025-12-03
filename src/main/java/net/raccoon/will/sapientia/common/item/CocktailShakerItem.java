package net.raccoon.will.sapientia.common.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.raccoon.will.sapientia.common.item.data.CocktailShakerData;
import net.raccoon.will.sapientia.core.registry.SapComponents;

import java.util.ArrayList;
import java.util.List;

public class CocktailShakerItem extends Item {
    private static final int MAX_INGREDIENTS = 5;
    private static final int MAX_SHAKE = 30;

    public CocktailShakerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        CocktailShakerData data = stack.get(SapComponents.COCKTAIL_DATA.get());
        if (data == null) {
            data = new CocktailShakerData(new ArrayList<>(), 0, MAX_SHAKE, ItemStack.EMPTY);
            stack.set(SapComponents.COCKTAIL_DATA.get(), data);
        }

        if (!data.result().isEmpty()) {
            if (!level.isClientSide) {
                player.getInventory().add(data.result().copy());
            }
            reset(stack);
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }
        player.startUsingItem(usedHand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        if (!(entity instanceof Player player)) return;

        // LOG: print where this is running
        System.out.println("[CocktailShaker] onUseTick called. isClientSide=" + level.isClientSide() +
                " rem=" + remainingUseDuration);

        CocktailShakerData data = stack.get(SapComponents.COCKTAIL_DATA.get());
        System.out.println("[CocktailShaker] current data: " + data);

        if (data == null || !data.result().isEmpty()) {
            System.out.println("[CocktailShaker] aborting useTick: data null or result present");
            return;
        }

        int progress = data.shakeProgress() + 1;
        System.out.println("[CocktailShaker] incrementing progress -> " + progress);

        if (!level.isClientSide) {
            // after setting, read it back and log it
            CocktailShakerData newData = new CocktailShakerData(
                    data.ingredients(),
                    progress,
                    data.maxShake(),
                    progress >= data.maxShake() ? new ItemStack(Items.DIRT) : ItemStack.EMPTY
            );
            stack.set(SapComponents.COCKTAIL_DATA.get(), newData);

            CocktailShakerData verify = stack.get(SapComponents.COCKTAIL_DATA.get());
            System.out.println("[CocktailShaker] wrote newData, verify read: " + verify);

            if (progress >= data.maxShake()) {
                entity.releaseUsingItem();
            }
        }
    }

    public boolean tryAddIngredient(ItemStack shaker, ItemStack ingredient) {
        CocktailShakerData data = shaker.get(SapComponents.COCKTAIL_DATA.get());
        if (data == null || !data.result().isEmpty()) return false;

        List<ItemStack> list = new ArrayList<>(data.ingredients());
        if (list.size() >= MAX_INGREDIENTS) return false;

        list.add(ingredient.copy());

        shaker.set(SapComponents.COCKTAIL_DATA.get(),
                new CocktailShakerData(list, data.shakeProgress(), data.maxShake(), ItemStack.EMPTY));

        return true;
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack shaker, Slot slot, ClickAction action, Player player) {
        if (action != ClickAction.SECONDARY || !slot.hasItem()) return false;

        if (player.level().isClientSide) return true;

        ItemStack ingredient = slot.getItem();

        if (tryAddIngredient(shaker, ingredient)) {
            slot.remove(1);
            player.containerMenu.broadcastChanges();
            return true;
        }
        return false;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack shaker, ItemStack incoming, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (action != ClickAction.SECONDARY || incoming.isEmpty()) return false;

        if (player.level().isClientSide) return true;

        if (tryAddIngredient(shaker, incoming)) {
            incoming.shrink(1);
            access.set(incoming.isEmpty() ? ItemStack.EMPTY : incoming);
            player.containerMenu.broadcastChanges();
            return true;
        }
        return false;
    }

    private void reset(ItemStack stack) {
        stack.set(SapComponents.COCKTAIL_DATA.get(),
                new CocktailShakerData(new ArrayList<>(), 0 , MAX_SHAKE, ItemStack.EMPTY));
    }
}
