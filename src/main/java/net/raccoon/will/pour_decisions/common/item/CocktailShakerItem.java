package net.raccoon.will.pour_decisions.common.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.raccoon.will.pour_decisions.common.item.data.CocktailData;
import net.raccoon.will.pour_decisions.common.item.data.CocktailShakerData;
import net.raccoon.will.pour_decisions.core.registry.CocktailRegistry;
import net.raccoon.will.pour_decisions.core.registry.PDComponents;
import net.raccoon.will.pour_decisions.core.registry.PDItems;
import net.raccoon.will.pour_decisions.core.registry.PDTags;

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
        ItemStack shaker = player.getItemInHand(usedHand);

        ItemStack offhand = player.getOffhandItem();
        if (!offhand.isEmpty() && offhand.getItem() instanceof BeverageItem booze) {
            addFluidToShaker(shaker, booze.getFluid());

            Integer uses = offhand.get(PDComponents.BEVERAGE_USES.get());
            if (uses == null) uses = booze.getFluidUses();

            if (uses > 1) {
                offhand.set(PDComponents.BEVERAGE_USES.get(), uses - 1);
            } else {
                // Last use, remove the item
                offhand.shrink(1);
            }

            if (offhand.getMaxDamage() > 0) {
                offhand.hurtAndBreak(1, player, EquipmentSlot.OFFHAND);
            }

            player.containerMenu.broadcastChanges();
        }

        CocktailShakerData data = shaker.get(PDComponents.SHAKER_DATA.get());
        if (data == null) {
            data = new CocktailShakerData(new ArrayList<>(), 0, MAX_SHAKE, ItemStack.EMPTY);
            shaker.set(PDComponents.SHAKER_DATA.get(), data);
        }

        if (!data.result().isEmpty()) {
            if (!level.isClientSide) {
                player.getInventory().add(data.result().copy());
            }
            reset(shaker);
            return InteractionResultHolder.sidedSuccess(shaker, level.isClientSide());
        }
        player.startUsingItem(usedHand);
        return InteractionResultHolder.consume(shaker);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        if (!(entity instanceof Player player)) return;

        CocktailShakerData data = stack.get(PDComponents.SHAKER_DATA.get());
        if (data == null || !data.result().isEmpty()) {
            return;
        }

        int progress = data.shakeProgress() + 1;

        if (!level.isClientSide) {
            ItemStack result = ItemStack.EMPTY;

            if (progress >= data.maxShake()) {
                result = makeDrinkResult(stack);
            }

            if (!result.isEmpty()) {
                clearFluids(stack);
            }

            CocktailShakerData newData = new CocktailShakerData(
                    data.ingredients(),
                    progress,
                    data.maxShake(),
                    result
            );
            stack.set(PDComponents.SHAKER_DATA.get(), newData);

            if (progress >= data.maxShake()) {
                entity.releaseUsingItem();
            }
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }


    private ItemStack makeDrinkResult(ItemStack shaker) {
        CocktailData cocktail = shaker.get(PDComponents.COCKTAIL_FLUIDS.get());
        if (cocktail == null) return ItemStack.EMPTY;

        List<String> fluids = cocktail.fluids();
        return CocktailRegistry.getResultFor(fluids);
    }

    private void addFluidToShaker(ItemStack shaker, String fluid) {
        CocktailData data = shaker.get(PDComponents.COCKTAIL_FLUIDS.get());

        List<String> fluids;
        if (data == null) {
            fluids = new ArrayList<>();
        } else {
            fluids = new ArrayList<>(data.fluids());
        }

        fluids.add(fluid);

        shaker.set(PDComponents.COCKTAIL_FLUIDS.get(), new CocktailData(fluids));

    }

    public boolean isValidIngredient(ItemStack item) {
        return item.is(PDTags.Items.COCKTAIL_INGREDIENTS);
    }

    private void clearFluids(ItemStack shaker) {
        shaker.set(PDComponents.COCKTAIL_FLUIDS.get(), new CocktailData(List.of()));
    }

    private void reset(ItemStack stack) {
        stack.set(PDComponents.SHAKER_DATA.get(),
                new CocktailShakerData(new ArrayList<>(), 0 , MAX_SHAKE, ItemStack.EMPTY));
    }
}
