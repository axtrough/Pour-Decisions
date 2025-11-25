package net.raccoon.will.sapientia.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.raccoon.will.sapientia.core.registry.SapComponents;
import net.raccoon.will.sapientia.core.registry.SapDamageTypes;
import net.raccoon.will.sapientia.core.registry.SapItems;
import net.raccoon.will.sapientia.core.registry.SapSounds;

import java.util.ArrayList;
import java.util.List;

public class RevolverItem extends Item {


    public RevolverItem(Properties properties) {
        super(properties);
    }

    public List<Integer> getBulletChambers(ItemStack stack) {
        return stack.getOrDefault(SapComponents.BULLET_CHAMBERS.get(), new ArrayList<>());
    }

    public int getBulletCount(ItemStack stack) {
        return getBulletChambers(stack).size();
    }

    public int getNumChambers(ItemStack stack) {
        return stack.getOrDefault(SapComponents.NUM_CHAMBERS.get(), 6);
    }

    private static int getCurrentChamber(ItemStack stack) {
        return stack.getOrDefault(SapComponents.CURRENT_CHAMBER.get(), -1);
    }

    private static void setCurrentChamber(ItemStack stack, int value) {
        stack.set(SapComponents.CURRENT_CHAMBER.get(), value);
    }

    private void spinCylinder(ItemStack stack, RandomSource random) {
        int chambers = getNumChambers(stack);
        setCurrentChamber(stack, random.nextInt(chambers));
    }

    private void advanceCylinder(ItemStack stack) {
        int chambers = getNumChambers(stack);
        int next = (getCurrentChamber(stack) + 1) % chambers;
        setCurrentChamber(stack, next);
    }

    private boolean rollCylinder(ItemStack stack, Player player, Level level) {
        if (player.isCrouching()) {
            spinCylinder(stack, player.getRandom());
            level.playLocalSound(player, SapSounds.REVOLVER_SPIN.get(), SoundSource.PLAYERS, 1, 1);
            player.displayClientMessage(Component.literal("Spinning Cylinder..."), true);
            return true;
        }
        return false;
    }

    private void clampChambers(ItemStack stack) {
        int chambers = getNumChambers(stack);

        List<Integer> bullets = new ArrayList<>(getBulletChambers(stack));
        bullets.removeIf(b -> b >= chambers);
        stack.set(SapComponents.BULLET_CHAMBERS.get(), bullets);

        int current = getCurrentChamber(stack);
        if (current >= chambers) setCurrentChamber(stack, 0);
    }

    public void reload(ItemStack gun, Player player) {
        List<Integer> chambers = getBulletChambers(gun);
        List<Integer> newChambers = new ArrayList<>(chambers);

        int max = getNumChambers(gun);
        int bulletsNeeded = max - newChambers.size();
        int loaded = 0;

        for (int i = 0; i < bulletsNeeded; i++) {
            if (removeBulletFromInventory(player)) {
                newChambers.add(newChambers.size());
                loaded++;
            } else {
                break;
            }
        }
        gun.set(SapComponents.BULLET_CHAMBERS.get(), newChambers);

        if (loaded > 0) {
            player.level().playSound(null, player.blockPosition(), SapSounds.REVOLVER_SPIN.get(), SoundSource.PLAYERS, 1f, 1f);
        }
    }

    private boolean removeBulletFromInventory(Player player) {
        if  (player.isCreative()) {
            return true;
        }

        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(SapItems.BULLET.get())) {
                stack.shrink(1);
                return true;
            }
        }
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        clampChambers(stack);


        List<Integer> bullets = getBulletChambers(stack);
        int current = getCurrentChamber(stack);

        if (bullets.isEmpty()) {
            player.displayClientMessage(Component.literal("No bullets in gun."), true);
            level.playLocalSound(player, SapSounds.REVOLVER_EMPTY.get(), SoundSource.PLAYERS, 1, 1);

            if (rollCylinder(stack, player, level)) return InteractionResultHolder.consume(stack);
            advanceCylinder(stack);
            return InteractionResultHolder.consume(stack);
        }

        if (rollCylinder(stack, player, level)) return InteractionResultHolder.consume(stack);

        if (bullets.contains(current)) {

            if (!level.isClientSide()) {

                if (hand == InteractionHand.OFF_HAND) {
                    player.hurt(SapDamageTypes.causeRevolverSuicide(player.level().registryAccess()), 67f);

                } else if (hand == InteractionHand.MAIN_HAND) {
                    HitResult hit = hitscan(player, 40);

                    if (hit.getType() == HitResult.Type.ENTITY) {
                        EntityHitResult entityHit = (EntityHitResult) hit;
                        Entity target = entityHit.getEntity();

                        if (target instanceof LivingEntity living) {
                            target.hurt(SapDamageTypes.causeRevolverShot(target.level().registryAccess()), 67f);
                        }
                    }
                }
            }

            player.displayClientMessage(Component.literal("BANG!"), true);
            level.playLocalSound(player, SapSounds.REVOLVER_SHOOT.get(), SoundSource.PLAYERS, 1, 1);

            bullets.remove((Integer) current);
            stack.set(SapComponents.BULLET_CHAMBERS.get(), bullets);

        } else {
            player.displayClientMessage(Component.literal("It didn't shoot loser"), true);
            level.playLocalSound(player, SapSounds.REVOLVER_EMPTY.get(), SoundSource.PLAYERS, 1, 1);
        }

        advanceCylinder(stack);
        return InteractionResultHolder.consume(stack);
    }


    public static HitResult hitscan(Player player, double maxDistance) {
        Level level = player.level();
        Vec3 eye = player.getEyePosition(1f);
        Vec3 look = player.getLookAngle();
        Vec3 reachVec = eye.add(look.scale(maxDistance));

        HitResult blockHit = level.clip(new ClipContext(
                eye,
                reachVec,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));

        double blockDist = blockHit.getType() == HitResult.Type.MISS
                ? Double.MAX_VALUE
                : blockHit.getLocation().distanceTo(eye);


        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                level,
                player,
                eye,
                reachVec,
                player.getBoundingBox().expandTowards(look.scale(maxDistance)).inflate(1.0),
                e -> e instanceof LivingEntity && e != player
        );

        double entityDist = entityHit == null
                ? Double.MAX_VALUE
                : entityHit.getLocation().distanceTo(eye);

        return entityDist < blockDist ? entityHit : blockHit;
    }
}