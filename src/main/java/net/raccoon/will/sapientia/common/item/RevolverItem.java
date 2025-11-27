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
import net.raccoon.will.sapientia.core.registry.SapDamageSources;
import net.raccoon.will.sapientia.core.registry.SapItems;
import net.raccoon.will.sapientia.core.registry.SapSounds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RevolverItem extends Item {

    private static final int NUM_CHAMBERS_DEFAULT = 6;
    private static final int SHOOT_COOLDOWN_TICKS = 20; //1 sec
    private static final int RELOAD_COOLDOWN_TICKS = 40; //2 sec

    public RevolverItem(Properties properties) {
        super(properties);
    }

    public static String visualizeChambers(ItemStack stack) {
        int chambers = stack.getOrDefault(SapComponents.NUM_CHAMBERS.get(), 6);
        int current = stack.getOrDefault(SapComponents.CURRENT_CHAMBER.get(), 0);
        List<Integer> bullets = stack.getOrDefault(SapComponents.BULLET_CHAMBERS.get(), new ArrayList<>());

        StringBuilder sb = new StringBuilder();
        sb.append("Cylinder: [");
        for (int i = 0; i < chambers; i++) {
            if (i == current) sb.append(">"); // indicate current chamber
            else sb.append(" ");

            if (bullets.contains(i)) sb.append("X"); // bullet
            else sb.append("#"); // empty

            if (i == current) sb.append("<");
            else sb.append(" ");
        }
        sb.append("]");
        return sb.toString();
    }

    public List<Integer> getBulletChambers(ItemStack stack) {
        return stack.getOrDefault(SapComponents.BULLET_CHAMBERS.get(), new ArrayList<>());
    }

    public int getBulletCount(ItemStack stack) {
        return getBulletChambers(stack).size();
    }

    public int getNumChambers(ItemStack stack) {
        return stack.getOrDefault(SapComponents.NUM_CHAMBERS.get(), NUM_CHAMBERS_DEFAULT);
    }

    public int getCurrentChamber(ItemStack stack) {
        return stack.getOrDefault(SapComponents.CURRENT_CHAMBER.get(), 0);
    }

    public void setCurrentChamber(ItemStack stack, int value) {
        stack.set(SapComponents.CURRENT_CHAMBER.get(), value % getNumChambers(stack));
    }

    private void advanceCylinder(ItemStack stack) {
        setCurrentChamber(stack, getCurrentChamber(stack) + 1);
    }

    private void spinCylinder(ItemStack stack, RandomSource random) {
        int chambers = getNumChambers(stack);
        setCurrentChamber(stack, random.nextInt(chambers));
        System.out.println(visualizeChambers(stack));
    }

    private boolean removeBulletFromInventory(Player player) {
        if (player.isCreative()) return true;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(SapItems.BULLET.get())) {
                stack.shrink(1);
                return true;
            }
        }
        return false;
    }

    public void reload(ItemStack stack, Player player) {
        System.out.println(visualizeChambers(stack));

        if (player.getCooldowns().isOnCooldown(this)) return;

        List<Integer> bullets = new ArrayList<>(getBulletChambers(stack));
        int maxChambers = getNumChambers(stack);

        // calculate empty chambers
        List<Integer> emptyChambers = new ArrayList<>();
        for (int i = 0; i < maxChambers; i++) {
            if (!bullets.contains(i)) emptyChambers.add(i);
        }

        if (emptyChambers.isEmpty()) return; // cylinder full

        // shuffle empty chambers for randomness
        Collections.shuffle(emptyChambers, new Random(player.getRandom().nextLong()));

        int bulletsLoaded = 0;
        for (int chamber : emptyChambers) {
            if (removeBulletFromInventory(player)) {
                bullets.add(chamber);
                bulletsLoaded++;
            } else {
                break; // no more bullets in inventory
            }
        }

        if (bulletsLoaded == 0) {
            player.displayClientMessage(Component.literal("No bullets to reload!"), true);
            return;
        }

        // set bullets in stack
        stack.set(SapComponents.BULLET_CHAMBERS.get(), bullets);

        // pick a random bullet in cylinder to be current
        int randomCurrent = bullets.get(new Random().nextInt(bullets.size()));
        setCurrentChamber(stack, randomCurrent);

        // play sound and set cooldown
        player.level().playLocalSound(player, SapSounds.REVOLVER_SPIN.get(), SoundSource.PLAYERS, 1f, 1f);
        player.getCooldowns().addCooldown(this, RELOAD_COOLDOWN_TICKS);
    }


    // ----- Shooting ----- \\
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        List<Integer> bullets = new ArrayList<>(getBulletChambers(stack));
        int current = getCurrentChamber(stack);

        //spin cylinder if crouching
        if (player.isCrouching()) {
            spinCylinder(stack, player.getRandom());
            level.playLocalSound(player, SapSounds.REVOLVER_SPIN.get(), SoundSource.PLAYERS, 1f, 1f);
            advanceCylinder(stack);
            return InteractionResultHolder.consume(stack);
        }

        //cooldown
        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(stack);
        }

        //shooting
        if (bullets.contains(current)) {
            bullets.remove((Integer) current);
            stack.set(SapComponents.BULLET_CHAMBERS.get(), bullets);
            player.getCooldowns().addCooldown(this, SHOOT_COOLDOWN_TICKS);

            if (!level.isClientSide) {
                HitResult hit = hitscan(player, 40);
                if (hit.getType() == HitResult.Type.ENTITY) {
                    EntityHitResult entityHit = (EntityHitResult) hit;
                    Entity target = entityHit.getEntity();
                    if (target instanceof LivingEntity living) {
                        living.hurt(SapDamageSources.revolver(living.level(), player), 67f);
                    }
                } else if (hand == InteractionHand.OFF_HAND) {
                    player.hurt(SapDamageSources.revolver(player.level(), player), 67f);
                }
            }
            level.playLocalSound(player, SapSounds.REVOLVER_SHOOT.get(), SoundSource.PLAYERS, 1f, 1f);
        } else {
            level.playLocalSound(player, SapSounds.REVOLVER_EMPTY.get(), SoundSource.PLAYERS, 1f, 1f);
        }

        advanceCylinder(stack);
        return InteractionResultHolder.consume(stack);
    }

    // ----- Hitscan ----- \\
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

        double blockDist = blockHit.getType() == HitResult.Type.MISS ? Double.MAX_VALUE : blockHit.getLocation().distanceTo(eye);

        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                level,
                player,
                eye,
                reachVec,
                player.getBoundingBox().expandTowards(look.scale(maxDistance)).inflate(1.0),
                e -> e instanceof LivingEntity && e != player
        );

        double entityDist = entityHit == null ? Double.MAX_VALUE : entityHit.getLocation().distanceTo(eye);

        return entityDist < blockDist ? entityHit : blockHit;
    }
}